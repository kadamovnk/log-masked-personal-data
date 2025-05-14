package ru.edme.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.edme.other.Masking;

@Aspect
@Component
public class LogMaskingAspect {
    
    @Around("@annotation(ru.edme.annotation.LogMasked)")
    public Object maskMethodArgs(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = joinPoint.getArgs();
        
        for (Object arg : args) {
            if (arg instanceof String) {
                Masking.mask((String) arg);
            } else {
                Masking.maskSensitiveFields(arg);
            }
        }
        
        return joinPoint.proceed();
    }
}
