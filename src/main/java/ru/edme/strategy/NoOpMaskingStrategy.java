package ru.edme.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * No-op masking strategy. Returns the value unchanged.
 * Used as a fallback for types without a specific masking strategy.
 * @param <T> the type of value
 */
@Component
@Order(Integer.MAX_VALUE) // Lowest priority
public class NoOpMaskingStrategy<T> implements MaskingStrategy<T> {
    /**
     * Returns the value unchanged.
     * @param value the value to return
     * @return the same value
     */
    @Override
    public T mask(T value) {
        return value;
    }
}
