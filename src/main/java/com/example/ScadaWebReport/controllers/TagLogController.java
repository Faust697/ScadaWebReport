package com.example.ScadaWebReport.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.ScadaWebReport.Model.Taglog.Taglog;
import com.example.ScadaWebReport.repos.TaglogRepo;
import com.example.ScadaWebReport.repos.TaglogRepositoryImpl;
import com.example.ScadaWebReport.services.TagLogWithName;
import com.example.ScadaWebReport.services.jsonParsers;
import com.example.ScadaWebReport.services.dataProcessingService;

@Controller
public class TagLogController {

	
	private final TaglogRepo taglogRepo;

	private final dataProcessingService dps;

	@Autowired
	public TagLogController(TaglogRepo taglogRepo, TaglogRepositoryImpl taglogRepositoryImpl, jsonParsers jsonParser,
			dataProcessingService dataProcessingService) {
		this.taglogRepo = taglogRepo;
		this.dps = dataProcessingService;
	}

	// Онлайн данные

	@GetMapping("/")

	public String getTagLogs(@RequestParam(defaultValue = "0") int page, Model model) {
		List<TagLogWithName> tagLogsWithNames = dps.getTagLogsWithNames("online", "", true);

		dps.formatDataValues(tagLogsWithNames, true, true);
		dps.setTagLogPageInModel(tagLogsWithNames, page, model, "Anlıq sərfiyyat", 2);
		return "tag-log-list2";
	}

	// Суммарные данные
	@GetMapping("/tag-logs-all")
	public String getTagLogsSum(@RequestParam(defaultValue = "0") int page, Model model) {
		List<TagLogWithName> tagLogsWithNames = dps.getTagLogsWithNames("total",
				"AND data_value IS NOT NULL AND data_value > 0", false);
		dps.setTagLogPageInModel(tagLogsWithNames, page, model, "Toplam sərfiyyat", 1);
		return "tag-log-list";
	}

	// Уровень воды
	@GetMapping("/tag-logs-lvl")
	public String getTagLogsLevel(@RequestParam(defaultValue = "0") int page, Model model) {
		List<TagLogWithName> tagLogsWithNames = dps.getTagLogsWithNames("level", "", false);
		dps.formatDataValues(tagLogsWithNames, true, false);
		dps.setTagLogPageInModel(tagLogsWithNames, page, model, "Kanalda su səviyyəsi", 3);
		return "tag-log-list";
	}
	
	
	
	
	// Уровень воды
		@PostMapping("/tag-logs-lvl/{tag_id}")
		public String getTagInfoByDate(@RequestParam(defaultValue = "0") int page, Model model) {
			List<TagLogWithName> tagLogsWithNames = dps.getTagLogsWithNames("tagLibraryLevel.json", "", false);
			dps.formatDataValues(tagLogsWithNames, true, false);
			dps.setTagLogPageInModel(tagLogsWithNames, page, model, "Kanalda su səviyyəsi", 3);
			return "tag-log-list";
		}
	
		
		
	
	

	@GetMapping("/first")
	public String getFirstTagLog(Model model) {
	
		return "dateTest";
	}

	
	  @PostMapping("/your-endpoint")
	    public ResponseEntity<String> handlePostRequest(
	    		@RequestParam("dateRangePicker") String dateRangePicker,
	            @RequestParam("tagLogId") String tagLogId
	    		) {
		  
		    String[] parts = dateRangePicker.split(" - ");
	        
	        if (parts.length == 2) {
	            String startDateString = parts[0];
	            String endDateString = parts[1];
	            
	            // Создайте объекты SimpleDateFormat для парсинга даты и времени
	            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	            
	            try {
	                // Парсинг начальной и конечной даты
	                Date startDate = dateFormat.parse(startDateString);
	                Date endDate = dateFormat.parse(endDateString);
	                 
	                Float lastValue =taglogRepo.findFirstByTagIdAndLogdateBetweenOrderByLogdateDesc(tagLogId,"DESC",
	                		startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), 
	                		endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).getData_value();
	                     
	               // System.out.println("---" );
	                Float firstValue =  taglogRepo.findFirstByTagIdAndLogdateBetweenOrderByLogdateDesc(tagLogId,"ASC",
	                		startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), 
	                		endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).getData_value() ;
	                
	               // System.out.println("Начальная дата: " + startDate+" "+firstValue);
	              // System.out.println("Конечная дата: " + endDate+" "+lastValue);
	              //  System.out.println(lastValue-firstValue);
	               // Вместо использования model.addAttribute, создайте строку с результатом
					Float result = lastValue - firstValue;
					String resultString = String.valueOf(result);
					
					return ResponseEntity.ok(resultString);
	            } catch (ParseException e) {
	                e.printStackTrace();
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка обработки запроса");
	            }
	        }
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка обработки запроса");
	       
	   
	       
	    }

}
