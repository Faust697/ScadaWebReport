package com.example.ScadaWebReport.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import com.example.ScadaWebReport.repos.TaglogRepo;
import com.example.ScadaWebReport.repos.TaglogRepositoryImpl;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
@ComponentScan(basePackages = "com.example.ScadaWebReport")
public class RepositoryConfig {

	@Bean
	public TaglogRepo taglogRepo() {
	    return new TaglogRepositoryImpl();
	}
	


	  
}
   