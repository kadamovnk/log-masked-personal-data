package ru.edme.custom.logger;

import org.springframework.stereotype.Component;
import ru.edme.aop.logger.annotation.SensitiveField;
import ru.edme.pattern.MaskingPattern;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class SensitiveDataMasker {
    // Cache for sensitive fields
    private static final Map<Class<?>, Map<String, Field>> SENSITIVE_FIELD_CACHE = new ConcurrentHashMap<>();
    
    // Cache for masking patterns
    private static final Map<Field, MaskingPattern[]> PATTERN_CACHE = new ConcurrentHashMap<>();
    
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    
    /**
     * Main method to mask any object or value
     */
    public static Object mask(Object obj) {
        if (obj == null) return "null";
        
        if (obj instanceof LocalDate) return mask(obj, MaskingPattern.DATE_YYYY_MM_DD);
        
        if (obj instanceof String) return maskStringWithAllPatterns((String) obj);
        
        // For complex objects, use reflection to find sensitive fields
        return maskObject(obj);
    }
    
    /**
     * Mask a value with a specific pattern
     */
    public static Object mask(Object obj, MaskingPattern pattern) {
        if (obj == null) return "null";
        
        if (obj instanceof LocalDate) return maskLocalDate((LocalDate) obj, pattern);
        
        if (!(obj instanceof String)) return mask(obj);
        
        // Apply the specific pattern to the string
        return pattern.applyTo((String) obj);
    }
    
    /**
     * Masks a string with all available patterns
     */
    private static String maskStringWithAllPatterns(String value) {
        String result = value;
        
        for (MaskingPattern pattern : MaskingPattern.values()) {
            if (pattern.getCompiledPattern().matcher(result).find()) {
                result = pattern.applyTo(result);
            }
        }
        return result;
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
                    // This is a sensitive field, mask it
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
        
        String stringValue = value.toString();
        MaskingPattern[] patterns = getPatterns(field);
        
        if (value instanceof LocalDate) {
            for (MaskingPattern pattern : patterns) {
                if (pattern == MaskingPattern.DATE_YYYY_MM_DD) {
                    return maskLocalDate((LocalDate) value, pattern);
                }
            }
            
            return maskLocalDate((LocalDate)value, MaskingPattern.DATE_YYYY_MM_DD);
        }
        
        if (patterns.length == 0) {
            return "*".repeat(Math.min(stringValue.length(), 5));
        } else {
            for (MaskingPattern pattern : patterns) {
                stringValue = pattern.applyTo(stringValue);
            }
            return stringValue;
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