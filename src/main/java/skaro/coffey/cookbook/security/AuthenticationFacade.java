package skaro.coffey.cookbook.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

public interface AuthenticationFacade {
	Authentication getAuthentication();
	SecurityContext getContext();
}
