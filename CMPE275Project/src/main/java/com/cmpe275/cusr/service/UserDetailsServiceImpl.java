package com.cmpe275.cusr.service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmpe275.cusr.model.UserBean;
import com.cmpe275.cusr.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	//implementing the mandatory read only method of UserDetailsService
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

	  UserBean user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Email : " + email+" not registered");
		}
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		
		grantedAuthorities.add(new SimpleGrantedAuthority("LOGGED_USER"));
		
		return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
	}

}


