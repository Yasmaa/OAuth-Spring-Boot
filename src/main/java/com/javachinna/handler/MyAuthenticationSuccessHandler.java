package com.javachinna.handler;


import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.userdetails.UserDetailsService;



public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException 
	{
        
        request.getSession(false).setMaxInactiveInterval(120);

        
		//Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//UserDetailsService user = new UserDetailsService();
		//user.setUserName(((User)principal).getUsername());
		
		//request.getSession(false).setAttribute("loggedInUser", user);
		response.sendRedirect(request.getContextPath());
    }
}