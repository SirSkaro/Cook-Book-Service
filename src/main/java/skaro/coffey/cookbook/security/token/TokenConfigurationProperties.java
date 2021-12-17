package skaro.coffey.cookbook.security.token;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class TokenConfigurationProperties {

	@NotBlank
	private String secretKey;
	@Positive
	private long validityTimeInMinutes;

	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public long getValidityTimeInMinutes() {
		return validityTimeInMinutes;
	}
	public void setValidityTimeInMinutes(long validityTimeInMinutes) {
		this.validityTimeInMinutes = validityTimeInMinutes;
	}
}
