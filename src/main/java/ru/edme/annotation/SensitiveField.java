package ru.edme.annotation;

import ru.edme.model.MaskingPattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SensitiveField {
    MaskingPattern[] patterns() default {};
    String customRegex() default "";
    String customReplacement() default "";
    boolean keepFirstChars() default false;
    int firstCharsToKeep() default 0;
    boolean keepLastChars() default false;
    int lastCharsToKeep() default 0;
}
