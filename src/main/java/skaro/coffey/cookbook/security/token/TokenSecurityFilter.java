package skaro.coffey.cookbook.security.token;

import static java.util.function.Predicate.not;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import skaro.coffey.cookbook.security.AuthenticationFacade;

public class TokenSecurityFilter extends OncePerRequestFilter {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private TokenService tokenService;
	private UserDetailsService userDetailsService;
	private AuthenticationFacade authFacade;
	
	public TokenSecurityFilter(TokenService tokenService, UserDetailsService userDetailsService, AuthenticationFacade authFacade) {
		this.tokenService = tokenService;
		this.userDetailsService = userDetailsService;
		this.authFacade = authFacade;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			extractTokenFromHeader(request)
				.filter(not(tokenService::isTokenExpired))
				.map(this::getUserDetails)
				.ifPresent(userDetails -> authenticate(userDetails, request));
		} catch(UsernameNotFoundException e) {
			LOG.info("User unkown user tried to authenticate with a token");
		}
		
		filterChain.doFilter(request, response);
	}
	
	private Optional<String> extractTokenFromHeader(HttpServletRequest request) {
		String expectedAuthPrefix = "Bearer ";
		return Optional.ofNullable(request.getHeader("Authorization"))
				.filter(authHeader -> StringUtils.startsWithIgnoreCase(authHeader, expectedAuthPrefix))
				.map(authHeader -> authHeader.substring(expectedAuthPrefix.length()));
	}
	
	private UserDetails getUserDetails(String token) {
		String usernameFromToken = tokenService.getUsernameFromToken(token);
		return userDetailsService.loadUserByUsername(usernameFromToken);
	}
	
	private void authenticate(UserDetails userDetails, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        authFacade.getContext().setAuthentication(authenticationToken);
	}
	

}
