package com.example.ScadaWebReport.telegramBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import com.example.ScadaWebReport.Entity.Mongo.TelegramUserModel;
import com.example.ScadaWebReport.repos.TelegramUserRepo;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

@Service
public class Bot extends TelegramLongPollingBot {


	
	   @Autowired
	    private TelegramUserRepo tgUsers;

	
	    public Bot() {
	 
	    }

	    // Конструктор, используемый для внедрения зависимости
	    @Autowired
	    public Bot(TelegramUserRepo tgUsers) {
	        super();
	        this.tgUsers = tgUsers;
	
	    }



	private String botToken="6955741396:AAGNzDSHDakIKta55N1cUBI3_bU2D7VRkXg";



 

    @Override
    public void onUpdateReceived(Update update) {
    	 if (update.hasMessage() && update.getMessage().hasText()) {
             Message receivedMessage = update.getMessage();
             String chatId = receivedMessage.getChatId().toString();

             // Получаем текст сообщения
             String receivedText = receivedMessage.getText();

             if(!tgUsers.findByChatId(chatId).isPresent())
             {
            	   TelegramUserModel tgUserModel = new TelegramUserModel();
                   
            
            	  
                   // Вызов сеттера
                   tgUserModel.setName(receivedMessage.getFrom().getUserName());
                   tgUserModel.setNotify(false);
                   tgUserModel.setVerified(false);
                   tgUserModel.setChatId(chatId);
                   tgUsers.save(tgUserModel);
                   
                   sendTextMessage(chatId, "New user saved!"); 
             }		 
            	 
             else {
             sendTextMessage(chatId, "Hi, old user!");
             }
             System.out.println(tgUsers.findAll());
             
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
