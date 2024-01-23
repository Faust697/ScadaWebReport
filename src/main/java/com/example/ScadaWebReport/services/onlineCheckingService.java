package com.example.ScadaWebReport.services;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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
	private void getWellsForNotification()
	{
		 List<TelegramUserModel> usersToNotify =  tur.findAllByNotifyAndVerified(true, true);
		 if(usersToNotify.size()!=0)
		 {
			 for(TelegramUserModel user: usersToNotify)
			 { 
				 System.out.println(user.getName()); 
				 botInitializer.getBot().sendTextMessage(user.getChatId(), messageTgBuilder());
	 
			 }
		 }
		

	}
	
	 
	 
	 private String messageTgBuilder()
	 {
		 List<Well> wells =  dps.getWellsForNotification(null);
			
		  Iterator<Well> iterator = wells.iterator();

	        while (iterator.hasNext()) {
	        	Well well = iterator.next();
	            if (!well.getTotalFlow().equals("")) {
	                iterator.remove();
	            }
	        }
	        
	        String message = "----------------------------- \n";
	        
	       
		for(Well well:wells)
		{
			message = message + "Warning! Well "+well.getName()+" is offline now! \n";
			
		}
		message = message +"-----------------------------\n";
		
		return message;
		 
	 }
	 
	 
	 
	 
	 
	

}
