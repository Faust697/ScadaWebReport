package com.example.ScadaWebReport.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class WellMonitoringService {
	private volatile boolean stopLoop = false;

	    private final dataProcessingService dps;

	    private final ExecutorService executorService;


	    @Autowired
	    public WellMonitoringService(dataProcessingService dataProcessingService) {
	        this.dps = dataProcessingService;
	        this.executorService = Executors.newSingleThreadExecutor();
	    }

	    public void startAsyncLoop() {
	        executorService.submit(() -> {
	            while (!stopLoop) {
	               /* try {
	                    List<Well> wells = dps.getWells(null);

	                    // Создайте экземпляр сервиса
	                    WellsLoggingService loggingService = new WellsLoggingService((ArrayList<Well>) wells);

	                    // Запустите процесс логирования
	                    loggingService.Logg();

	                    Thread.sleep(300000); // Например, подождите 1 секунду
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                    logErrorDetails(e);
	                } catch (IOException e) {
	                    e.printStackTrace(); // Обработка IOException
	                    logErrorDetails(e);
	                }*/
	            }
	        });
	    }
	    
	    public void stopAsyncLoop() {
	        stopLoop = true;
	        executorService.shutdownNow(); // Принудительное завершение потока при остановке
	    }
    
    
    private void logErrorDetails(Exception e) {
        // Вывод информации о причине и времени ошибки
        System.err.println("Ошибка: " + e.getMessage());
        System.err.println("Время ошибки: " + System.currentTimeMillis());
        
    }
    
}
