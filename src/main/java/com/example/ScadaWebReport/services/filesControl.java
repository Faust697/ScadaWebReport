package com.example.ScadaWebReport.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class filesControl {
	
	private static String generateFileName() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateStr = now.format(formatter);
		return "SCADA_WELL_" + dateStr + ".xlsx";
	}
}
