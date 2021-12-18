package skaro.coffey.cookbook.security;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import skaro.coffey.cookbook.security.token.TokenService;
import skaro.coffey.cookbook.security.token.TokenSession;
import skaro.coffey.cookbook.security.user.User;

@RestController
public class LoginController {
	public static final String LOGIN_ENDPOINT = "/login";
	
	private TokenService tokenService;
	private UserDetailsService userDetailsService;
	private AuthenticationManager authenticationManager;

	public LoginController(TokenService tokenService, UserDetailsService userDetailsService,
			AuthenticationManager authenticationManager) {
		this.tokenService = tokenService;
		this.userDetailsService = userDetailsService;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping(LOGIN_ENDPOINT)
	public ResponseEntity<TokenSession> createToken(@RequestBody User user, HttpServletResponse response) {
		try {
			authenticate(user);
			TokenSession token = createToken(user);
			storeTokenInCookie(token, response);
			return ResponseEntity.ok(token);
		} catch (DisabledException | BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} 
	}
	
	private void authenticate(User user) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		authenticationManager.authenticate(authenticationToken);
	}
	
	private TokenSession createToken(User request) throws DisabledException, BadCredentialsException {
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		return tokenService.generateToken(userDetails.getUsername());
	}
	
	private void storeTokenInCookie(TokenSession token, HttpServletResponse response) {
		Cookie sessionCookie = new Cookie("cookbook-session", token.getToken());
		sessionCookie.setMaxAge(getCookieAge(token));
		response.addCookie(sessionCookie);
	}
	
	private int getCookieAge(TokenSession token) {
		return (int)((token.getExpiration().getTime() - new Date().getTime()) / 1000);
	}

}
