package com.example.ScadaWebReport.services;

import org.springframework.stereotype.Service;


import java.util.function.Supplier;


//Метод для попытки подлкючения к базе с нескольких попыток
@Service
public class DatabaseConnectionService {

	public <T> T executeWithRetry(Supplier<T> databaseOperation) {
        int maxRetries = 10;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                return databaseOperation.get();
            } catch (Exception e) {
                
                e.printStackTrace();
                retryCount++;
                try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                System.out.println("Retrying database operation (attempt " + retryCount + ")");
            }
        }

        throw new RuntimeException("Failed to execute database operation after " + maxRetries + " retries");
    }
    
}