package com.example.ScadaWebReport.controllers;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ScadaWebReport.domain.TagInfo;
import com.example.ScadaWebReport.domain.Taglog;
import com.example.ScadaWebReport.repos.TaglogRepo;
import com.example.ScadaWebReport.repos.TaglogRepositoryImpl;
import com.example.ScadaWebReport.services.TagLogWithName;
import com.example.ScadaWebReport.services.jsonParsers;

@Controller
public class TagLogController {

	private final TaglogRepo taglogRepo;
	private TaglogRepositoryImpl taglogRepositoryImpl;
	private final jsonParsers jsonParser;

	@Autowired
	public TagLogController(TaglogRepo taglogRepo, TaglogRepositoryImpl taglogRepositoryImpl, jsonParsers jsonParser) {
		this.taglogRepo = taglogRepo;
		this.taglogRepositoryImpl = taglogRepositoryImpl;
		this.jsonParser = jsonParser;
	}

	//Онлайн данные
	
	  @GetMapping("/tag-logs")
	 
	public String getTagLogs(@RequestParam(defaultValue = "0") int page, Model model) {
		List<TagLogWithName> tagLogsWithNames = getTagLogsWithNames("tagLibrary.json", "", true);
		
		
	
		
		
		formatDataValues(tagLogsWithNames, true, true);
		setTagLogPageInModel(tagLogsWithNames, page, model, "Anlıq sərfiyyat", 2);
		return "tag-log-list";
	}
	
	
	

	
	//Суммарные данные
	@GetMapping("/tag-logs-all")
	public String getTagLogsSum(@RequestParam(defaultValue = "0") int page, Model model) {
		List<TagLogWithName> tagLogsWithNames = getTagLogsWithNames("tagLibrarySum.json",
				"AND data_value IS NOT NULL AND data_value > 0", false);
		setTagLogPageInModel(tagLogsWithNames, page, model, "Toplam sərfiyyat", 1);
		return "tag-log-list";
	}

