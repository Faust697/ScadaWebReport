package com.example.ScadaWebReport.components;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.ScadaWebReport.Entity.Mongo.Well;
import com.example.ScadaWebReport.services.WellsLoggingService;
import com.example.ScadaWebReport.services.dataProcessingService;


@Service
public class WellMonitoringService {
	private volatile boolean stopLoop = false;

	    private final dataProcessingService dps;
	    private final ExecutorService executorService;
	    private WellsLoggingService wellsLoggingService;


	    @Autowired
	    public WellMonitoringService(dataProcessingService dataProcessingService,
	    		WellsLoggingService wellsLoggingService) {
	        this.dps = dataProcessingService;
	        this.executorService = Executors.newSingleThreadExecutor();
	        this.wellsLoggingService = wellsLoggingService;
	    }

	    @Scheduled(fixedRate = 300000)
	    public void startAsyncLoop() throws IOException {
	      //  executorService.submit(() -> {
	            //while (!stopLoop) {
	             // try {
	                    List<Well> wells = dps.getWells(null);
	                    wellsLoggingService.setWells(wells);
	              
	                    // Запустите процесс логирования
	                    wellsLoggingService.Logg();

	                  /*  Thread.sleep(300000); // Например, подождите 1 секунду
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                    logErrorDetails(e);
	                } catch (IOException e) {
	                    e.printStackTrace(); // Обработка IOException
	                    logErrorDetails(e);
	                }*/
	          //  }
	       // });
	    }
	    
	/*    public void stopAsyncLoop() {
	        stopLoop = true;
	        executorService.shutdownNow(); // Принудительное завершение потока при остановке
	    }
    */
    
    private void logErrorDetails(Exception e) {
        // Вывод информации о причине и времени ошибки
        System.err.println("Ошибка: " + e.getMessage());
        System.err.println("Время ошибки: " + System.currentTimeMillis());
        
    }
    
}
