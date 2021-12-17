package skaro.coffey.cookbook.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringAuthenticationFacade implements AuthenticationFacade {

	@Override
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	@Override
	public SecurityContext getContext() {
		return SecurityContextHolder.getContext();
	}

}
