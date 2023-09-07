package com.example.ScadaWebReport.repos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ScadaWebReport.Model.staticInfo.StaticInfoModel;

@Service
public class StaticInfoRepositoryImpl {

	@Autowired
	private final StaticInfoRepo repository;

	@Autowired
	public StaticInfoRepositoryImpl(StaticInfoRepo repository) {
		this.repository = repository;
	}

	
	public List<StaticInfoModel> getAllChannelData() {
		return repository.findAll();
	}

	public StaticInfoModel saveChannelData(StaticInfoModel channelData) {
		return repository.save(channelData);
	}


}