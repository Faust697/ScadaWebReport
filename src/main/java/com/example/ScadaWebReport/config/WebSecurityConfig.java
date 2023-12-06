package com.example.ScadaWebReport.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	 private final UserDetailsService userDetailsService;

	    @Autowired
	    public WebSecurityConfig(UserDetailsService userDetailsService) {
	        this.userDetailsService = userDetailsService;
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    	
	    	PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	    	System.out.print(pe.toString());
	    	
	        auth.userDetailsService(userDetailsService)
	            .passwordEncoder(passwordEncoder());
	    }

	    
	    /*@Override
	    public void configure(WebSecurity web) throws Exception {
	        web
	            .ignoring()
	            .antMatchers("/", "/login", "/img/**", "/scripts/**", "/**");
	    }
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	            .csrf().disable() // Отключаем CSRF защиту
	            .authorizeRequests()
	            .antMatchers("/", "/login", "*.css", "*.js", "*.jpg", "*.png").permitAll()
	            .anyRequest().authenticated()
	            .and()
	            .formLogin()
	                .loginPage("/login")
	                .defaultSuccessUrl("/", true)
	                .permitAll()
	            .and()
	            .logout()
	                .logoutUrl("/logout") // URL, по которому будет выполняться выход
	                .logoutSuccessUrl("/")
	                .invalidateHttpSession(true)
	                .deleteCookies("JSESSIONID"); // Удалить куки (если используются)
	    }
	    
*/
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	        	.csrf().disable() // Отключаем CSRF защиту
	            .authorizeRequests()
	                .antMatchers("/get-upload","/upload", "/wells","/get-upload-well", "/users").authenticated()
	                .anyRequest().permitAll()
	                .and()
	            .formLogin()
	                .loginPage("/login")
	                .defaultSuccessUrl("/get-upload", true)
	                .permitAll()
	                .and()
	                .logout()
	                .logoutUrl("/logout") // URL, по которому будет выполняться выход
	                .logoutSuccessUrl("/")
	                .invalidateHttpSession(true)
	                .deleteCookies("JSESSIONID");// Удалить куки (если используются)
	    }

	    
	    
  /*  @Bean
    @Override
    public UserDetailsService userDetailsService() {
	    //example of test user
        UserDetails user = User.withUsername("Dazad")
            .password(passwordEncoder().encode("2023scada2023"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(user);
    }*/
    
  
    
}


	
	

