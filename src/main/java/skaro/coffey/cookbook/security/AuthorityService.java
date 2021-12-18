package skaro.coffey.cookbook.security;

public interface AuthorityService {
	public static final String NAMED_USER_AUTHORITY = "named user";
	
	boolean isNamedUser();
	
}
