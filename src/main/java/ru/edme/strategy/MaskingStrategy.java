package ru.edme.strategy;

public interface MaskingStrategy<T> {
    T mask(T value);
}