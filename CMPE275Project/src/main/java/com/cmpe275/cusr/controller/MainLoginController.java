package com.cmpe275.cusr.controller;

import javax.inject.Inject;
/*
 * Reference
 * http://www.littlebigextra.com/part-1-authorising-user-using-spring-social-google-facebook-and-linkedin-and-spring-security/
 */
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.plus.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmpe275.cusr.grantedauthority.GrantedAuthorityClass;
import com.cmpe275.cusr.model.UserBean;
import com.cmpe275.cusr.repositories.UserRepository;


@Controller
public class MainLoginController {
	
	private Facebook facebook;

	private Google google;

	private ConnectionRepository connectionRepository;
	
	@Autowired
	private GrantedAuthorityClass grantedAuthority;

	@Autowired
	private UserRepository userRepository;
	
	@Inject
	public MainLoginController(Facebook facebook, Google google, ConnectionRepository connectionRepository) {
		this.facebook = facebook;
		this.connectionRepository = connectionRepository;
		this.google = google;
	}

	@RequestMapping(value = "/facebook", method = RequestMethod.GET)
	public String loginToFacebook(Model model) {
		return this.getFacebookUserData(model, new UserBean());
	}

	@RequestMapping(value = "/google", method = RequestMethod.GET)
	public String loginToGoogle(Model model) {
		return this.getGoogleUserData(model, new UserBean());
	}

	@RequestMapping(value =  {"/login"} )
	public String login() {
		return "login";
	}

	@GetMapping("/registration")
	public String showRegistration(UserBean userBean) {
		System.out.println("You are in the registration page GET request mapping");
		return "registration";
	}
	
	
	@PostMapping("/registration")
	public String registerUser(HttpServletResponse httpServletResponse, Model model, @Valid UserBean userBean,
			BindingResult bindingResult) {
		System.out.println("You are in the registration page POST request mapping");
		if (bindingResult.hasErrors()) {
			return "registration";
		}
		userBean.setProvider("REGISTRATION");
		this.userRepository.save(userBean);
		this.grantedAuthority.setSecuritycontext(userBean);

		model.addAttribute("loggedInUser", userBean);
		return "login";
	}
	
	@GetMapping("/logout")
	public String logoutPage(UserBean userBean) {
		System.out.println("You are Logging Out");
		return "logOut";
	}
	
	

	//User name or Password not matched
	@RequestMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}

	
	public void setUsersSecurityContext(UserBean userBean) {
		this.grantedAuthority.setSecuritycontext(userBean);
	}
	
	public String getFacebookUserData(Model model, UserBean userBean) {

		ConnectionRepository connectionRepository = this.connectionRepository;
		if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
			return "redirect:/login";
		}
		// Populate the Bean
		populateUserDetailsFromFacebook(userBean);
		//userRepository.save(userBean);
		setUsersSecurityContext(userBean);
		model.addAttribute("loggedInUser", userBean);
		return "secure/user";
	}

	protected void populateUserDetailsFromFacebook(UserBean userForm) {
		Facebook facebook = this.facebook;
		
		String [] fields = { "email",  "first_name", "last_name" };
		
		//Getting the required information of the user through facebook class
		User user = facebook.fetchObject("me", User.class, fields);
		
		userForm.setProvider("facebook");
		userForm.setEmail(user.getEmail());
		userForm.setFirstName(user.getFirstName());
		userForm.setLastName(user.getLastName());

	}
	
	public String getGoogleUserData(Model model, UserBean userBean) {

		/*if the secured connection to Google is not made then 
		redirect again to the login page */
		ConnectionRepository connectionRepository = this.connectionRepository;
		if (connectionRepository.
					findPrimaryConnection(Google.class) == null) {
			return "redirect:/login";
		}

		populateUserDetailsFromGoogle(userBean);
		setUsersSecurityContext(userBean);

		model.addAttribute("loggedInUser", userBean);
		return "secure/user";
	}

	protected void populateUserDetailsFromGoogle(UserBean userBean) {
		Google google = this.google;
		Person googleUser = google.plusOperations().getGoogleProfile();
		userBean.setEmail(googleUser.getAccountEmail());
		userBean.setFirstName(googleUser.getGivenName());
		userBean.setLastName(googleUser.getFamilyName());
		//this.userRepository.save(userBean);
		userBean.setProvider("google");
	}
	
}