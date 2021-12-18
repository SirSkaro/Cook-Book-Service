package skaro.coffey.cookbook.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import liquibase.util.StringUtils;

@Service
public class CookbookAuthorityService implements AuthorityService {

	private AuthenticationFacade authFacade;
	
	public CookbookAuthorityService(AuthenticationFacade authFacade) {
		this.authFacade = authFacade;
	}

	@Override
	public boolean isNamedUser() {
		return authFacade.getAuthentication().getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(authority -> StringUtils.equalsIgnoreCaseAndEmpty(authority, NAMED_USER_AUTHORITY));
	}

}
