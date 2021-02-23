package com.javachinna.service;

import java.util.Map;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import com.javachinna.dto.LocalUser;
import com.javachinna.dto.UserRegistrationForm;
import com.javachinna.exception.UserExistAuthenticationException;
import com.javachinna.model.User;


public interface UserService {

	public User registerNewUser(UserRegistrationForm UserRegistrationForm) throws UserExistAuthenticationException;

	User findUserByEmail(String email);

	LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo);
}