	//Уровень воды
	@GetMapping("/tag-logs-lvl")
	public String getTagLogsLevel(@RequestParam(defaultValue = "0") int page, Model model) {
		List<TagLogWithName> tagLogsWithNames = getTagLogsWithNames("tagLibraryLevel.json", "", false);
		formatDataValues(tagLogsWithNames, true, false);
		setTagLogPageInModel(tagLogsWithNames, page, model, "Kanalda su səviyyəsi", 3);
		return "tag-log-list";
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

	// ___________________________________Всё, что ниже надо бы потом вынести в сервисы

	
	
	private List<Taglog> getLatestLogsForTags(String jsonFileName, List<String> tagIds, String additionalFilter) {
		// Загрузите список тегов из JSON
	    List<TagInfo> tagInfoList = jsonParser.loadTagInfoFromJson(jsonFileName);

	    // Создайте список ID тегов
	    List<String> filteredTagIds = tagIds.stream()
	            .filter(tagId -> tagInfoList.stream().anyMatch(tagInfo -> tagInfo.getId().equals(tagId)))
	            .collect(Collectors.toList());

	    // Преобразуйте список ID тегов в строку с разделителями
	    String tagIdString = String.join(",", filteredTagIds);

	    // Получите последние записи логов для каждого тега(за последний месяц)
	    List<Taglog> latestLogs = taglogRepositoryImpl.findLatestLogForEachTag(
	            "\"tag_log_" + jsonParser.formatCurrentMonth() + "\"", tagIdString, additionalFilter);

	    // Если в latestLogs меньше строк данных, чем в tagIds, выполните дополнительный запрос для недостающих тегов
	    if (latestLogs.size() < tagIds.size()) {
	        List<String> missingTagIds = tagIds.stream()
	                .filter(tagId -> !latestLogs.stream().anyMatch(taglog -> taglog.getTag_id().toString().equals(tagId)))
	                .collect(Collectors.toList());
	
	        
			/*
			 * if (!missingTagIds.isEmpty()) { String missingTagIdString = String.join(",",
			 * missingTagIds); List<Taglog> additionalLogs =
			 * taglogRepositoryImpl.findLatestLogForEachTag( "\"tag_log\"",
			 * missingTagIdString, additionalFilter);
			 * 
			 * 
			 * 
			 * latestLogs.addAll(additionalLogs); }
			 */
	    }
	    return latestLogs;
	}
	
	
	
	
	//Работа с джейсонами, составление списков и прочее
	
	private List<TagLogWithName> getTagLogsWithNames(String jsonFileName, String additionalFilter, boolean Level) {
		
		
		Map<String, Integer> tagToLevelMap=null;
		List<TagLogWithName> tagLogsLevel = null;
		Map<String, String> tagTextMap = null;
		Map<String, String> tagCalibrationMap = null;
		Map<String, String> tagReasonsMap = null;
		
		//Получаем данные из джейсона
		List<String> tagIds = jsonParser.loadTagInfoFromJson(jsonFileName).stream().map(TagInfo::getId)
				.collect(Collectors.toList());

		List<Taglog> latestLogs = getLatestLogsForTags(jsonFileName, tagIds, additionalFilter);
		Map<String, String> tagNamesMap = jsonParser.loadTagNamesFromJson(jsonFileName);	
		
	
		tagTextMap = jsonParser.loadTagNamesFromJson("tagPercent.json");
		tagCalibrationMap = jsonParser.loadTagNamesFromJson("tagCalibration.json");
		tagReasonsMap = jsonParser.loadTagNamesFromJson("tagReasons.json");
		

		String tagLevel=null;
		//Если вдруг это вызов для запроса онлайн, то нужно отобразить ещё и уровень воды.
		if(Level) {
				tagToLevelMap = jsonParser.loadTagLevelIdFromJson("tagToLevelLibrary.json");
				tagLogsLevel = getTagLogsWithNames("tagLibraryLevel.json", "", false);		
		}
			
		
		
		List<TagLogWithName> tagLogsWithNames = new ArrayList<>();
		for (Taglog taglog : latestLogs) {
			String tagName = tagNamesMap.get(taglog.getTag_id().toString());
			String tagText = tagTextMap.get(taglog.getTag_id().toString());
			String tagStatus = tagCalibrationMap.get(taglog.getTag_id().toString());
			String tagReason = tagReasonsMap.get(taglog.getTag_id().toString());
			
			
			if(Level) {
			tagLevel = getLevelTagValue(taglog.getTag_id().toString(), tagLogsLevel, tagToLevelMap);
			}
			
		
			
			TagLogWithName tagLogWithName = new TagLogWithName(taglog, tagName, tagText, tagLevel, tagStatus, tagReason );
			tagLogsWithNames.add(tagLogWithName);
			
		}

		return tagLogsWithNames;
	}

	
	//	Отправка всего нужного на модель
	private void setTagLogPageInModel(List<TagLogWithName> tagLogsWithNames, int page, Model model, String pagename,
			int measurement) {
		int size = 1000;
		Pageable pageable = PageRequest.of(page, size);
		Page<TagLogWithName> tagLogPage = new PageImpl<>(tagLogsWithNames, pageable, tagLogsWithNames.size());
		model.addAttribute("tagLogPage", tagLogPage);
		model.addAttribute("pagename", pagename);
		model.addAttribute("measurement", measurement);
	}
	
	
	//Форматирование чисел результатов(слишком длинные)
	private void formatDataValues(List<TagLogWithName> tagLogsWithNames, boolean forValue, boolean forLevel) {
		for (TagLogWithName tagLogWithName : tagLogsWithNames) {
			if (tagLogWithName.getData_value() != null && forValue) {
				Float dataValue = Math.round(tagLogWithName.getData_value() * 100.0f) / 100.0f;
				tagLogWithName.setData_value(dataValue);
				
			}
			
			if (tagLogWithName.getTagLevel() != null && forLevel &&  tagLogWithName.getTagLevel() != "") {			
				Float dataValueLvl = Math.round(Float.parseFloat(tagLogWithName.getTagLevel()) * 10.0f) / 10.0f;	
				tagLogWithName.setTagLevel(dataValueLvl.toString());
			}
		}
	}
	
	
	//Проклятый метод, для приравнивания айдишников по проходимости и уровню воды
	private String getLevelTagValue(String mainId, List<TagLogWithName> tagLogsLevel, Map<String, Integer> tagToLevelMap) {
		

			
		String levelTagId = tagToLevelMap.get(mainId).toString(); // Получаем айди из мэпа
		
		Optional<TagLogWithName> foundTagLog = tagLogsLevel.stream()
		        .filter(tagLogWithName -> tagLogWithName.getTag_id().toString().equals(levelTagId))
		        .findFirst();

		String Level = foundTagLog.map(tagLogWithName -> {
		    Float dataValue = tagLogWithName.getData_value();
		    return dataValue != null ? dataValue.toString() : "";
		}).orElse("");
	    
	 
		
		return Level;
	
	}
	

}
