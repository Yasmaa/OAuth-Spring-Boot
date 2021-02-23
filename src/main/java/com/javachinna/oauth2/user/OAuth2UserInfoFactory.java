package com.javachinna.oauth2.user;

import java.util.Map;

import com.javachinna.dto.SocialProvider;
import com.javachinna.exception.OAuthAuthenticationException;

public class OAuth2UserInfoFactory {
	public static OAuthUserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
		if (registrationId.equalsIgnoreCase(SocialProvider.GOOGLE.getProviderType())) {
			return new GoogleUserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase(SocialProvider.FACEBOOK.getProviderType())) {
			return new FacebookUserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase(SocialProvider.GITHUB.getProviderType())) {
			return new GithubUserInfo(attributes);
		} else {
			throw new OAuthAuthenticationException("Sorry! Login with " + registrationId + " is not supported yet.");
		}
	}
}