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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.webapp.madrasati.core.error.MethodExecutionException;

@Aspect
@EnableTransactionManagement
@Component
public class AspectApplication {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PlatformTransactionManager transactionManager;

    public AspectApplication(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Pointcut("execution(* com.webapp.madrasati..service.*.*(..))")
    public void serviceLayerExecution() {
    }

    @Before("serviceLayerExecution()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        if (logger.isDebugEnabled()) {
            logMethodDetails(joinPoint, "called");
        }
    }

    @AfterReturning(pointcut = "serviceLayerExecution()", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        if (logger.isDebugEnabled()) {
            logMethodDetails(joinPoint, "executed");
            logger.debug("Result: {}", result);
        }
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
        return logAndMeasureExecutionTime(joinPoint);
    }

    @Pointcut("@annotation(com.webapp.madrasati.core.annotation.LoggMethod)")
    public void logMethodAnnotation() {
    }

    @Around("logMethodAnnotation()")
    public Object logAnnotatedMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAndMeasureExecutionTime(joinPoint);
    }

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object manageAndLogTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            return result;
        } catch (Exception ex) {
            transactionManager.rollback(status);
            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - start;
            logger.info("Transaction for {}.{} took {} ms",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    duration);
        }
    }

    private void logMethodDetails(JoinPoint joinPoint, String state) {
        if (logger.isDebugEnabled()) {
            logger.debug("Method {}: {}.{} with arguments: {}",
                    state,
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    Arrays.stream(joinPoint.getArgs())
                            .map(arg -> arg == null ? "null" : arg.toString())
                            .collect(Collectors.joining(", ")));
        }
    }

    private Object logAndMeasureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = joinPoint.getTarget().getClass().getSimpleName() + "." + method.getName();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            throw new MethodExecutionException("Exception in " + methodName, throwable);
        } finally {
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();

            // Only log execution time if it exceeds 500ms
            if (executionTime > 500) {
                logger.info("Method {} executed in {} ms", methodName, executionTime);
            }

            // Log return result only in debug level
            if (logger.isDebugEnabled() && result != null) {
                logger.debug("Method {} returned: {}", methodName, result);
            }
        }
    }
}
