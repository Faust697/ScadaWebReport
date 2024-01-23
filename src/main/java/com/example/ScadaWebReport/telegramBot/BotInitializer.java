package com.example.ScadaWebReport.telegramBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.example.ScadaWebReport.repos.TelegramUserRepo;

import lombok.Getter;


@Service
public class BotInitializer {

	@Value("${telegram.bot.token}")
	private String botToken;
   

	private final TelegramUserRepo tgUsersRepo;
	@Getter
	private Bot bot;
	
	
    @Autowired
    public BotInitializer(TelegramUserRepo tgUsersRepo) {
        super();
        this.tgUsersRepo = tgUsersRepo;
       
    }
    
	   public void startTelegramBot() {
	        try {
	            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
	            bot = new Bot(tgUsersRepo
	            		, botToken.replaceAll("\"", ""));
	            
	            telegramBotsApi.registerBot(bot);
	           
	            System.out.println("Telegram bot registered successfully!");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	   
	   public String getBotToken() {
			return botToken;
		}

		public void setBotToken(String botToken) {
			this.botToken = botToken;
		}
	
}
