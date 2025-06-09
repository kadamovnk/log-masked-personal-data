package ru.edme.custom.logger;

import ru.edme.annotation.SensitiveField;
import ru.edme.pattern.MaskingPattern;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class SensitiveDataMasker {
    private static final Map<Class<?>, Map<String, Field>> SENSITIVE_FIELD_CACHE = new ConcurrentHashMap<>();
    private static final Map<Field, MaskingPattern[]> PATTERN_CACHE = new ConcurrentHashMap<>();
    
    public static Object[] maskArgs(Object... args) {
        if (args == null || args.length == 0) {
            return args;
        }
        
        Object[] maskedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            maskedArgs[i] = mask(args[i]);
        }
        return maskedArgs;
    }
    
    public static Object mask(Object obj) {
        if (obj == null) {
            return null;
        }
        
        if (obj instanceof MaskArg maskArg) {
            return maskWithPatterns(maskArg.value(), maskArg.maskData() != null ? maskArg.maskData().patterns() : new MaskingPattern[0]);
        }
        if (obj instanceof LocalDate date) {
            return date.toString();
        }
        if (obj instanceof String value) {
            return value;
        }
        return maskObject(obj);
    }
    
    public static Object mask(Object obj, MaskingPattern pattern) {
        if (obj == null) {
            return null;
        }
        
        if (obj instanceof LocalDate date) {
            return pattern.applyTo(date.format(ISO_LOCAL_DATE));
        }
        if (obj instanceof String value) {
            return pattern.applyTo(value);
        }
        return mask(obj);
    }
    
    public static Object maskWithPatterns(Object obj, MaskingPattern[] patterns) {
        if (obj == null || patterns == null || patterns.length == 0) return mask(obj);
        if (obj instanceof String str) {
            String result = str;
            for (MaskingPattern pattern : patterns) result = pattern.applyTo(result);
            return result;
        }
        if (obj instanceof LocalDate date) {
            String result = date.format(ISO_LOCAL_DATE);
            for (MaskingPattern pattern : patterns) result = pattern.applyTo(result);
            return result;
        }
        return mask(obj, patterns[0]);
    }
    
    private static String maskObject(Object obj) {
        Class<?> clazz = obj.getClass();
        Map<String, Field> sensitiveFields = getSensitiveFields(clazz);
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) return obj.toString();
        StringBuilder result = new StringBuilder(clazz.getSimpleName()).append("{");
        boolean first = true;
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                String fieldName = field.getName();
                String maskedValue = getMaskedFieldValue(field, value, sensitiveFields);
                appendField(result, fieldName, maskedValue, first);
                first = false;
            } catch (Exception ignored) {}
        }
        result.append("}");
        return result.toString();
    }
    
    private static String getMaskedFieldValue(Field field, Object value, Map<String, Field> sensitiveFields) {
        String fieldName = field.getName();
        if (sensitiveFields.containsKey(fieldName)) {
            return maskFieldValue(field, value);
        } else if (shouldRecursivelyMask(value)) {
            return maskObject(value);
        } else {
            return String.valueOf(value);
        }
    }
    
    private static void appendField(StringBuilder sb, String fieldName, String maskedValue, boolean isFirst) {
        if (!isFirst) sb.append(", ");
        sb.append(fieldName).append("=").append(maskedValue);
    }
    
    private static boolean shouldRecursivelyMask(Object value) {
        return value != null &&
                !isSimpleType(value) &&
                !value.getClass().isEnum() &&
                !value.getClass().getPackageName().startsWith("java.");
    }
    
    private static Map<String, Field> getSensitiveFields(Class<?> clazz) {
        return SENSITIVE_FIELD_CACHE.computeIfAbsent(clazz, cls -> {
            Map<String, Field> map = new ConcurrentHashMap<>();
            for (Field field : cls.getDeclaredFields()) {
                if (field.isAnnotationPresent(SensitiveField.class)) {
                    field.setAccessible(true);
                    map.put(field.getName(), field);
                }
            }
            return map;
        });
    }
    
    private static String maskFieldValue(Field field, Object value) {
        if (value == null) {
            return null;
        }
        
        MaskingPattern[] patterns = getPatterns(field);
        if (patterns.length == 0) {
            if (value instanceof LocalDate date) {
                return date.toString();
            }
            return value.toString();
        }
        return maskWithPatterns(value, patterns).toString();
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
        if (obj == null) {
            return true;
        }
        
        Class<?> clazz = obj.getClass();
        return clazz.isPrimitive() || SIMPLE_TYPES.contains(clazz);
    }
    
    private static final Set<Class<?>> SIMPLE_TYPES = Set.of(
            String.class, Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class
    );
}