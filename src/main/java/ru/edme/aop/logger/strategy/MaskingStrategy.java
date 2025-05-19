package ru.edme.aop.logger.strategy;

public interface MaskingStrategy<T> {
    T mask(T value);
}
