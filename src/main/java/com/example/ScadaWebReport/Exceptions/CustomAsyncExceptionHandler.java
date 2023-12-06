package com.example.ScadaWebReport.Exceptions;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        // логика обработки необработанных исключений
        System.err.println("Unexpected exception occurred in asynchronous method: " + method.getName());
        System.err.println("Exception message: " + throwable.getMessage());
        System.err.println("Timestamp: " + System.currentTimeMillis());
        throwable.printStackTrace();
        
    }
}