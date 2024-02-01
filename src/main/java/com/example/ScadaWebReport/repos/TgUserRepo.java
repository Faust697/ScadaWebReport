package com.example.ScadaWebReport.repos;

import java.util.List;
import java.util.Optional;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ScadaWebReport.Entity.Mongo.TelegramUserModel;
import com.example.ScadaWebReport.Entity.Mongo.TgUserModel;


//Тут будет версия пользовтаеля с листом регионов
public interface TgUserRepo extends MongoRepository<TgUserModel, String>{
	List <TelegramUserModel> findAllById(String Id);
	List <TelegramUserModel> findAllByVerified(boolean verified);
	List <TelegramUserModel> findAllByNotify(boolean notify);
	Optional<TelegramUserModel> findByChatId(String chatId);
	List <TelegramUserModel> findAllByNotifyAndVerified(boolean Notify, boolean Verified);
	
	


}
