package com.example.ScadaWebReport.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ScadaWebReport.Entity.Mongo.UserModel;


public interface UserRepo extends MongoRepository<UserModel, String> {
	
	

	List<UserModel> findAll();
	
	UserModel findByUsername(String username);

	boolean existsByUsername(String Username);
}