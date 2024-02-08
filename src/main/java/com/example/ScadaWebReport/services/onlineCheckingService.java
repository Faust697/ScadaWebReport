package com.example.ScadaWebReport.services;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Iterator;

import com.example.ScadaWebReport.Entity.Mongo.TelegramUserModel;
import com.example.ScadaWebReport.Entity.Mongo.Well;
import com.example.ScadaWebReport.repos.TelegramUserRepo;
import com.example.ScadaWebReport.telegramBot.BotInitializer;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class onlineCheckingService {
	
	
	private final TelegramUserRepo tur;
	private final dataProcessingService dps;
	private final BotInitializer botInitializer;
	
	@Scheduled(fixedRate = 300000)
	private void getWellsForNotification() {
		String message;
		List<TelegramUserModel> usersToNotify = tur.findAllByNotifyAndVerified(true, true);

		if (usersToNotify.size() != 0) {
			for (TelegramUserModel user : usersToNotify) {
				message = messageTgBuilder(user.getRegion());
				if (message != null) {
					if (!message.equals("")) {
						
					
					botInitializer.getBot().sendTextMessage(user.getChatId(), message);
					}
				}
			}
		}

	}
	
	 private String messageTgBuilder(String region)
	 {
		 try {
		 List<Well> wells =  dps.getWellsForNotification(null);
		   
		 
		 
		 if(!wells.isEmpty())
		 { 
		  Iterator<Well> iterator = wells.iterator();

	        while (iterator.hasNext()) {
	        	Well well = iterator.next();
	            if (!well.getTotalFlow().equals("")) {
	                iterator.remove();
	            }
	        }
		 
	        wells = wells.stream()
	        	    .peek(w -> w.setRegion(w.getRegion().trim())) // Удаляем пробелы из регионов
	        	    .sorted(Comparator.comparing(Well::getRegion)) // Сортируем по регионам
	        	    .collect(Collectors.toList()); // Собираем обратно в список
	        
	        
	        
	       

	       
	        
		 if(!region.equals("Admin"))
		 {
			 wells = wells.stream()
               .filter(well -> region.equals(well.getRegion()))
               .collect(Collectors.toList());
		 }
			 
		 
		  String message = "";	
		 
		
	        if(wells.size()==0)
	        {
	        	return null;
	        }
	        message = message +"-----------------------------\n";
	        
	        
	      /*  Map<String, Long> regionCounts = wells.stream()
	        	    .map(w -> {
	        	        String trimmedRegion = w.getRegion().trim();
	        	        w.setRegion(trimmedRegion);
	        	        return trimmedRegion;
	        	    })
	        	    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

	     // Сортировка по ключам (регионам) перед выводом
	        //  Map<String, Long> sortedRegionCounts = new TreeMap<>(regionCounts);
	        //   sortedRegionCounts.forEach((regionName, count) -> System.out.println(regionName + ": " + count));*/

		for(Well well:wells)
		{
			message = message + "Warning! Well "+well.getName()+" is offline now! ( "+well.getRegion()+" ) \n"; 	
		}
		message = message +"-----------------------------\n"+getCurrentTime()+"\n";
		
		message = message+"Total number of offline wells is "+wells.size()+".";
		return message;
		 
	 }
		 }
		 catch(Exception e)
		 {
			 return "Error while receiving data";
			 
		 }
		return null;
	 
	 }
	 
	 
	 private String getCurrentTime()
	 {

	        // Define the format for the string representation
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	        // Format the current date and time as a string
	        return LocalDateTime.now().format(formatter);
		 
	 }
	 
	 
	

}
