package ru.edme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.edme.annotation.SensitiveField;
import ru.edme.model.MaskingPattern;
import ru.edme.strategy.MaskingStrategy;
import ru.edme.strategy.MaskingStrategyRegistry;

@Service
@RequiredArgsConstructor
public class MaskingService {
    private final MaskingStrategyRegistry strategyRegistry;
    
    public <T> T maskValue(T value) {
        if (value == null) return null;
        
        @SuppressWarnings("unchecked")
        MaskingStrategy<T> strategy = (MaskingStrategy<T>)
                strategyRegistry.getStrategy(value.getClass());
        
        return strategy.mask(value);
    }
    
    public <T> T maskValue(T value, SensitiveField annotation) {
        if (value == null || !(value instanceof String)) {
            return maskValue(value);
        }
        
        String stringValue = (String) value;
        
        // Apply specific masking patterns if provided
        if (annotation.patterns().length > 0) {
            for (MaskingPattern pattern : annotation.patterns()) {
                stringValue = pattern.getMaskedValue(stringValue);
            }
        } else if (!annotation.customRegex().isEmpty()) {
            stringValue = stringValue.replaceAll(
                    annotation.customRegex(),
                    annotation.customReplacement().isEmpty() ? "***" : annotation.customReplacement()
            );
        } else {
            stringValue = (String) maskValue((T) stringValue);
        }
        
        @SuppressWarnings("unchecked")
        T result = (T) stringValue;
        return result;
    }
    
    public void maskSensitiveFields(Object obj) {
        if (obj == null) return;
        
        ReflectionUtils.doWithFields(obj.getClass(), field -> {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    SensitiveField annotation = field.getAnnotation(SensitiveField.class);
                    field.set(obj, maskValue(value, annotation));
                }
            } catch (Exception e) {
                // exception handling or logging will be added in the future
            }
        }, field -> field.isAnnotationPresent(SensitiveField.class));
    }
}