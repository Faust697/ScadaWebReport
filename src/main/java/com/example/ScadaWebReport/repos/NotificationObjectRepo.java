package com.example.ScadaWebReport.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ScadaWebReport.Entity.Mongo.NotificationObjectModel;

public interface NotificationObjectRepo extends MongoRepository<NotificationObjectModel, String>{

	List <NotificationObjectModel> findAllById(String Id);
	//List <NotificationObjectModel> findAllByNotify(boolean notify);
	Optional<NotificationObjectModel> findById(String Id);
	
	
}
