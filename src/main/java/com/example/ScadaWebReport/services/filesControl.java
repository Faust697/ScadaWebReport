package com.example.ScadaWebReport.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class filesControl {
	
	private static String generateFileName() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateStr = now.format(formatter);
		return "SCADA_WELL_" + dateStr + ".xlsx";
	}
	
	
	// Вынести это в сервис
	public ResponseEntity getPdf(
			// ClassPathResource classPathResource1,
			Model model, String id, boolean inside) throws IOException {
		InputStream inputStream = null;
		InputStreamResource resource = null;
		long length = 10;

		if (inside) {
			ClassPathResource classPathResource = new ClassPathResource("monitoring-pdf/" + id);
			inputStream = classPathResource.getInputStream();
			resource = new InputStreamResource(inputStream);
			length = classPathResource.contentLength();
		}

		else {
			Path outFilePath = Paths.get(System.getProperty("user.dir") + File.separator + "out-monitoring-pdf", id);
			resource = new InputStreamResource(Files.newInputStream(outFilePath));
			length = Files.size(outFilePath);

		}
		// Устанавливаем заголовки
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + id);
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		// Возвращаем ResponseEntity с файлом
		headers.setContentDisposition(ContentDisposition.builder("inline").filename(id).build());
		model.addAttribute("fileExists", true);

		return ResponseEntity.ok().headers(headers).contentLength(length)
				.contentType(MediaType.parseMediaType("application/pdf")).body(resource);

	}
}
