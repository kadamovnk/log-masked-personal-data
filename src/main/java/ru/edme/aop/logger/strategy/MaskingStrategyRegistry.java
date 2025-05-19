package ru.edme.aop.logger.strategy;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MaskingStrategyRegistry {
    private final Map<Class<?>, MaskingStrategy<?>> strategies = new HashMap<>();
    
    public <T> void registerStrategy(Class<T> type, MaskingStrategy<T> strategy) {
        strategies.put(type, strategy);
    }
    
    @SuppressWarnings("unchecked")
    public <T> MaskingStrategy<T> getStrategy(Class<T> type) {
        if (type == null) return value -> value;
        return (MaskingStrategy<T>) strategies.getOrDefault(type,
                (MaskingStrategy<T>) strategies.get(Object.class));
    }
}

