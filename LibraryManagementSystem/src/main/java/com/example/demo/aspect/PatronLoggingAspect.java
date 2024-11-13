package com.example.demo.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PatronLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(PatronLoggingAspect.class);

    @Pointcut("execution(* com.example.demo.service.PatronService.*(..))")
    public void patronServiceMethods() {}

    @Before("patronServiceMethods()")
    public void logBeforePatronServiceMethods() {
        logger.info("A method in PatronService is about to be executed");
    }

    @AfterReturning("patronServiceMethods()")
    public void logAfterPatronServiceMethods() {
        logger.info("A method in PatronService has been executed successfully");
    }
}
