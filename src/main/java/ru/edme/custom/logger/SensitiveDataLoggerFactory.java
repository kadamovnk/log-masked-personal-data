package ru.edme.custom.logger;

public class SensitiveDataLoggerFactory {
    public static SensitiveDataLogger getLogger(Class<?> clazz) {
        return new SensitiveDataLogger(clazz);
    }
}
