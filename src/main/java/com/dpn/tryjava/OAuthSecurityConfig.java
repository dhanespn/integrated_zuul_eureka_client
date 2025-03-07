package com.dpn.tryjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configurable
@EnableWebSecurity
// Modifying or overriding the default spring boot security.
public class OAuthSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	OAuth2ClientContext oauth2ClientContext;
	@Autowired
	AuthorizationCodeResourceDetails authorizationCodeResourceDetails;
	@Autowired
	ResourceServerProperties resourceServerProperties;

	// This method is for overriding the default AuthenticationManagerBuilder.
	// We can specify how the user details are kept in the application. It may
	// be in a database, LDAP or in memory.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
	}

	// This method is for overriding some configuration of the WebSecurity
	// If you want to ignore some request or request patterns then you can
	// specify that inside this method
	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

	private OAuth2ClientAuthenticationProcessingFilter filter() {
		OAuth2ClientAuthenticationProcessingFilter oAuth2Filter = new OAuth2ClientAuthenticationProcessingFilter(
					"/google/login");
		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(authorizationCodeResourceDetails,
				oauth2ClientContext);
		oAuth2Filter.setRestTemplate(oAuth2RestTemplate);
		oAuth2Filter.setTokenServices(new UserInfoTokenServices(resourceServerProperties.getUserInfoUri(),
				resourceServerProperties.getClientId()));
		return oAuth2Filter;
	}

	// This method is used for override HttpSecurity of the web Application.
	// We can specify our authorization criteria inside this method.
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				// starts authorizing configurations
				.authorizeRequests()
				// ignore the "/" and "/index.html"
				.antMatchers("/", "/index.html", "/app/app.js", "/css/**").permitAll()
				// authenticate all remaining URLS
				.anyRequest().fullyAuthenticated()//
				.and()//
				.logout()//
				.logoutSuccessUrl("/")//
				.permitAll()//
				.and()//
				.addFilterAt(filter(), BasicAuthenticationFilter.class)//
				.csrf()//
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

}
