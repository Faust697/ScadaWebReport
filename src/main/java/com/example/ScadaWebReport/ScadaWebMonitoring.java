package com.example.ScadaWebReport;

import com.example.ScadaWebReport.components.AsyncLoopContollComponent;
import com.example.ScadaWebReport.repos.TelegramUserRepo;
import com.example.ScadaWebReport.repos.UserRepo;
import com.example.ScadaWebReport.telegramBot.Bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringBootApplication(scanBasePackages = "com.example.ScadaWebReport")
@EntityScan("com.example.ScadaWebReport")
@EnableAsync
@EnableAutoConfiguration(exclude = { MongoAutoConfiguration.class })
public class ScadaWebMonitoring {

    private final UserRepo userRepo;
    private final TelegramUserRepo tgUsersRepo;

    @Autowired
    private AsyncLoopContollComponent asyncLoopContollComponent;

    public ScadaWebMonitoring(UserRepo userRepo, TelegramUserRepo tgUsersRepo) {
        super();
        this.userRepo = userRepo;
        this.tgUsersRepo = tgUsersRepo;
    }

    public static void main(String[] args) {
        try {
            SpringApplication.run(ScadaWebMonitoring.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        // Запустите асинхронный цикл и бота после инициализации приложения
        try {
            asyncLoopContollComponent.startAsyncLoop();
            startTelegramBot(tgUsersRepo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stopMonitor() {
        try {
            asyncLoopContollComponent.stopAsyncLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTelegramBot(TelegramUserRepo tgUsersRepo) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot(tgUsersRepo));
            System.out.println("Telegram bot registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
