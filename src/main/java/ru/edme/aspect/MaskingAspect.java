package ru.edme.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.edme.annotation.SensitiveField;
import ru.edme.annotation.SensitiveObject;
import ru.edme.custom.logger.SensitiveDataMasker;
import ru.edme.pattern.MaskingPattern;

import java.beans.Introspector;
import java.lang.reflect.Field;

@Aspect
public class MaskingAspect {

    @Pointcut("call(* org.slf4j.Logger.info(..)) || " +
            "call(* org.slf4j.Logger.warn(..)) || " +
            "call(* org.slf4j.Logger.error(..))")
    public void loggerMethods() {}

    @Around("call(public * ru.edme.model..get*(..)) && " +
            "target(targetObj)")
    public Object maskGetter(ProceedingJoinPoint pjp, Object targetObj) throws Throwable {

        Class<?> targetClass = targetObj.getClass();
        if (!targetClass.isAnnotationPresent(SensitiveObject.class)) {
            return pjp.proceed();
        }

        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String methodName = sig.getName();
        if (!methodName.startsWith("get") || methodName.length() == 3) {
            return pjp.proceed();
        }

        String prop = Introspector.decapitalize(methodName.substring(3));
        Field field;
        try {
            field = findField(targetClass, prop);
        } catch (NoSuchFieldException e) {
            return pjp.proceed();
        }

        SensitiveField ann = field.getAnnotation(SensitiveField.class);
        Object original = pjp.proceed();

        if (ann != null && original instanceof String str) {
            MaskingPattern[] patterns = ann.patterns();
            return SensitiveDataMasker.maskWithPatterns(str, patterns);
        }

        return original;
    }

    private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field " + fieldName + " not found in " + clazz);
    }
}