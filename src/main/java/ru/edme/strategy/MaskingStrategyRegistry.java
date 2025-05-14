package ru.edme.strategy;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class MaskingStrategyRegistry {
    private final Map<Class<?>, MaskingStrategy<?>> strategies = new HashMap<>();
    
    @PostConstruct
    public void init() {
        register(String.class, new StringMaskingStrategy());
        register(LocalDate.class, new LocalDateMaskingStrategy());
    }
    
    public <T> void register(Class<T> type, MaskingStrategy<T> strategy) {
        strategies.put(type, strategy);
    }
    
    @SuppressWarnings("unchecked")
    public <T> MaskingStrategy<T> getStrategy(Class<T> type) {
        return (MaskingStrategy<T>) strategies.getOrDefault(type,
                value -> value); // Default no-op strategy
    }
}