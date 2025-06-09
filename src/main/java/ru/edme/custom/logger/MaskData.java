package ru.edme.custom.logger;

import ru.edme.pattern.MaskingPattern;

public record MaskData(MaskingPattern[] patterns) {
    public static MaskData pattern(MaskingPattern pattern) {
        return new MaskData(new MaskingPattern[] {pattern});
    }
    
    public static MaskData pattern(MaskingPattern... patterns) {
        return new MaskData(patterns);
    }
}
