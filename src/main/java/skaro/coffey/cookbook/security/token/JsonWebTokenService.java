package skaro.coffey.cookbook.security.token;

import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JsonWebTokenService implements TokenService {

	private TokenConfigurationProperties tokenConfig;

	public JsonWebTokenService(TokenConfigurationProperties tokenConfig) {
		this.tokenConfig = tokenConfig;
	}

	@Override
	public TokenSession generateToken(String username) { 
		Date expiration = createNewTokenExpiration();
		String token = createNewToken(expiration, username);
		
		TokenSession session = new TokenSession();
		session.setToken(token);
		session.setUsername(username);
		session.setExpiration(expiration);
		return session;
	}
	
	@Override
	public Boolean isTokenExpired(String token) { 
		Claims claims = Jwts.parser()
				.setSigningKey(tokenConfig.getSecretKey())
				.parseClaimsJws(token)
				.getBody();
		return claims.getExpiration().before(new Date()); 
	}
	
	@Override
	public String getUsernameFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(tokenConfig.getSecretKey())
				.parseClaimsJws(token)
				.getBody()
				.getSubject(); 
	} 
	
	private Date createNewTokenExpiration() {
		long expirationInMinutes = tokenConfig.getValidityTimeInMinutes() * 1000 * 60;
		return new Date(System.currentTimeMillis() + expirationInMinutes);
	}
	
	private String createNewToken(Date expiration, String username) {
		return Jwts.builder().setClaims(new HashMap<>())
				.setSubject(username) 
				.setIssuedAt(new Date(System.currentTimeMillis())) 
				.setExpiration(expiration) 
				.signWith(SignatureAlgorithm.HS512, tokenConfig.getSecretKey()).compact(); 
	}

}
