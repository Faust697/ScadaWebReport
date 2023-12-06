package com.example.ScadaWebReport;

import com.example.ScadaWebReport.components.WellMonitoringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication(scanBasePackages = "com.example.ScadaWebReport")
@EntityScan("com.example.ScadaWebReport")
@EnableAsync
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class })
public class ScadaWebMonitoring {

	@Autowired
	private WellMonitoringComponent wellMonitoringComponent;

	public static void main(String[] args) {
		try {
			SpringApplication.run(ScadaWebMonitoring.class, args);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@PostConstruct
	public void init() {
		// Запустите асинхронный цикл после инициализации приложения
		try {
			wellMonitoringComponent.startAsyncLoop();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

		}
	}

	@PreDestroy
	public void stopMonitor() {

		try {
			wellMonitoringComponent.stopAsyncLoop();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

		}
	}
}
