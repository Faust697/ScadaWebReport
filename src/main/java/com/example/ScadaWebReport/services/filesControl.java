package com.example.ScadaWebReport.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
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
	
	
	   //Вынести это в сервис
    public ResponseEntity getPdf(ClassPathResource classPathResource, Model model, String id) throws IOException
    {
   
    	InputStream inputStream = classPathResource.getInputStream();

        // Устанавливаем заголовки
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + classPathResource.getFilename());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        // Создаем InputStreamResource
        InputStreamResource resource = new InputStreamResource(inputStream);

        // Возвращаем ResponseEntity с файлом
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(id + ".pdf").build());
        model.addAttribute("fileExists", true);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(classPathResource.contentLength())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    	
    }
}
