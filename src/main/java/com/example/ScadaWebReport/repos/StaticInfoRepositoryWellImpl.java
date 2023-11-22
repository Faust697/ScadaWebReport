package com.example.ScadaWebReport.repos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.ScadaWebReport.Document.MongoDocument.StaticInfoWellModel;

@Service
public class StaticInfoRepositoryWellImpl {

	@Autowired
	private final StaticInfoWellRepo repository;

	@Autowired
	public StaticInfoRepositoryWellImpl(StaticInfoWellRepo repository) {
		this.repository = repository;
	}

	
	public List<StaticInfoWellModel> getAllWellData() {
		return repository.findAll();
	}

	public StaticInfoWellModel saveWellData(StaticInfoWellModel WellData) {
		return repository.save(WellData);
	}


}