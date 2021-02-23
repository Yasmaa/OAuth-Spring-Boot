package com.javachinna.config;

import java.util.Arrays;
import javax.sql.DataSource;

import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.client.RestTemplate;

import com.javachinna.model.Role;
import com.javachinna.oauth2.OAuthUserService;
import com.javachinna.oauth2.CustomOidcUserService;
import com.javachinna.oauth2.OAuthAccessTokenResponse;
import com.javachinna.handler.MyAuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] IGNORED_RESOURCE_LIST = new String[] { "/fonts/**", "/webjars/**", "/files/**", "/static/**", "/robots.txt" };

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private OAuthUserService customOAuth2UserService;

	@Autowired
	CustomOidcUserService customOidcUserService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {



		

		http.authorizeRequests().antMatchers("/", "/home").hasRole(Role.USER);
		// Pages do not require login
		http.authorizeRequests().antMatchers("/**").permitAll();

		// Form Login config
		http.authorizeRequests().and().formLogin()//
				// Submit URL of login page.
				// .loginProcessingUrl("/j_spring_security_check") // Submit URL
				.loginPage("/login")//
				//
				.usernameParameter("j_username")//
				.passwordParameter("j_password")
				.successHandler(new MyAuthenticationSuccessHandler())
				.failureUrl("/login?error=true");

	    http.authorizeRequests().and().oauth2Login().loginPage("/login").failureHandler(authenticationFailureHandler).defaultSuccessUrl("/home");

	    http.sessionManagement().invalidSessionUrl("/expiredSession").maximumSessions(1).expiredUrl("/expiredSession");

		http
      .rememberMe()
        .tokenRepository(persistentTokenRepository())
        .key("AppKey")
        .alwaysRemember(true)
        .rememberMeParameter("rm")
        .rememberMeCookieName("javasampleapproach-remember-me")
        .tokenValiditySeconds(20) //  20 seconds
    	;

		http.authorizeRequests().and().logout().logoutSuccessUrl("/signout").deleteCookies("JSESSIONID").logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
		
		http.csrf().disable();


		

		


	}

	// This bean is load the user specific data when form login is used.
	@Override
	public UserDetailsService userDetailsService() {
		return userDetailsService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(IGNORED_RESOURCE_LIST);
	}

	private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient() {
		OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
		tokenResponseHttpMessageConverter.setTokenResponseConverter(new OAuthAccessTokenResponse());
		RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
		tokenResponseClient.setRestOperations(restTemplate);
		return tokenResponseClient;
	}


	 @Autowired
	DataSource dataSource;
    
    @Bean(name = "persistentTokenRepository")
	public PersistentTokenRepository persistentTokenRepository() {
	JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
	tokenRepository.setDataSource(dataSource);
	return tokenRepository;
}
}
