package com.example.ScadaWebReport.repos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.ScadaWebReport.Document.MongoDocument.StaticInfoModel;
import com.example.ScadaWebReport.Document.MongoDocument.StaticInfoWellModel;

@Repository
public interface StaticInfoWellRepo extends MongoRepository<StaticInfoWellModel, String> {

	List<StaticInfoWellModel> findByCurrentFlowIdIn();

	List<StaticInfoWellModel> findAll();
}