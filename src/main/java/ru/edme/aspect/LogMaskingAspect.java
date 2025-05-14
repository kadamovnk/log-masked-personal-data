package ru.edme.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.edme.service.MaskingService;

@Aspect
@Component
@RequiredArgsConstructor
public class LogMaskingAspect {
    private final MaskingService maskingService;
    
    @Around("@annotation(ru.edme.annotation.LogMasked)")
    public Object maskMethodArgs(ProceedingJoinPoint joinPoint) throws Throwable {
        var args = joinPoint.getArgs();
        
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof String) {
                args[i] = maskingService.maskValue((String) arg);
            } else {
                maskingService.maskSensitiveFields(arg);
            }
        }
        
        return joinPoint.proceed(args);
    }
}
