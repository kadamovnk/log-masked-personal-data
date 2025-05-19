package ru.edme.custom.logger;

import ru.edme.pattern.MaskingPattern;

public record Mask(MaskingPattern[] patterns) {
    public static Mask with(MaskingPattern pattern) {
        return new Mask(new MaskingPattern[] {pattern});
    }
    
    public static Mask with(MaskingPattern... patterns) {
        return new Mask(patterns);
    }
}
