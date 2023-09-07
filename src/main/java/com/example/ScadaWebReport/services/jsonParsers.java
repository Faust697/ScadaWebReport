package com.example.ScadaWebReport.services;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Component;

import com.example.ScadaWebReport.Model.TagInfo;

@Component
public class jsonParsers {
	
	
	
	// Загрузка имён из джейсона
	public Map<String, String> loadTagNamesFromJson(String name) {
	    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name);
	    if (inputStream != null) {
	        JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
	        Map<String, String> tagNames = new HashMap<>();
	        
	        for (String key : jsonObject.keySet()) {
	            if (jsonObject.get(key) instanceof String) {
	                tagNames.put(key, jsonObject.getString(key));
	            } else {
	                // Handle other types as needed
	            }
	        }
	        
	        return tagNames;
	    }

	    return Collections.emptyMap();
	}
	
	
	public Map<String, Integer> loadTagLevelIdFromJson(String name) {
	    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name);
	    if (inputStream != null) {
	        JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
	        Map<String, Integer> tagNames = new HashMap<>();
	        
	        for (String key : jsonObject.keySet()) {
	            tagNames.put(key, jsonObject.getInt(key));
	        }
	        
	        return tagNames;
	    }

	    return Collections.emptyMap();
	}

	
	public List<TagInfo> loadTagInfoFromJson(String jsonName) {
	    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(jsonName);
	    if (inputStream != null) {
	        JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
	        List<TagInfo> tagInfoList = new ArrayList<>();
	        for (String id : jsonObject.keySet()) {
	            tagInfoList.add(new TagInfo(id, jsonObject.getString(id)));
	        }
	        return tagInfoList;
	    }

	    return Collections.emptyList();
	}
	
	
	 public String formatCurrentMonth() {
	        LocalDate currentDate = LocalDate.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
	        return currentDate.format(formatter);
	    }


}
