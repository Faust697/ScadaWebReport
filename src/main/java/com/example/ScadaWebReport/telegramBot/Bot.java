package com.example.ScadaWebReport.telegramBot;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.example.ScadaWebReport.Entity.Mongo.TelegramUserModel;
import com.example.ScadaWebReport.Entity.Mongo.TgUserModel;
import com.example.ScadaWebReport.components.RegionsTgUsers;
import com.example.ScadaWebReport.repos.TelegramUserRepo;
import com.example.ScadaWebReport.repos.TgUserRepo;



public class Bot extends TelegramLongPollingBot {


	private String botToken;
	private TelegramUserRepo tgUsers;
	private TgUserRepo tgUsersTest;


	
	    public Bot(TelegramUserRepo tgUsers,
	    		 String botToken
	    		) {	
	        super();
	        
	        this.tgUsers = tgUsers;
	        this.botToken = botToken;

	
	    }


    @Override
    public void onUpdateReceived(Update update) {
    	 if (update.hasMessage() && update.getMessage().hasText()) {
             Message receivedMessage = update.getMessage();
             String chatId = receivedMessage.getChatId().toString();

             // Получаем текст сообщения
             String receivedText = receivedMessage.getText();

				/*
				 * if(!tgUsersTest.findByChatId(chatId).isPresent() &&
				 * receivedMessage.getText()=="3333") { TgUserModel tgUserModel = new
				 * TgUserModel();
				 * 
				 * 
				 * ArrayList<String> regions = null;
				 * 
				 * regions.add(RegionsTgUsers.Deafault.toString());
				 * 
				 * // Вызов сеттера
				 * tgUserModel.setName(receivedMessage.getFrom().getUserName());
				 * tgUserModel.setNotify(false); tgUserModel.setVerified(false);
				 * tgUserModel.setChatId(chatId); tgUserModel.setRegion(regions);
				 * tgUsersTest.save(tgUserModel);
				 * 
				 * 
				 * 
				 * sendTextMessage(chatId,
				 * "Salam, siz yeni istifadəçi kimi qeydiyyatdan keçmisiniz. Obyektlərin statusu haqqında məlumat almaq üçün administratorun təsdiqini gözləyin."
				 * ); }
				 */
             
             if(!tgUsers.findByChatId(chatId).isPresent())
             {
            	   TelegramUserModel tgUserModel = new TelegramUserModel();
                   
            	   
            	  
                   // Вызов сеттера
                   tgUserModel.setName(receivedMessage.getFrom().getUserName());
                   tgUserModel.setNotify(false);
                   tgUserModel.setVerified(false);
                   tgUserModel.setChatId(chatId);
                   tgUserModel.setRegion((RegionsTgUsers.Deafault).toString());
                   tgUsers.save(tgUserModel);
                   
                   
                   
                   sendTextMessage(chatId, "Salam, siz yeni istifadəçi kimi qeydiyyatdan keçmisiniz. Obyektlərin statusu haqqında məlumat almaq üçün administratorun təsdiqini gözləyin."); 
             }		 
            	 
             else {
             sendTextMessage(chatId, "Siz artıq sistemdəsiniz.");
             }
           
             
         }
    }
    
    public void sendTextMessage(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "bot";
    }

    @Override
    public String getBotToken() {
       
        return botToken;
    }
}
