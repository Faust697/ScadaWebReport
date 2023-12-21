package com.example.ScadaWebReport.controllers;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ScadaWebReport.Entity.Mongo.Role;
import com.example.ScadaWebReport.Entity.Mongo.UserModel;
import com.example.ScadaWebReport.repos.TaglogRepo;
import com.example.ScadaWebReport.repos.TaglogRepositoryImpl;
import com.example.ScadaWebReport.repos.UserRepo;
import com.example.ScadaWebReport.services.UserDetailsServiceImpl;
import com.example.ScadaWebReport.services.UserVerificationService;
import com.example.ScadaWebReport.services.dataProcessingService;


@Controller
public class AdminController {

	
	private final dataProcessingService dps;
	private final UserDetailsServiceImpl uds;
	private final UserRepo userRepo;
	private final UserVerificationService userVerificationService;

	@Autowired
	public AdminController(TaglogRepo taglogRepo,
			TaglogRepositoryImpl taglogRepositoryImpl,
			dataProcessingService dataProcessingService,
			UserDetailsServiceImpl uds,
			UserRepo userRepo,
			UserVerificationService userVerificationService) {
		
		
		this.dps = dataProcessingService;
		this.uds = uds;
		this.userRepo  = userRepo;
		this.userVerificationService = userVerificationService;
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
	
	  //Логининг
    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
    	
    	dps.UpdateVisitors(request.getRemoteAddr().toString());
		model.addAttribute("totalVisitors", dps.totalVisitors());
		model.addAttribute("weeklyVisitors", dps.totalWeekVisitors());
    	
        return "login.html"; 
    }
    
    //Логаут
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/"; // Перенаправление на главную страницу после выхода
    }
    
    @GetMapping("/user-form")
    public String showAddUserForm(Model model,
    		HttpServletRequest request) {
        // Создаем объект UserModel и добавляем его в модель
        UserModel userModel = new UserModel();
        model.addAttribute("userModel", userModel);
        // Передаем все возможные роли в модель
        model.addAttribute("editMode", false);
        model.addAttribute("allRoles", Arrays.asList(Role.values()));

        return "user-editor"; // Название вашего Thymeleaf шаблона
    }
    
    @PostMapping("/add-user")
    public String addUser(@ModelAttribute UserModel userModel,
    		Model model,
    		HttpServletRequest request) {
        UserModel user = new UserModel();
    	user.setUsername(userModel.getUsername());
    	user.setEmail(userModel.getEmail());
    	user.setPass(userModel.getPass());
    	user.setRoles(userModel.getRoles());
    	
        userRepo.save(userModel); 

        return "redirect:/user-list";
    }
	
    @GetMapping("/remove_user")
    public String removeUser(@RequestParam String username, Model model, HttpServletRequest request)
    {   	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    UserModel user = userRepo.findByUsername(authentication.getName());

	    if(user.getRoles().contains(Role.ADMIN))
	    {
	
	    	userRepo.deleteByUsername(username);
	    	
	    }

    	return "redirect:/users";
    }
    
    
    @GetMapping("/edit-user")
    public String showEditUserForm(@RequestParam String username, Model model,
    		HttpServletRequest request) {
        // Создаем объект UserModel и добавляем его в модель
        UserModel userModel = new UserModel();
      userVerificationService.checkForAdmin();
      {
    	  userModel = userRepo.findByUsername(username);
    	  
    	  System.out.println("User roles: " + userModel.getRoles());
        model.addAttribute("userModel", userModel);
        // Передаем все возможные роли в модель
        model.addAttribute("editMode", true);
        model.addAttribute("allRoles", Arrays.asList(Role.values()));
      }
        return "user-editor"; // Название вашего Thymeleaf шаблона
    }
    
	
	
}
