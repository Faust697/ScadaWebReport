package com.example.ScadaWebReport.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.ScadaWebReport.services.WellMonitoringService;

@Component
public class WellMonitoringComponent {
    private final WellMonitoringService wellMonitoringService;

    @Autowired
    public WellMonitoringComponent(WellMonitoringService wellMonitoringService) {
        this.wellMonitoringService = wellMonitoringService;
    }

    public void startAsyncLoop() {
        wellMonitoringService.startAsyncLoop();
    }

    public void stopAsyncLoop() {
        wellMonitoringService.stopAsyncLoop();
    }
}
