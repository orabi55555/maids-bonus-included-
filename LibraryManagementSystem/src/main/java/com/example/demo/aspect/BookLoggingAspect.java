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
public class BookLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(BookLoggingAspect.class);

    @Pointcut("execution(* com.example.demo.service.BookService.*(..))")
    public void bookServiceMethods() {}

    @Before("bookServiceMethods()")
    public void logBeforeBookServiceMethods() {
        logger.info("A method in BookService is about to be executed");
    }

    @AfterReturning("bookServiceMethods()")
    public void logAfterBookServiceMethods() {
        logger.info("A method in BookService has been executed successfully");
    }
}
