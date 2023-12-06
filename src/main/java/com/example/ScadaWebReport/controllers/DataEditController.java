package com.example.ScadaWebReport.controllers;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.ScadaWebReport.Document.MongoDocument.StaticInfoModel;
import com.example.ScadaWebReport.Document.MongoDocument.StaticInfoWellModel;
import com.example.ScadaWebReport.repos.StaticInfoRepo;
import com.example.ScadaWebReport.repos.StaticInfoWellRepo;
import com.example.ScadaWebReport.services.ExcelService;
import com.example.ScadaWebReport.services.RoleChecker;
import com.example.ScadaWebReport.services.UserDetailsServiceImpl;
import com.example.ScadaWebReport.services.dataProcessingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DataEditController {
	
	private static final Logger log = LoggerFactory.getLogger(DataEditController.class);
    private final ExcelService excelService;
    private final StaticInfoRepo staticInfoRepository;
    private final StaticInfoWellRepo staticInfoWellRepository;
    private final dataProcessingService dps;
    private final RoleChecker roleChecker;//ввести это в эксплуотацию
    
    private final UserDetailsServiceImpl uds;

    @Autowired
    public DataEditController( ExcelService excelService, 
    		StaticInfoRepo staticInfoRepository, 
    		StaticInfoWellRepo staticInfoWellRepository,
    		dataProcessingService dataProcessingService,
    		RoleChecker roleChecker,
    		UserDetailsServiceImpl userDetailsService) {
        this.excelService = excelService;
        this.staticInfoRepository = staticInfoRepository;
        this.staticInfoWellRepository = staticInfoWellRepository;
        this.dps = dataProcessingService;
        this.roleChecker = roleChecker;
        this.uds = userDetailsService;
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
            log.info("Data was successfuly imported to MongoDB.");
            return "upload-result"; // Вернуть страницу результатов с успешным сообщением
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Произошла ошибка при импорте данных.");
           log.error("Error: "+e.getMessage());
            return "upload-result"; // Вернуть страницу результатов с сообщением об ошибке
        }
    }

    
    
    @PostMapping("/upload-well")
    public String uploadWellFile(@RequestParam("file") MultipartFile file, Model model) {
    
        try {
            if (file.isEmpty()) {
                model.addAttribute("message", "Выберите файл для загрузки.");
                return "upload-result"; // Вернуть страницу результатов с сообщением об ошибке
            }

            List<StaticInfoWellModel> staticInfoWellList = excelService.readExcelFileWell(file.getInputStream());

            staticInfoWellRepository.deleteAll();

            if (staticInfoWellList.isEmpty()) {
                model.addAttribute("message", "Список данных пустой. Нет данных для импорта.");
                return "upload-result"; // Вернуть страницу результатов с сообщением об ошибке
            }

            // Сохраните данные в MongoDB
            staticInfoWellRepository.saveAll(staticInfoWellList);

            model.addAttribute("message", "Данные успешно импортированы в MongoDB.");
            log.info("Data was successfuly imported to MongoDB.");
            return "upload-result"; // Вернуть страницу результатов с успешным сообщением
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Произошла ошибка при импорте данных.");
           log.error("Error: "+e.getMessage());
            return "upload-result"; // Вернуть страницу результатов с сообщением об ошибке
        }
    }
    
    
    //Получаем страницу загрузки данных
    @GetMapping("/get-upload")
    public String getUpload(Model model, HttpServletRequest request) {
    	
    	

    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    	 if (authentication != null) {
             System.out.println("Роли пользователя:");

             for (GrantedAuthority authority : authentication.getAuthorities()) {
                 System.out.println(authority.getAuthority());
             }
         } else {
             System.out.println("Пользователь не аутентифицирован");
         }
    	
    	 

         UserDetails userDetails = uds.loadUserByUsername(authentication.getName());

         // Передаем роли в модель
         model.addAttribute("userRoles", userDetails.getAuthorities());
      	dps.UpdateVisitors(request.getRemoteAddr().toString());
    		model.addAttribute("totalVisitors", dps.totalVisitors());
    		model.addAttribute("weeklyVisitors", dps.totalWeekVisitors());
    		
        return "data-upload.html"; 
    }
    
    
    
    //Получаем страницу загрузки данных
    @GetMapping("/get-upload-well")
    public String getUploadWell(Model model, HttpServletRequest request) {
    	
    	

    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    	 if (authentication != null) {
             System.out.println("Роли пользователя:");

             for (GrantedAuthority authority : authentication.getAuthorities()) {
                 System.out.println(authority.getAuthority());
             }
         } else {
             System.out.println("Пользователь не аутентифицирован");
         }
    	
    	 

         UserDetails userDetails = uds.loadUserByUsername(authentication.getName());

         // Передаем роли в модель
         model.addAttribute("userRoles", userDetails.getAuthorities());
      	dps.UpdateVisitors(request.getRemoteAddr().toString());
    		model.addAttribute("totalVisitors", dps.totalVisitors());
    		model.addAttribute("weeklyVisitors", dps.totalWeekVisitors());
    		
        return "data-upload-well.html"; 
    }
    
    
    
    
    //Скачиваем файл с графиком калибровки
    @GetMapping(
    		  value = "/get-pdf/{id}",
    		  produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    		)
    		public ResponseEntity getFile(@PathVariable("id") String id, Model model) throws IOException {
    	
    	
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
    	        model.addAttribute("fileExists", true);
    	        return ResponseEntity.ok()
    	                .headers(headers)
    	                .contentLength(classPathResource.contentLength())
    	                .contentType(MediaType.parseMediaType("application/pdf"))
    	                .body(resource);
    	    } else {
    	        // Возвращаем сообщение о том, что файл не найден
    	    	 model.addAttribute("fileExists", false);
    	    	 log.error("Error: File not found.");
    	        return ResponseEntity.badRequest().body("Üzr istəyirik, lakin seçilmiş fayl hələ sistemə əlavə olunmayıb.");
    	        		//notFound().build();
    	    }
    		   
    		}
    
    
    
    //Логининг
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
    	
    	dps.UpdateVisitors(request.getRemoteAddr().toString());
		model.addAttribute("totalVisitors", dps.totalVisitors());
		model.addAttribute("weeklyVisitors", dps.totalWeekVisitors());
    	
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
