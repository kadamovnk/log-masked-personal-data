package ru.edme.strategy;

import org.springframework.stereotype.Component;
import ru.edme.model.MaskingPattern;

@Component
public class StringMaskingStrategy implements MaskingStrategy<String> {
    
    @Override
    public String mask(String value) {
        if (value == null) return null;
        
        String masked = value;
        
        for (MaskingPattern pattern : MaskingPattern.values()) {
            masked = pattern.getMaskedValue(masked);
        }
        
        return masked;
    }
}