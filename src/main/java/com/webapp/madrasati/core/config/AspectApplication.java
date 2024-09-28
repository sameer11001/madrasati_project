package com.webapp.madrasati.core.config;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.webapp.madrasati.core.error.MethodExecutionException;

@Aspect
@Component
public class AspectApplication {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.webapp.madrasati..service.*.*(..))")
    public void serviceLayerExecution() {
    }

    @Before("serviceLayerExecution()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger.info("Method Called: {}.{}() with arguments: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "serviceLayerExecution()", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        logger.info("Method Executed: {}.{}()",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
        logger.info("Result: {}", result);
    }

    @AfterThrowing(pointcut = "serviceLayerExecution()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Method Threw Exception: {}.{}() - Exception: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }

    @Around("serviceLayerExecution()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();
        logger.info("Execution time of {}.{}(): {} ms",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                stopWatch.getTotalTimeMillis());

        return result;
    }

    @Pointcut("@annotation(com.webapp.madrasati.core.annotation.LoggMethod)")
    public void logMethodAnnotation() {
    }

    @Around("logMethodAnnotation()")
    public Object logAnnotatedMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = joinPoint.getTarget().getClass().getSimpleName() + "." + method.getName();

        // Log method entry
        logger.info("Entering method: {}", methodName);

        // Log parameters
        String params = Arrays.stream(joinPoint.getArgs())
                .map(arg -> arg == null ? "null" : arg.toString())
                .collect(Collectors.joining(", "));
        logger.info("Method parameters: ({})", params);

        // Execute the method and measure time
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            String errorMessage = String.format("Exception occurred in %s", methodName);
            throw new MethodExecutionException(errorMessage, throwable);
        } finally {
            stopWatch.stop();

            // Log method exit
            logger.info("Exiting method: {}. Execution time: {} ms", methodName, stopWatch.getTotalTimeMillis());

            // Log return value
            if (result != null) {
                logger.info("Method returned: {}", result);
            }
        }
    }

}
