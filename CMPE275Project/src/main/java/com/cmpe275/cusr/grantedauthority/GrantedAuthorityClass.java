package com.cmpe275.cusr.grantedauthority;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cmpe275.cusr.model.UserBean;

@Configuration
public class GrantedAuthorityClass {
	public void 
	setSecuritycontext
	(UserBean userForm) 
	{
		Set<GrantedAuthority> grantedAuthorities = 
						new HashSet<>();
		
		grantedAuthorities.add
		(new SimpleGrantedAuthority(userForm.getProvider().toUpperCase()));
		
		Authentication authentication = 
				new UsernamePasswordAuthenticationToken(userForm.getEmail(),
				userForm.getPassword(), grantedAuthorities);
		
		SecurityContextHolder.getContext().
		setAuthentication(authentication);
	}
	

}
