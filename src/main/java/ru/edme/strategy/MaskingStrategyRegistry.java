package ru.edme.strategy;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry for all available masking strategies. Allows lookup by type.
 * Automatically collects all @Component-annotated strategies and provides methods
 * to register and retrieve masking strategies for specific types.
 */
@Component
public class MaskingStrategyRegistry {
    private final Map<Class<?>, MaskingStrategy<?>> strategies = new HashMap<>();

    /**
     * Constructs the registry and registers all available MaskingStrategy implementations.
     * This automatically collects all @Component-annotated strategies.
     *
     * @param availableStrategies list of available MaskingStrategy implementations
     */
    public MaskingStrategyRegistry(List<MaskingStrategy<?>> availableStrategies) {
        for (MaskingStrategy<?> strategy : availableStrategies) {
            Class<?> targetType = determineTypeParameter(strategy);
            if (targetType != null) {
                registerStrategy(targetType, strategy);
            }
        }
    }

    /**
     * Registers a masking strategy for a specific type (internal use).
     *
     * @param type the class type
     * @param strategy the masking strategy
     */
    private <T> void registerStrategy(Class<?> type, MaskingStrategy<?> strategy) {
        strategies.put(type, strategy);
    }

    /**
     * Gets the masking strategy for the given type.
     * Returns a no-op strategy if none is found.
     *
     * @param type the class type
     * @param <T> the type parameter
     * @return the masking strategy for the type
     */
    @SuppressWarnings("unchecked")
    public <T> MaskingStrategy<T> getStrategy(Class<T> type) {
        if (type == null) return value -> value;
        return (MaskingStrategy<T>) strategies.getOrDefault(type,
                (MaskingStrategy<T>) strategies.get(Object.class));
    }

    /**
     * Uses a naming convention to determine the target type of a strategy.
     * Example: StringMaskingStrategy -> String.class
     *
     * @param strategy the masking strategy
     * @return the class type or null if not found
     */
    private Class<?> determineTypeParameter(MaskingStrategy<?> strategy) {
        String className = strategy.getClass().getSimpleName();
        if (className.endsWith("MaskingStrategy")) {
            String typeName = className.substring(0, className.length() - 15);
            try {
                try {
                    return Class.forName("java.lang." + typeName);
                } catch (ClassNotFoundException e) {
                    try {
                        return Class.forName("java.time." + typeName);
                    } catch (ClassNotFoundException ex) {
                        return null;
                    }
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}

