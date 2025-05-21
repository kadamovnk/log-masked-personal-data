package ru.edme.custom.logger;

import ru.edme.aop.logger.annotation.SensitiveField;
import ru.edme.pattern.MaskingPattern;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SensitiveDataMasker {
    // Cache for sensitive fields
    private static final Map<Class<?>, Map<String, Field>> SENSITIVE_FIELD_CACHE = new ConcurrentHashMap<>();
    
    // Cache for masking patterns
    private static final Map<Field, MaskingPattern[]> PATTERN_CACHE = new ConcurrentHashMap<>();
    
    // Date formatter for ISO_LOCAL_DATE
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    
    /**
     * Main method to mask any object or value
     */
    public static Object mask(Object obj) {
        if (obj == null) return "null";
        
        if (obj instanceof LocalDate date) return maskLocalDate(date, MaskingPattern.DATE_YYYY_MM_DD);
        
        if (obj instanceof String value) return maskString(value);
        
        // For objects, use reflection to find sensitive fields
        return maskObject(obj);
    }
    
    /**
     * Mask a value with a specific pattern
     */
    public static Object mask(Object obj, MaskingPattern pattern) {
        if (obj == null) return "null";
        
        if (obj instanceof LocalDate date) return maskLocalDate(date, pattern);
        
        if (obj instanceof String value) return pattern.applyTo(value);
        
        return mask(obj);
    }
    
    /**
     * Smart string masking - using pattern detection
     */
    private static String maskString(String value) {
        boolean valueModified = false;
        String result = value;
        
        // Check for a phone pattern (more specific check)
        if (value.matches(".*\\+\\d{1,3}\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}.*")) {
            result = MaskingPattern.PHONE.applyTo(result);
            valueModified = true;
        }
        
        // Check for SNILS pattern (with dashes)
        if (value.matches(".*\\d{3}-\\d{3}-\\d{3}-\\d{2}.*")) {
            result = MaskingPattern.SNILS.applyTo(result);
            valueModified = true;
        }
        
        // Check for INN patterns (as before)
        if (value.matches(".*\\d{10,}.*")) {
            for (MaskingPattern pattern : new MaskingPattern[] {
                    MaskingPattern.INN_10_DIGITS, MaskingPattern.INN_12_DIGITS}) {
                if (pattern.getCompiledPattern().matcher(value).find()) {
                    result = pattern.applyTo(result);
                    valueModified = true;
                }
            }
        }
        
        // Check for an email pattern
        if (value.contains("@")) {
            result = MaskingPattern.EMAIL.applyTo(result);
            valueModified = true;
        }
        
        // Check for date patterns
        if (value.matches(".*\\d{2}[.-/]\\d{2}[.-/]\\d{4}.*") ||
                value.matches(".*\\d{4}[.-/]\\d{2}[.-/]\\d{2}.*")) {
            result = MaskingPattern.DATE_YYYY_MM_DD.applyTo(result);
            result = MaskingPattern.DATE_DD_MM_YYYY.applyTo(result);
            valueModified = true;
        }
        
        // Apply address masking last
        if (value.length() > 15 && value.contains(",")) {
            result = MaskingPattern.ADDRESS.applyTo(result);
            valueModified = true;
        }
        
        // If no specific pattern was applied, but we should still mask
        if (!valueModified && shouldMaskByDefault(value)) {
            if (containsSensitivePattern(value)) {
                if (value.contains("@")) {
                    result = MaskingPattern.EMAIL.applyTo(value);
                } else if (value.matches(".*\\d{10,}.*")) {
                    result = value.replaceAll("\\d{6,}", "******");
                } else {
                    int length = value.length();
                    result = value.substring(0, Math.min(length/4, 3)) +
                            "*".repeat(Math.max(length - Math.min(length/4, 3) * 2, 3)) +
                            value.substring(Math.max(length - Math.min(length/4, 3), 0));
                }
            }
        }
        
        return result;
    }
    
    /**
     * Helper method to check if a string potentially contains sensitive data
     */
    private static boolean containsSensitivePattern(String value) {
        // Check for typical sensitive patterns
        return value.matches(".*\\d{4,}.*") ||          // Contains 4+ digits
                value.length() >= 8 ||                   // Long enough to be sensitive
                value.matches(".*[A-Za-z]+\\d+.*") ||    // Contains letters followed by digits
                value.matches(".*@.*");                  // Contains @ symbol
    }
    
    /**
     * Determine if a value should be masked by default logic
     */
    private static boolean shouldMaskByDefault(String value) {
        // Skip very short values and common non-sensitive strings
        if (value.length() < 3) return false;
        
        // Skip common non-sensitive keywords
        String lowerValue = value.toLowerCase();
        String[] nonSensitiveWords = {"true", "false", "yes", "no", "ok", "cancel", "none"};
        for (String word : nonSensitiveWords) {
            if (lowerValue.equals(word)) return false;
        }
        
        return true;
    }
    
    /**
     * Central method for masking with multiple patterns
     */
    public static Object maskWithPatterns(Object obj, MaskingPattern[] patterns) {
        if (obj == null) return "null";
        if (patterns == null || patterns.length == 0) return mask(obj);
        
        // Check if NO_MASK pattern is present - if so, return the original value
        for (MaskingPattern pattern : patterns) {
            if (pattern == MaskingPattern.NO_MASK) {
                return obj.toString();
            }
        }
        
        // For a single pattern, use the simple mask method
        if (patterns.length == 1) return mask(obj, patterns[0]);
        
        // Handle multiple patterns
        if (obj instanceof String result) {
            for (MaskingPattern pattern : patterns) {
                result = pattern.applyTo(result);
            }
            return result;
        } else if (obj instanceof LocalDate date) {
            for (MaskingPattern pattern : patterns) {
                if (pattern == MaskingPattern.DATE_YYYY_MM_DD) {
                    return maskLocalDate(date, pattern);
                }
            }
            return maskLocalDate(date, MaskingPattern.DATE_YYYY_MM_DD);
        } else {
            return mask(obj, patterns[0]);
        }
    }
    
    /**
     * Masks an object by inspecting its fields for @SensitiveField annotations
     */
    private static String maskObject(Object obj) {
        Class<?> clazz = obj.getClass();
        Map<String, Field> sensitiveFields = getSensitiveFields(clazz);
        
        if (sensitiveFields.isEmpty()) {
            return obj.toString();
        }
        
        StringBuilder result = new StringBuilder(clazz.getSimpleName() + "{");
        
        Field[] fields = clazz.getDeclaredFields();
        boolean first = true;
        
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                
                if (!first) {
                    result.append(", ");
                }
                
                if (sensitiveFields.containsKey(fieldName)) {
                    // Sensitive field, mask it
                    result.append(fieldName).append("=").append(maskFieldValue(field, value));
                } else if (value != null && !isSimpleType(value) && !value.getClass().isEnum() &&
                        !value.getClass().getPackageName().startsWith("java.")) {
                    // Recursively mask nested objects
                    result.append(fieldName).append("=").append(maskObject(value));
                } else {
                    // Regular field, no masking needed
                    result.append(fieldName).append("=").append(value);
                }
                
                first = false;
            } catch (Exception e) {
                // Silently fail and continue with the next field
            }
        }
        
        result.append("}");
        return result.toString();
    }
    
    /**
     *  Masking LocalDate objects
     */
    private static String maskLocalDate(LocalDate date, MaskingPattern pattern) {
        String formatted = date.format(ISO_DATE);
        return pattern.applyTo(formatted);
    }
    
    private static Map<String, Field> getSensitiveFields(Class<?> clazz) {
        return SENSITIVE_FIELD_CACHE.computeIfAbsent(clazz, cls -> Arrays.stream(cls.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(SensitiveField.class))
                .collect(Collectors.toMap(
                        Field::getName,
                        field -> {
                            field.setAccessible(true);
                            return field;
                        }
                )));
    }
    
    private static String maskFieldValue(Field field, Object value) {
        if (value == null) return "null";
        
        MaskingPattern[] patterns = getPatterns(field);
        
        if (patterns.length == 0) {
            String stringValue = value.toString();
            return "*".repeat(Math.min(stringValue.length(), 5));
        } else {
            return maskWithPatterns(value, patterns).toString();
        }
    }
    
    private static MaskingPattern[] getPatterns(Field field) {
        return PATTERN_CACHE.computeIfAbsent(field, f -> {
            SensitiveField annotation = f.getAnnotation(SensitiveField.class);
            assert annotation != null;
            MaskingPattern[] patterns = annotation.patterns();
            return patterns.length > 0 ? patterns : new MaskingPattern[0];
        });
    }
    
    private static boolean isSimpleType(Object obj) {
        if (obj == null) return true;
        
        Class<?> clazz = obj.getClass();
        return clazz.isPrimitive() ||
                clazz == String.class ||
                clazz == Boolean.class ||
                clazz == Character.class ||
                Number.class.isAssignableFrom(clazz);
    }
}