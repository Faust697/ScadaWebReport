package com.example.ScadaWebReport.repos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.ScadaWebReport.Model.MongoModels.StaticInfoModel;

@Repository
public interface StaticInfoRepo extends MongoRepository<StaticInfoModel, String> {

	List<StaticInfoModel> findByOnlineIdIn();

	List<StaticInfoModel> findAll();
}