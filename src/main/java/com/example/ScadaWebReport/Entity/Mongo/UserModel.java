package com.example.ScadaWebReport.Entity.Mongo;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
public class UserModel {

    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int Id;

    @NotNull
    private String username;

    @NotNull
    private String pass;

    private String email;

    private Set<Role> roles = new HashSet<>();

    public UserModel() {
    }

    public UserModel(int id, String username, String pass, String email, Set<Role> roles) {
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
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
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
		this.pass = pass;
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
