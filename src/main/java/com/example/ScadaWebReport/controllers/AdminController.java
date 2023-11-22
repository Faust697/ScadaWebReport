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

import com.example.ScadaWebReport.Document.MongoDocument.Role;
import com.example.ScadaWebReport.Document.MongoDocument.UserModel;
import com.example.ScadaWebReport.repos.TaglogRepo;
import com.example.ScadaWebReport.repos.TaglogRepositoryImpl;
import com.example.ScadaWebReport.repos.UserRepo;
import com.example.ScadaWebReport.services.UserDetailsServiceImpl;
import com.example.ScadaWebReport.services.dataProcessingService;


@Controller
public class AdminController {

	
	private final dataProcessingService dps;
	private final UserDetailsServiceImpl uds;
	private final UserRepo userRepo;

	@Autowired
	public AdminController(TaglogRepo taglogRepo, TaglogRepositoryImpl taglogRepositoryImpl,
			dataProcessingService dataProcessingService, UserDetailsServiceImpl uds, UserRepo userRepo) {
		this.dps = dataProcessingService;
		this.uds = uds;
		this.userRepo  = userRepo;
	}
	
	
	
	@GetMapping("/users")

	public String getUsersList(@RequestParam(defaultValue = "0") int page, Model model, HttpServletRequest request) {
	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserModel user = userRepo.findByUsername(authentication.getName());
	    
	    List<UserModel> users = userRepo.findAll();
	    if(user.getRoles().contains(Role.ADMIN))
	    {

	    
	    // Добавим следующую строку для вывода ролей в консоль
	    System.out.println("User Roles: " + user.getRoles());
	    model.addAttribute("user", user);
	    model.addAttribute("users", users);
	    model.addAttribute("userRoles", user.getRoles());
	    model.addAttribute("totalVisitors", dps.totalVisitors());
	    model.addAttribute("weeklyVisitors", dps.totalWeekVisitors());

	    return "users-list";
	    }
	    else 
	    {
	    	return "redirect:/";
	    	
	    }
	    
	}
	
	
	
}
