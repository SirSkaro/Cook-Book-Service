package skaro.coffey.cookbook.security.token;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class TokenConfigurationProperties {

	@NotBlank
	private String secretKey;
	@Positive
	private long validityTime;

	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public long getValidityTime() {
		return validityTime;
	}
	public void setValidityTime(long validityTime) {
		this.validityTime = validityTime;
	}
}
