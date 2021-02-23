package com.javachinna.exception;

import org.springframework.security.core.AuthenticationException;

public class UserExistAuthenticationException extends AuthenticationException {

   
	private static final long serialVersionUID = 5570981880007077317L;

	public UserExistAuthenticationException(final String msg) {
        super(msg);
    }

}
