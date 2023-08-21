package com.example.ScadaWebReport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.ScadaWebReport.repos.TaglogRepo;
import com.example.ScadaWebReport.repos.TaglogRepositoryImpl;

@Configuration
public class RepositoryConfig {

	public TaglogRepo taglogRepo() {
	    return new TaglogRepositoryImpl();
	}
}
   