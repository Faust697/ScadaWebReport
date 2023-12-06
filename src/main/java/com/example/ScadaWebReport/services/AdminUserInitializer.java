package com.example.ScadaWebReport.services;
//AdminUserInitializer.java
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.ScadaWebReport.Document.MongoDocument.Role;
import com.example.ScadaWebReport.Document.MongoDocument.UserModel;
import com.example.ScadaWebReport.repos.UserRepo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class AdminUserInitializer implements ApplicationListener<ContextRefreshedEvent> {

 @Autowired
 private UserRepo userRepository;
 @Autowired
 private PasswordEncoder passwordEncoder;

 @Override
 public void onApplicationEvent(ContextRefreshedEvent event) {
     // Проверяем наличие пользователя администратора
     if (!userRepository.existsByUsername("admin")) {
         // Если администратора нет, создаем его
         UserModel admin = new UserModel();
         admin.setUsername("admin");
         // Здесь вы хешировуем пароль
         admin.setPass(passwordEncoder.encode("david2023!"));
         
         //Раздаём роли
         Set<Role> roles = new HashSet<>();
         roles.add(Role.ADMIN);
         roles.add(Role.CANALS);
         roles.add(Role.DATA_EDITOR);
         roles.add(Role.SUBARTEZIAN);
         admin.setRoles(roles);
         

         userRepository.save(admin);
     }
 }
}
