package ru.edme.aop.logger.annotation;

import ru.edme.pattern.MaskingPattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface SensitiveField {
    MaskingPattern[] patterns() default {};
}
