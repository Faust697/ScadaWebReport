package com.example.ScadaWebReport.Entity.Mongo;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
public class UserModel {

    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String Id;

    @NotNull
    private String username;

    @NotNull
    private String pass;

    private String email;

    private Set<Role> roles = new HashSet<>();

    public UserModel() {
    }

    public UserModel(String id, String username, String pass, String email, Set<Role> roles) {
        this.Id = id;
        this.username = username;
        this.pass = pass;
        this.email = email;
        this.roles = roles;
    }

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return authorities;
    }
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}

    public void setPass(String pass) {
        // Хешируем пароль перед сохранением
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.pass = passwordEncoder.encode(pass);
    }
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Set<Role> getRoles() {
	    return roles;
	}

	public void setRoles(Set<Role> roles) {
	    this.roles = roles;
	}
	
}
