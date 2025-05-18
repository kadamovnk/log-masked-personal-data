package ru.edme.strategy;

import org.springframework.stereotype.Component;
import ru.edme.model.MaskingPattern;

/**
 * Masking strategy for String values. Applies one or more MaskingPattern rules to the input string.
 */
@Component
public class StringMaskingStrategy implements MaskingStrategy<String> {
    
    /**
     * Masks the string using all available patterns if none are specified.
     * @param value the string to mask
     * @return masked string
     */
    @Override
    public String mask(String value) {
        if (value == null) return null;
        return mask(value, (MaskingPattern[]) null);
    }
    
    /**
     * Masks the string using the provided patterns.
     * @param value the string to mask
     * @param patterns the masking patterns to apply
     * @return masked string
     */
    public String mask(String value, MaskingPattern... patterns) {
        if (value == null) return null;
        String result = value;
        if (patterns != null && patterns.length > 0) {
            for (MaskingPattern pattern : patterns) {
                result = pattern.getMaskedValue(result);
            }
        } else {
            for (MaskingPattern pattern : MaskingPattern.values()) {
                result = pattern.getMaskedValue(result);
            }
        }
        return result;
    }
}
