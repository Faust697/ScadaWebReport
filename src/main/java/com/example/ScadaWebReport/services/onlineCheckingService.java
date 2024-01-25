package com.example.ScadaWebReport.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
						System.out.println(user.getName());
						botInitializer.getBot().sendTextMessage(user.getChatId(), message);
					}
				}
			}
		}

	}
	
	 
	 private String messageTgBuilder(String region)
	 {
		 List<Well> wells =  dps.getWellsForNotification(null);
		 if(!region.equals("Admin"))
		 {
			 wells = wells.stream()
               .filter(well -> region.equals(well.getRegion()))
               .collect(Collectors.toList());
		 }
			 
		 
		  String message = "";	
		 
		 if(!wells.isEmpty())
		 { 
		  Iterator<Well> iterator = wells.iterator();

	        while (iterator.hasNext()) {
	        	Well well = iterator.next();
	            if (!well.getTotalFlow().equals("")) {
	                iterator.remove();
	            }
	        }
	        
	        message = message +"-----------------------------\n";
		for(Well well:wells)
		{
			message = message + "Warning! Well "+well.getName()+" is offline now! \n"; 	
		}
		message = message +"-----------------------------\n"+getCurrentTime()+"\n";
		
		return message;
		 
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
