package ru.edme.strategy;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Masking strategy for LocalDate values. Masks the date by setting month and day to 1.
 */
@Component
public class LocalDateMaskingStrategy implements MaskingStrategy<LocalDate> {
    
    /**
     * Masks the LocalDate by setting month and day to 1.
     * @param value the LocalDate to mask
     * @return masked LocalDate
     */
    @Override
    public LocalDate mask(LocalDate value) {
        if (value == null) return null;
        return LocalDate.of(value.getYear(), 1, 1);
    }
}
