package com.example.ScadaWebReport.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

import com.example.ScadaWebReport.Entity.Mongo.Role;
import com.example.ScadaWebReport.Entity.Mongo.UserModel;
import com.example.ScadaWebReport.repos.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return User.builder()
            .username(user.getUsername())
            .password(user.getPass())
            .authorities(getAuthorities(user))
            .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserModel user) {
        return user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
            .collect(Collectors.toSet());
    }
    
    
    public Boolean checkForAdmin() {
        UserModel currentUser = getCurrentUser();
        if (currentUser != null && currentUser.getRoles() != null && currentUser.getRoles().contains(Role.ADMIN)) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkForDataEditor() {
        UserModel currentUser = getCurrentUser();
        if (currentUser != null && currentUser.getRoles() != null && currentUser.getRoles().contains(Role.DATA_EDITOR)) {
            return true;
        } else {
            return false;
        }
    }
	
	public Boolean checkForWells()
	{
		 UserModel currentUser = getCurrentUser();
	        if (currentUser != null && currentUser.getRoles() != null && currentUser.getRoles().contains(Role.SUBARTEZIAN)) {
	            return true;
	        } else {
	            return false;
	        }
		
	}
	
	public Boolean checkForCanals()
	{
		 UserModel currentUser = getCurrentUser();
	        if (currentUser != null && currentUser.getRoles() != null && currentUser.getRoles().contains(Role.CANALS)) {
	            return true;
	        } else {
	            return false;
	        }
	}
	
	public UserModel getCurrentUser() {
	    String currentUserName = getCurrentUserName();
	    if (currentUserName != null) {
	        return userRepo.findByUsername(currentUserName);
	    } else {
	        return null;
	    }
	}
	
	public String getCurrentUserName()
	{
		return SecurityContextHolder.getContext().getAuthentication().getName();
		
	}
    
}