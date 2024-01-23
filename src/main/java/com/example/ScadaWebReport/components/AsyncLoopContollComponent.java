package com.example.ScadaWebReport.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ScadaWebReport.components.WellMonitoringService;

@Component
public class AsyncLoopContollComponent {
    private final WellMonitoringService wellMonitoringService;

    @Autowired
    public AsyncLoopContollComponent(WellMonitoringService wellMonitoringService) {
        this.wellMonitoringService = wellMonitoringService;
    }

    public void startAsyncLoop() {
      //  wellMonitoringService.startAsyncLoop();
    }

 /*   public void stopAsyncLoop() {
        wellMonitoringService.stopAsyncLoop();
    }*/
}
