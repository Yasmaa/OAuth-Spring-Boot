package com.javachinna.exception;

import org.springframework.security.core.AuthenticationException;

public class OAuthAuthenticationException extends AuthenticationException {
	
	private static final long serialVersionUID = 3392450042101522832L;

	public OAuthAuthenticationException(String msg, Throwable t) {
		super(msg, t);
	}

	public OAuthAuthenticationException(String msg) {
		super(msg);
	}
}