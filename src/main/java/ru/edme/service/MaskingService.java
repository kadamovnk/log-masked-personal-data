package ru.edme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.edme.annotation.SensitiveField;
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
    
    public void maskSensitiveFields(Object obj) {
        if (obj == null) return;
        
        ReflectionUtils.doWithFields(obj.getClass(), field -> {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null) {
                    field.set(obj, maskValue(value));
                }
            } catch (Exception e) {
                // Handle exception or log
            }
        }, field -> field.isAnnotationPresent(SensitiveField.class));
    }
}
