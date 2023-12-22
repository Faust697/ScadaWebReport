package com.example.ScadaWebReport.repos;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ScadaWebReport.Entity.Mongo.UserModel;


public interface UserRepo extends MongoRepository<UserModel, String> {
	
	

	List<UserModel> findAll();
	
	UserModel findByUsername(String username);
	Optional<UserModel> findById(ObjectId objectId);

	boolean existsByUsername(String Username);

	void deleteByUsername(String username);
}
