package com.example.ScadaWebReport.controllers;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.ScadaWebReport.Entity.Mongo.StaticInfoModel;
import com.example.ScadaWebReport.Entity.Mongo.StaticInfoWellModel;
import com.example.ScadaWebReport.repos.StaticInfoRepo;
import com.example.ScadaWebReport.repos.StaticInfoWellRepo;
import com.example.ScadaWebReport.services.ExcelService;
import com.example.ScadaWebReport.services.RoleChecker;
import com.example.ScadaWebReport.services.UserDetailsServiceImpl;
import com.example.ScadaWebReport.services.dataProcessingService;
import com.example.ScadaWebReport.services.filesControl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Controller
public class DataEditController {
	

    private final ExcelService excelService;
    private final StaticInfoRepo staticInfoRepository;
    private final StaticInfoWellRepo staticInfoWellRepository;
    private final dataProcessingService dps;
    private final UserDetailsServiceImpl uds;
    private final filesControl filesControll;

 
 

    
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
    
  /*  
	@PostMapping("/upload-pdf")
	public String uploadSchedule(@RequestParam("file") MultipartFile file, Model model) throws IOException {

		try {

			if (file.isEmpty()) {
				model.addAttribute("message", "Выберите файл для загрузки.");
				return "upload-result"; // Вернуть страницу результатов с сообщением об ошибке
			}

			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	        Path filePath = Paths.get("/out-monitoring-pdf", fileName);

			// Создание ClassPathResource для проверки существования папки
			ClassPathResource classPathResource = new ClassPathResource("monitoring-pdf");
		
			
			
			if (!(new ClassPathResource("out-monitoring-pdf").exists())) {
				Files.createDirectories(classPathResource.getFile().toPath());
			}

				file.transferTo(filePath);	
			
			model.addAttribute("message", "Данные успешно импортированы в MongoDB.");
			log.info("Data was successfuly imported to MongoDB.");
			return "upload-result"; // Вернуть страницу результатов с успешным сообщением
		}

		catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("message", "Произошла ошибка при импорте данных.");
			System.out.print(e.getMessage());
			log.error("Error: " + e.getMessage());
			return "upload-result"; // Вернуть страницу результатов с сообщением об ошибке
		}

	}
	
	*/
    
    
    
    
    @PostMapping("/upload-pdf")
    public String uploadSchedule(@RequestParam("file") MultipartFile file, Model model) throws IOException {

        try {
            if (file.isEmpty()) {
                model.addAttribute("message", "Выберите файл для загрузки.");
                return "upload-result"; // Вернуть страницу результатов с сообщением об ошибке
            }

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = Paths.get("out-monitoring-pdf", fileName);

            // Проверка существования директории и ее создание при необходимости
            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }

            // Проверка существования файла с таким же именем и его удаление
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            String uploadDirectory = System.getProperty("user.dir") + File.separator + "out-monitoring-pdf";
             filePath = Paths.get(uploadDirectory, fileName);
             
             System.out.println("Saved file to: "+filePath.toFile().toString());
            // Запись нового файла
            file.transferTo(filePath.toFile());

            model.addAttribute("message", "Данные успешно импортированы в MongoDB.");
            log.info("Data was successfully imported to MongoDB.");
            return "upload-result"; // Вернуть страницу результатов с успешным сообщением
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Произошла ошибка при импорте данных.");
            log.error("Error: " + e.getMessage());
            return "upload-result"; // Вернуть страницу результатов с сообщением об ошибке
        }
    }
	
    
	@GetMapping("/upload-schedule")
	public String getUploadSchedule(Model model) {
		if (uds.checkForAdmin() || uds.checkForDataEditor()) {
			return "data-upload-schedule";
		}

		else
			model.addAttribute("message", "No access.");
			return "upload-result";
	}
	
	
	
	
	@GetMapping(value = "/get-pdf/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity getFile(@PathVariable("id") String id, Model model) throws IOException {

	    Path outFilePath = Paths.get(System.getProperty("user.dir") + File.separator + "/out-monitoring-pdf/", id);

		ClassPathResource classPathResource = new ClassPathResource("monitoring-pdf/" + id);
		
		System.out.println(outFilePath);
		System.out.println(classPathResource.getPath());
		
	    // Проверяем сначала существование файла в новой папке
		if (Files.exists(outFilePath)) {

			return filesControll.getPdf(model, id, false);
	    	
		}
		 // Если нет, то ищем в ресурсах
	    else if (classPathResource.exists()) {

			return filesControll.getPdf(model, id, true);
		}
	    

	    else {
	        // Файл не найден ни в одной из папок, возвращаем соответствующий HTTP-ответ
	        model.addAttribute("fileExists", false);
	        log.error("Error: File not found.");
	        return ResponseEntity.badRequest().body("Üzr istəyirik, lakin seçilmiş fayl hələ sistemə əlavə olunmayıb.");
	    }
	}
    

  
    


}
