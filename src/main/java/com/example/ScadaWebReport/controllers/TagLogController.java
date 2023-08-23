package com.example.ScadaWebReport.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.example.ScadaWebReport.domain.Taglog;
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

	@GetMapping("/tag-logs")

	public String getTagLogs(@RequestParam(defaultValue = "0") int page, Model model) {
		List<TagLogWithName> tagLogsWithNames = dps.getTagLogsWithNames("tagLibrary.json", "", true);

		dps.formatDataValues(tagLogsWithNames, true, true);
		dps.setTagLogPageInModel(tagLogsWithNames, page, model, "Anlıq sərfiyyat", 2);
		return "tag-log-list2";
	}

	// Суммарные данные
	@GetMapping("/tag-logs-all")
	public String getTagLogsSum(@RequestParam(defaultValue = "0") int page, Model model) {
		List<TagLogWithName> tagLogsWithNames = dps.getTagLogsWithNames("tagLibrarySum.json",
				"AND data_value IS NOT NULL AND data_value > 0", false);
		dps.setTagLogPageInModel(tagLogsWithNames, page, model, "Toplam sərfiyyat", 1);
		return "tag-log-list";
	}

	// Уровень воды
	@GetMapping("/tag-logs-lvl")
	public String getTagLogsLevel(@RequestParam(defaultValue = "0") int page, Model model) {
		List<TagLogWithName> tagLogsWithNames = dps.getTagLogsWithNames("tagLibraryLevel.json", "", false);
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
	
		
		
		
		@PostMapping("/greeting")
		public String addPerson(@ModelAttribute("person") @Valid TagLogWithName o, BindingResult bindingResult, Model model) {

			if (bindingResult.hasErrors()) {
				return "greeting";
			}

			

			return "greeting";
		}

		
	

	@GetMapping("/first-tag-log")
	public String getFirstTagLog(Model model) {
		Taglog firstTagLog = null;
		try {
			firstTagLog = taglogRepo.findFirstByOrderByTaglogIdAsc();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.print(firstTagLog.getFormattedLogdate());
		model.addAttribute("firstTagLog", firstTagLog);
		return "first-tag-log";
	}


}
