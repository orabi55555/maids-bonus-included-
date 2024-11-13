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
public class BorrowingRecordLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(BorrowingRecordLoggingAspect.class);

    @Pointcut("execution(* com.example.demo.service.BorrowingRecordService.*(..))")
    public void borrowingRecordServiceMethods() {}

    @Before("borrowingRecordServiceMethods()")
    public void logBeforeBorrowingRecordServiceMethods() {
        logger.info("A method in BorrowingRecordService is about to be executed");
    }

    @AfterReturning("borrowingRecordServiceMethods()")
    public void logAfterBorrowingRecordServiceMethods() {
        logger.info("A method in BorrowingRecordService has been executed successfully");
    }
}
