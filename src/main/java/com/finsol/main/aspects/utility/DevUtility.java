package com.finsol.main.aspects.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.finsol.main.aspects.model.ExceptionCount;
import com.finsol.main.aspects.repository.Exception_Repo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.transaction.Transactional;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: Akella Visalakshi
 * @version: 1.0
 */

@Component
@Aspect
@RestControllerAdvice
public class DevUtility {

    @Autowired
    Exception_Repo repo;

    private static final Logger logger = LoggerFactory.getLogger(DevUtility.class);

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private final AtomicLong exceptionCount = new AtomicLong(0);
    private final CacheManager cacheManager;

    public DevUtility(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Pointcut("execution(* com.finsol.main..*.*(..))")
    public void finSolPackagePointcut() {}

    @Before("finSolPackagePointcut()")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        logger.info("Before method execution: {} and arguments are -->{}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @After("finSolPackagePointcut()")
    public void afterMethodExecution(JoinPoint joinPoint) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime.get();
        startTime.remove(); // Clean up ThreadLocal
        logger.info("The {} Method execution took {} milliseconds" ,joinPoint.getSignature(),duration  );
        logger.info("After method execution: {}", joinPoint.getSignature().toShortString());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode errorObj = mapper.createObjectNode();
        errorObj.put("error message", "error occurred while processing the request");
        StackTraceElement originatingElement = findOriginatingStackTraceElement(e.getStackTrace());
        String originatingClass = originatingElement.getClassName();
        String originatingMethod = originatingElement.getMethodName();
        int lineNumber = originatingElement.getLineNumber();
        logErrorDetails(originatingClass, originatingMethod, lineNumber, e);
        incrementExceptionCount();
        HttpStatus status = determineHttpStatus(e);
        return ResponseEntity.status(status).body(errorObj);
    }

    private HttpStatus determineHttpStatus(Exception e) {
        if (e instanceof NullPointerException) {
            return HttpStatus.BAD_REQUEST; // 400
        } else if (e instanceof IllegalArgumentException) {
            return HttpStatus.UNPROCESSABLE_ENTITY; // 422
        } else if (e instanceof AccessDeniedException) {
            return HttpStatus.FORBIDDEN; // 403
        }
        // Add more specific exceptions and their corresponding HttpStatus as needed

        return HttpStatus.INTERNAL_SERVER_ERROR; // Default status
    }

    private StackTraceElement findOriginatingStackTraceElement(StackTraceElement[] stackTrace) {
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith("com.finsol.main")) {
                return element;
            }
        }
        return stackTrace[0];
    }

    private void logErrorDetails(String originatingClass, String originatingMethod, int lineNumber, Exception e) {
        logger.error("In DevUtil with error in {} class in {} method at line number {}", originatingClass, originatingMethod, lineNumber);
        logger.error("Inside DevUtil with error ==> {}", e.getMessage());
    }

    public void incrementExceptionCount() {
        long newCount = exceptionCount.incrementAndGet();
        cacheManager.getCache("exceptionCountCache").put("exceptionCount", newCount);
        logger.info("Exception count incremented: {}", newCount);
    }

    @Scheduled(cron = "0 59 23 * * ?")
    public void resetExceptionCount() {
        long count = exceptionCount.get();
        updateDatabase(count);
        exceptionCount.set(0);
        cacheManager.getCache("exceptionCountCache").put("exceptionCount", 0L);
        logger.info("Exception count reset to 0.");
    }

    @Transactional
    public void updateDatabase(long count) {
        ExceptionCount ec = new ExceptionCount();
        ec.setDate(LocalDateTime.now());
        ec.setException_date(LocalDateTime.now());
        ec.setException_Count(count);
        repo.save(ec);
        logger.info("Updated database with exception count: {}", count);
    }
}
