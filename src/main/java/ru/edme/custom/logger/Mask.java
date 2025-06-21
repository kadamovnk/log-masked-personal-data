package ru.edme.custom.logger;

import ru.edme.pattern.MaskingPattern;

public record Mask(Object args, MaskingPattern[] patterns) {
    public static Mask sensitive(Object value, MaskingPattern pattern) {
        return new Mask(value, new MaskingPattern[] {pattern});
    }
    
    public static Mask sensitive(Object value, MaskingPattern... patterns) {
        return new Mask(value, patterns);
    }
}
