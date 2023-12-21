package com.example.ScadaWebReport.services;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.ScadaWebReport.Entity.Mongo.Role;
import com.example.ScadaWebReport.Entity.Mongo.UserModel;
import com.example.ScadaWebReport.repos.UserRepo;

/**
 * @author ERahimov
 *
 */
@Service
public class UserVerificationService {
	
	private UserRepo userRepo;
	
	public UserVerificationService(
			UserRepo userRepo
			)
	{
		this.userRepo = userRepo;
	}
	
	public Boolean checkForAdmin() {

		if (getCurrentUser().getRoles().contains(Role.ADMIN))
			return true;
		else
			return false;

	}
	
	public Boolean checkForDataEditor()
	{
		if (getCurrentUser().getRoles().contains(Role.DATA_EDITOR))
			return true;
		else
			return false;
		
	}
	
	public Boolean checkForWells()
	{
		if (getCurrentUser().getRoles().contains(Role.SUBARTEZIAN))
			return true;
		else
			return false;
		
	}
	
	public Boolean checkForCanals()
	{
		if (getCurrentUser().getRoles().contains(Role.CANALS))
			return true;
		else
			return false;	
	}
	
	public UserModel getCurrentUser()
	{
		return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
	}
	
	public String getCurrentUserName()
	{
		return SecurityContextHolder.getContext().getAuthentication().getName();
		
	}

}
