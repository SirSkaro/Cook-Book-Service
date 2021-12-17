package skaro.coffey.cookbook.security.token;

public interface TokenService {
	TokenSession generateToken(String username);
	Boolean isTokenExpired(String token);
	String getUsernameFromToken(String token);
}
