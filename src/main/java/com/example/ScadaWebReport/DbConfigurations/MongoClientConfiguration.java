package com.example.ScadaWebReport.DbConfigurations;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.ScadaWebReport.repos") // Укажите пакет для ваших репозиториев
public class MongoClientConfiguration extends AbstractMongoClientConfiguration {

	
	@Value("${spring.data.mongodb.database}")
	private String databaseName;
	
	@Value("${spring.second-datasource.url}")
	private String connectionLink;
	
	@Value("${spring.data.mongodb.username}")
	private String mongoUser;
	
	@Value("${spring.data.mongodb.password}")
	private String mongoPass;
	

	
    @Override
    protected String getDatabaseName() {
        return databaseName; // Название базы данных
    }

    /*@Override
    public MongoClient mongoClient() {
    	
    	 ConnectionString connectionString = new ConnectionString(connectionLink);
         
         MongoClientSettings.Builder builder = MongoClientSettings.builder()
             .applyConnectionString(connectionString);
         
         // Добавить настройки Change Streams, если необходимо
         builder.readPreference(com.mongodb.ReadPreference.primaryPreferred());
         
         return MongoClients.create(builder.build());
     
    }*/
    
    @Override
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(connectionLink);
        
        MongoClientSettings.Builder builder = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .credential(MongoCredential.createCredential(mongoUser, databaseName, mongoPass.toCharArray()));
        
        // Добавить настройки Change Streams, если необходимо
        builder.readPreference(com.mongodb.ReadPreference.primaryPreferred());
        
        return MongoClients.create(builder.build());
    }
    
    
 
}