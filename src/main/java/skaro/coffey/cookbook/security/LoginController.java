package skaro.coffey.cookbook.security;

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

import skaro.coffey.cookbook.security.token.TokenSession;
import skaro.coffey.cookbook.security.token.TokenService;
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
	public ResponseEntity<TokenSession> createToken(@RequestBody User request) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
			TokenSession token = tokenService.generateToken(userDetails.getUsername());
			return ResponseEntity.ok(token);
		} catch (DisabledException | BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		} 
	}

}
