package ru.edme.aop.logger.strategy;

import org.springframework.stereotype.Component;

@Component
public class NoOpMaskingStrategy<T> implements MaskingStrategy<T> {
    
    @Override
    public T mask(T value) {
        return value;
    }
}
