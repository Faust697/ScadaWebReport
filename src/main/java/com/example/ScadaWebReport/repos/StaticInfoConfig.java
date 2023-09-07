package com.example.ScadaWebReport.repos;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.ScadaWebReport.repos") // Укажите пакет для ваших репозиториев
public class StaticInfoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "static_data"; // Название базы данных
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://192.168.7.62:27017"); // URI для вашей MongoDB
    }
}