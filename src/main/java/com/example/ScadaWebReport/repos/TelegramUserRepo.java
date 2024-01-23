package com.example.ScadaWebReport.repos;

import java.util.List;
import java.util.Optional;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ScadaWebReport.Entity.Mongo.TelegramUserModel;



public interface TelegramUserRepo extends MongoRepository<TelegramUserModel, String>{
	List <TelegramUserModel> findAllById(String Id);
	List <TelegramUserModel> findAllByVerified(boolean verified);
	List <TelegramUserModel> findAllByNotify(boolean notify);
	Optional<TelegramUserModel> findByChatId(String chatId);
	List <TelegramUserModel> findAllByNotifyAndVerified(boolean Notify, boolean Verified);
	
	


}
