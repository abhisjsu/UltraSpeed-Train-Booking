package com.cmpe275.cusr.security;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;


@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{

	@Override
	public void onLogoutSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		arg1.setStatus(HttpServletResponse.SC_OK);
		arg1.sendRedirect("/logout");
	}
	
}

/*Reference : http://javasampleapproach.com/spring-framework/spring-security/spring-security-customize-logout-handler*/