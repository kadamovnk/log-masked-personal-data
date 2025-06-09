package ru.edme.custom.logger;

public record MaskArg(Object value, MaskData maskData) {
    public static MaskArg value(Object value, MaskData maskData) {
        return new MaskArg(value, maskData);
    }
}
