package com.example.ScadaWebReport.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.jboss.jandex.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.ScadaWebReport.Model.staticInfo.StaticInfoModel;
import com.example.ScadaWebReport.repos.StaticInfoRepo;
import com.example.ScadaWebReport.services.ExcelService;


@Controller
public class DataEditController {
	

    private final ExcelService excelService;
    private final StaticInfoRepo staticInfoRepository;

    @Autowired
    public DataEditController( ExcelService excelService, StaticInfoRepo staticInfoRepository) {
        this.excelService = excelService;
        this.staticInfoRepository = staticInfoRepository;
    }
    
 

    
    //Загрузка данных в монгу из экзеля
    
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
    
        try {
            if (file.isEmpty()) {
                model.addAttribute("message", "Выберите файл для загрузки.");
                return "upload-result"; // Вернуть страницу результатов с сообщением об ошибке
            }

            List<StaticInfoModel> staticInfoList = excelService.readExcelFile(file.getInputStream());

            staticInfoRepository.deleteAll();

            if (staticInfoList.isEmpty()) {
                model.addAttribute("message", "Список данных пустой. Нет данных для импорта.");
                return "upload-result"; // Вернуть страницу результатов с сообщением об ошибке
            }

            // Сохраните данные в MongoDB
            staticInfoRepository.saveAll(staticInfoList);

            model.addAttribute("message", "Данные успешно импортированы в MongoDB.");
            return "upload-result"; // Вернуть страницу результатов с успешным сообщением
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Произошла ошибка при импорте данных.");
            return "upload-result"; // Вернуть страницу результатов с сообщением об ошибке
        }
    }

    
    
    @GetMapping("/get-upload")
    public String getUpload(Model model) {
    	
        return "data-upload.html"; 
    }
    
    
    @GetMapping(
    		  value = "/get-pdf/{id}",
    		  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    		)
    		public ResponseEntity<Resource> getFile(@PathVariable("id") String id) throws IOException {
    	
    	
    	// Путь к файлу в папке ресурсов
    	  String resourcePath = "monitoring-pdf/" + id;

    	    // Получаем InputStream из файла в ресурсах
    	    ClassPathResource classPathResource = new ClassPathResource(resourcePath);

    	    // Проверяем существование файла
    	    if (classPathResource.exists()) {
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

    	        return ResponseEntity.ok()
    	                .headers(headers)
    	                .contentLength(classPathResource.contentLength())
    	                .contentType(MediaType.parseMediaType("application/pdf"))
    	                .body(resource);
    	    } else {
    	        // Возвращаем сообщение о том, что файл не найден
    	        return ResponseEntity.notFound().build();
    	    }
    		   
    		}
    
    
    
    //Логининг
    @GetMapping("/login")
    public String login() {
        return "login.html"; 
    }
    
    //Логаут
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/"; // Перенаправление на главную страницу после выхода
    }
    


}
