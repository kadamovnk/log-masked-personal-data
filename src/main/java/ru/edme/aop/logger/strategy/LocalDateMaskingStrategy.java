package ru.edme.aop.logger.strategy;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LocalDateMaskingStrategy implements MaskingStrategy<LocalDate> {
    
    @Override
    public LocalDate mask(LocalDate value) {
        if (value == null) return null;
        return LocalDate.of(value.getYear(), 1, 1);
    }
}
