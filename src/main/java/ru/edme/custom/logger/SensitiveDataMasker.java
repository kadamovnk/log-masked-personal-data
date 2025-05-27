package ru.edme.custom.logger;

import ru.edme.aop.logger.annotation.SensitiveField;
import ru.edme.pattern.MaskingPattern;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
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
        if (obj == null) {
            return "null";
        }
        if (obj instanceof LocalDate date) return maskLocalDate(date, MaskingPattern.DATE_YYYY_MM_DD);
        if (obj instanceof String value) return maskString(value);
        return maskObject(obj);
    }
    
    /**
     * Mask a value with a specific pattern
     */
    public static Object mask(Object obj, MaskingPattern pattern) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof LocalDate date) return maskLocalDate(date, pattern);
        if (obj instanceof String value) return pattern.applyTo(value);
        return mask(obj);
    }
    
    /**
     * Smart string masking - using pattern detection
     */
    private static String maskString(String value) {
        if (value == null || value.length() < 3) {
            return value;
        }
        
        if (MaskingPattern.EMAIL.getCompiledPattern().matcher(value).matches()) {
            return MaskingPattern.EMAIL.applyTo(value);
        }
        
        return "***";
    }
    
    /**
     * Central method for masking with multiple patterns
     */
    public static Object maskWithPatterns(Object obj, MaskingPattern[] patterns) {
        if (obj == null) return "null";
        if (patterns == null || patterns.length == 0) return mask(obj);
        
        for (MaskingPattern pattern : patterns) {
            if (pattern == MaskingPattern.NO_MASK) {
                return obj.toString();
            }
        }
        
        if (patterns.length == 1) return mask(obj, patterns[0]);
        
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
        
        return buildMaskedObjectString(obj, clazz, sensitiveFields);
    }
    
    /**
     * Builds a string representation of the object with masked sensitive fields
     */
    private static String buildMaskedObjectString(Object obj, Class<?> clazz, Map<String, Field> sensitiveFields) {
        StringBuilder result = new StringBuilder(clazz.getSimpleName() + "{");
        
        Field[] fields = clazz.getDeclaredFields();
        boolean first = true;
        
        for (Field field : fields) {
            String formattedField = formatField(obj, field, sensitiveFields);
            if (formattedField != null) {
                if (!first) {
                    result.append(", ");
                }
                result.append(formattedField);
                first = false;
            }
        }
        
        result.append("}");
        return result.toString();
    }
    
    /**
     * Formats a single field for the masked object string
     */
    private static String formatField(Object obj, Field field, Map<String, Field> sensitiveFields) {
        try {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            
            if (sensitiveFields.containsKey(fieldName)) {
                return fieldName + "=" + maskFieldValue(field, value);
            } else if (shouldRecursivelyMask(value)) {
                return fieldName + "=" + maskObject(value);
            } else {
                return fieldName + "=" + value;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Determines if a field value should be recursively masked
     */
    private static boolean shouldRecursivelyMask(Object value) {
        return value != null &&
                !isSimpleType(value) &&
                !value.getClass().isEnum() &&
                !value.getClass().getPackageName().startsWith("java.");
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
        return clazz.isPrimitive() || SIMPLE_TYPES.contains(clazz);
    }
    
    private static final Set<Class<?>> SIMPLE_TYPES = Set.of(
            String.class,
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class
    );
}