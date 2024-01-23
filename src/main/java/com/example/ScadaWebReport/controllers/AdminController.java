package com.example.ScadaWebReport.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ScadaWebReport.Entity.Mongo.NotificationObjectModel;
import com.example.ScadaWebReport.Entity.Mongo.Role;
import com.example.ScadaWebReport.Entity.Mongo.StaticInfoWellModel;
import com.example.ScadaWebReport.Entity.Mongo.TelegramUserModel;
import com.example.ScadaWebReport.Entity.Mongo.UserModel;
import com.example.ScadaWebReport.Entity.Mongo.Well;
import com.example.ScadaWebReport.repos.NotificationObjectRepo;
import com.example.ScadaWebReport.repos.StaticInfoWellRepo;
import com.example.ScadaWebReport.repos.TaglogRepo;
import com.example.ScadaWebReport.repos.TaglogRepositoryImpl;
import com.example.ScadaWebReport.repos.TelegramUserRepo;
import com.example.ScadaWebReport.repos.UserRepo;
import com.example.ScadaWebReport.services.UserDetailsServiceImpl;
import com.example.ScadaWebReport.services.UserVerificationService;
import com.example.ScadaWebReport.services.dataProcessingService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AdminController {

	private final dataProcessingService dps;
	private final UserDetailsServiceImpl uds;
	private final UserRepo userRepo;
	private final TelegramUserRepo tgUserRepo;
	private final NotificationObjectRepo notificationObjectRepo;
	private final StaticInfoWellRepo staticInfoWellRepository;
	private final UserVerificationService userVerificationService;

	/*
	@Autowired
	public AdminController(TaglogRepo taglogRepo, 
			TaglogRepositoryImpl taglogRepositoryImpl,
			dataProcessingService dataProcessingService,
			UserDetailsServiceImpl uds,
			StaticInfoWellRepository staticInfoWellRepository,
			UserRepo userRepo,
			NotificationObjectRepo notificationObjectRepo,
			UserVerificationService userVerificationService,
			TelegramUserRepo tgUserRepo) {

		this.dps = dataProcessingService;
		this.uds = uds;
		this.userRepo = userRepo;
		this.userVerificationService = userVerificationService;
		this.tgUserRepo = tgUserRepo;
		this.notificationObjectRepo = notificationObjectRepo;
	}
*/
	@GetMapping("/users")
	public String getUsersList(@RequestParam(defaultValue = "0") int page, Model model, HttpServletRequest request) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserModel user = userRepo.findByUsername(authentication.getName());

		List<UserModel> users = userRepo.findAll();
		if (user.getRoles().contains(Role.ADMIN)) {

			
			// Добавим следующую строку для вывода ролей в консоль
			System.out.println("User Roles: " + user.getRoles());
			model.addAttribute("user", user);
			model.addAttribute("users", users);
			model.addAttribute("userRoles", user.getRoles());
			model.addAttribute("totalVisitors", dps.totalVisitors());
			model.addAttribute("weeklyVisitors", dps.totalWeekVisitors());

			return "users-list";
		} else {
			return "redirect:/";

		}

	}

	// Логининг
	@GetMapping("/login")
	public String login(Model model, HttpServletRequest request) {

		dps.UpdateVisitors(request.getRemoteAddr().toString());
		model.addAttribute("totalVisitors", dps.totalVisitors());
		model.addAttribute("weeklyVisitors", dps.totalWeekVisitors());

		return "login.html";
	}

	// Логаут
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/"; // Перенаправление на главную страницу после выхода
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/user-form")
	public String showAddUserForm(Model model, HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserModel activeUser = userRepo.findByUsername(authentication.getName());

		if (activeUser.getRoles().contains(Role.ADMIN)) {

			// Создаем объект UserModel и добавляем его в модель
			UserModel userModel = new UserModel();
			model.addAttribute("userModel", userModel);
			// Передаем все возможные роли в модель
			model.addAttribute("editMode", false);
			model.addAttribute("allRoles", Arrays.asList(Role.values()));

			return "user-editor"; // Название вашего Thymeleaf шаблона
		}

		else {
			model.addAttribute("message", "You don't have permissons!");
			return "upload-result";

		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/add-user")
	public String addUser(@ModelAttribute UserModel userModel, Model model, HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserModel activeUser = userRepo.findByUsername(authentication.getName());

		if (activeUser.getRoles().contains(Role.ADMIN)) {

			UserModel user;
			user = userRepo.findByUsername(userModel.getUsername());
			if (user == null) {
				user = new UserModel();
				user.setUsername(userModel.getUsername());
				user.setEmail(userModel.getEmail());
				user.setPass(userModel.getPass());
				user.setRoles(userModel.getRoles());

				userRepo.save(userModel);

				return "redirect:/users";
			}
			model.addAttribute("message", "User already exist!");
			return "upload-result";
		} else {
			model.addAttribute("message", "You don't have permissons!");
			return "upload-result";

		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/edit-user")
	public String editUser(@ModelAttribute UserModel userModel, Model model, HttpServletRequest request) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserModel activeUser = userRepo.findByUsername(authentication.getName());

		if (activeUser.getRoles().contains(Role.ADMIN)) {
			UserModel user = userRepo.findById(new ObjectId(userModel.getId()).toString()).get();

			user.setUsername(userModel.getUsername());
			user.setEmail(userModel.getEmail());

			if (userModel.getPass() != null) {
				user.setPass(userModel.getPass());
			}
			user.setRoles(userModel.getRoles());

			userRepo.save(userModel);
			return "redirect:/users";
		}

		else {
			model.addAttribute("message", "You don't have permissons!");
			return "upload-result";

		}

	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/remove_user")
	public String removeUser(@RequestParam String username, Model model, HttpServletRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserModel user = userRepo.findByUsername(authentication.getName());

		if (user.getRoles().contains(Role.ADMIN)) {

			userRepo.deleteByUsername(username);

		}

		return "redirect:/users";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/edit-user")
	public String showEditUserForm(@RequestParam String username, Model model, HttpServletRequest request) {
		// Создаем объект UserModel и добавляем его в модель
		UserModel userModel = new UserModel();
		userVerificationService.checkForAdmin();
		{
			userModel = userRepo.findByUsername(username);

			model.addAttribute("userModel", userModel);
			// Передаем все возможные роли в модель
			model.addAttribute("editMode", true);
			model.addAttribute("allRoles", Arrays.asList(Role.values()));
		}
		return "user-editor"; // Название вашего Thymeleaf шаблона
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/tg-users")
	public String showEditTgUsersForm(Model model, HttpServletRequest request) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserModel user = userRepo.findByUsername(authentication.getName());

		List<TelegramUserModel> tgUsers = tgUserRepo.findAll();
		if (user.getRoles().contains(Role.ADMIN)) {


			model.addAttribute("tgUsers", tgUsers);
			model.addAttribute("totalVisitors", dps.totalVisitors());
			model.addAttribute("weeklyVisitors", dps.totalWeekVisitors());

			return "tg-users-list";
		} else {
			return "tg-users-list";

		}
		
	}	
	
	
	
	@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/updateUserStatus")
    public String updateUserStatus(@RequestParam String Id) {
        TelegramUserModel tgUser = tgUserRepo.findById(Id).get();
        tgUser.setVerified(!tgUser.isVerified());
        tgUserRepo.save(tgUser);
        
        return "redirect:/tg-users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/updateUserNotifyStatus")
    public String updateUserNotifyStatus(@RequestParam String Id) {
    	TelegramUserModel tgUser = tgUserRepo.findById(Id).get();
    	tgUser.setNotify(!tgUser.isNotify());
        tgUserRepo.save(tgUser);
    	
        return "redirect:/tg-users";
    }
		
    //Получаем лист объектов для уведомлений
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/notofications-list")
	public String getNotificationList( 
			@RequestParam(required = false) String scrollTop,
			Model model, 
			HttpServletRequest request) {
	
    	List <NotificationObjectModel> notificatonObjectsList = notificationObjectRepo.findAll();
		
	    model.addAttribute("totalVisitors", dps.totalVisitors());
	    model.addAttribute("weeklyVisitors", dps.totalWeekVisitors());
	    model.addAttribute("pagename", "Subartezian quyuları");
	    model.addAttribute("notificatonObjectsList", notificatonObjectsList);
	    model.addAttribute("scrollTop", scrollTop);
	    
	    
	    return "notification-list-well"; 
 
	}
    
    
    //Перезаполняем лист колодцев, если список обновился
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/recreate-notification-list")
	public String resetNotificationWellsList( Model model, HttpServletRequest request) {
	
    	
    	notificationObjectRepo.deleteAll();
		List<StaticInfoWellModel> wells = staticInfoWellRepository.findAll();
		
		for(StaticInfoWellModel well:wells)
		{
			NotificationObjectModel nom= new NotificationObjectModel();
			nom.setName(well.getName());
			nom.setRegion(well.getRegion());
			nom.setTotalFlowValue(null);
			nom.setWellId(String.valueOf(well.getTotalFlowId()));
			nom.setNotificationStatus(false);
			notificationObjectRepo.save(nom);
			
		}
		
System.out.println(notificationObjectRepo.findAll());
	    model.addAttribute("totalVisitors", dps.totalVisitors());
	    model.addAttribute("weeklyVisitors", dps.totalWeekVisitors());
	    model.addAttribute("pagename", "Subartezian quyuları");


	    return "redirect:/notofications-list";
 
	}
    
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/change-object-notify-status")
    public String changeObjectNotifyStatus(@RequestParam String Id,
    		  @RequestParam(required = false) String scrollTop,
    		RedirectAttributes redirectAttributes) {
    	NotificationObjectModel nom = notificationObjectRepo.findByWellId(Id).get();
    	nom.setNotificationStatus(!nom.isNotificationStatus());
    	notificationObjectRepo.save(nom);
    
     
    	System.out.println("TEST "+scrollTop);
    	 redirectAttributes.addAttribute("scrollTop", scrollTop);
    	
        return "redirect:/notofications-list";
    }

}
