package ru.edme.aop.logger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.edme.aop.logger.annotation.SensitiveField;
import ru.edme.pattern.MaskingPattern;
import ru.edme.aop.logger.strategy.MaskingStrategy;
import ru.edme.aop.logger.strategy.MaskingStrategyRegistry;
import ru.edme.aop.logger.strategy.StringMaskingStrategy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for masking sensitive data in objects and strings using registered masking strategies.
 * Handles masking based on annotation configuration or type-based strategies.
 */
@Service
@RequiredArgsConstructor
public class MaskingService {
    private final MaskingStrategyRegistry strategyRegistry;
    
    // Cache: Class -> List of Sensitive Fields
    private static final Map<Class<?>, List<Field>> sensitiveFieldCache = new ConcurrentHashMap<>();
    // Cache: Field -> SensitiveField annotation
    private static final Map<Field, SensitiveField> annotationCache = new ConcurrentHashMap<>();
    
    /**
     * Apply masking to a value based on its type using the appropriate strategy.
     * @param value the value to mask
     * @return masked value
     */
    public <T> T maskValue(T value) {
        if (value == null) return null;
        
        @SuppressWarnings("unchecked")
        MaskingStrategy<T> strategy = (MaskingStrategy<T>) strategyRegistry.getStrategy(value.getClass());
        return strategy.mask(value);
    }
    
    /**
     * Mask a value based on the SensitiveField annotation's configuration.
     * @param value the value to mask
     * @param annotation the annotation specifying masking patterns
     * @return masked value
     */
    @SuppressWarnings("unchecked")
    public <T> T maskValue(T value, SensitiveField annotation) {
        if (value == null) return null;
        
        if (value instanceof String && annotation != null) {
            String stringValue = (String) value;
            // Only apply specific patterns if provided
            MaskingPattern[] patterns = annotation.patterns();
            if (patterns.length > 0) {
                StringMaskingStrategy stringStrategy = (StringMaskingStrategy) strategyRegistry.getStrategy(String.class);
                return (T) stringStrategy.mask(stringValue, patterns);
            }
        }
        
        return maskValue(value);
    }
    
    /**
     * Process an object, masking all fields with @SensitiveField annotations.
     * @param object the object to process
     * @return the object with masked sensitive fields
     */
    public <T> T maskSensitiveFields(T object) {
        if (object == null) return null;
        Class<?> clazz = object.getClass();
        
        // Get or compute sensitive fields for this class
        List<Field> sensitiveFields = sensitiveFieldCache.computeIfAbsent(clazz, c -> {
            List<Field> fields = new ArrayList<>();
            ReflectionUtils.doWithFields(c, field -> {
                if (field.isAnnotationPresent(SensitiveField.class)) {
                    field.setAccessible(true);
                    fields.add(field);
                    // Cache annotation for this field
                    annotationCache.put(field, field.getAnnotation(SensitiveField.class));
                }
            });
            return fields;
        });
        
        for (Field field : sensitiveFields) {
            try {
                Object value = field.get(object);
                if (value != null) {
                    SensitiveField annotation = annotationCache.get(field);
                    Object maskedValue = maskValue(value, annotation);
                    field.set(object, maskedValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error processing sensitive field: " + field.getName(), e);
            }
        }
        return object;
    }
}
