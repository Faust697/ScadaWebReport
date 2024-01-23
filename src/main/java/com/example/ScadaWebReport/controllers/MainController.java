package com.example.ScadaWebReport.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ScadaWebReport.Entity.Mongo.Role;
import com.example.ScadaWebReport.Entity.Mongo.UserModel;
import com.example.ScadaWebReport.Entity.Mongo.Well;
import com.example.ScadaWebReport.repos.TaglogRepo;
import com.example.ScadaWebReport.repos.TaglogRepositoryImpl;
import com.example.ScadaWebReport.repos.UserRepo;
import com.example.ScadaWebReport.services.UserDetailsServiceImpl;
import com.example.ScadaWebReport.services.dataProcessingService;

@Controller
public class MainController {
	



	private final dataProcessingService dps;
		private final UserDetailsServiceImpl uds;
		private final UserRepo userRepo;

		@Autowired
		public MainController(TaglogRepo taglogRepo, TaglogRepositoryImpl taglogRepositoryImpl,
				dataProcessingService dataProcessingService, UserDetailsServiceImpl uds, UserRepo userRepo) {
			this.dps = dataProcessingService;
			this.uds = uds;
			this.userRepo  = userRepo;
		}

		
		

		@GetMapping("/")

		public String getMainPage(@RequestParam(defaultValue = "0") int page, Model model, HttpServletRequest request) {
		
	//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//    UserModel user = userRepo.findByUsername(authentication.getName());
		    
		  
		    model.addAttribute("totalVisitors", dps.totalVisitors());
		    model.addAttribute("weeklyVisitors", dps.totalWeekVisitors());

		    return "main";
		}
		
		
	
		
		
		
	

}
