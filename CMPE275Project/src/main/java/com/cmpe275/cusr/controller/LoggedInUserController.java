package com.cmpe275.cusr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.cmpe275.cusr.model.UserBean;
import com.cmpe275.cusr.repositories.UserRepository;

@Controller
public class LoggedInUserController {

	@Autowired
	private UserRepository userRepository;

	@ModelAttribute("loggedInUser")
	public void secureUser(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserBean user = userRepository.findByEmail(auth.getName());
		model.addAttribute("loggedInUser", user);
	}

	@GetMapping("/secure/user")
	public String securePage() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(auth.getName());
		if("admin@gmail.com".equals(auth.getName()))
		{
			return "secure/testing";
		}
		else
		{
			return "secure/user";
		}
	}

}