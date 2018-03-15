package com.cmpe275.cusr.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	CustomLogoutSuccessHandler customLogoutSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/css/**", "/connect/**").permitAll()
		.antMatchers("/secure/**")
		.authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/secure/user")
		.failureUrl("/login-error").permitAll().
		and().logout().logoutSuccessHandler(customLogoutSuccessHandler).permitAll();
	}

}
