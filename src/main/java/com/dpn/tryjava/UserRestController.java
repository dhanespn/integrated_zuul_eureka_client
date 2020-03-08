package com.dpn.tryjava;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@RequestMapping("/user")
	public Principal sayHello(Principal principal) {
		return principal;
	}

	@RequestMapping("/token")
	public String getToken(){
		Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
		//return auth.getPrincipal().toString();
		String token = ((OAuth2AuthenticationDetails) auth.getDetails()).getTokenValue();
		return token;
	}
	


	@RequestMapping({ "/hello" })
	public String firstPage() {
		return "Hello World";
	}


	
}
