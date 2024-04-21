package com.jh.EVSherpa.global.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggerAspect {
    @Before("execution(* com.jh.EVSherpa..*(..))")
    public void logMethodAccessBefore(JoinPoint joinPoint) {
        String _class = joinPoint.getTarget().getClass().getSimpleName();
        String _method = joinPoint.getSignature().getName();

        log.debug("START : {}", _class + " " + _method);
    }

    @AfterReturning("execution(* com.jh.EVSherpa..*(..))")
    public void logMethodAccessAfter(JoinPoint joinPoint) {
        String _class = joinPoint.getTarget().getClass().getSimpleName();
        String _method = joinPoint.getSignature().getName();

        log.debug("END : {}", _class + " " + _method);
    }
}
