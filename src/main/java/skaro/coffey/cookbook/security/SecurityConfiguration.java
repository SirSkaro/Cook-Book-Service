package skaro.coffey.cookbook.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import skaro.coffey.cookbook.security.token.TokenConfigurationProperties;

@Configuration
public class SecurityConfiguration {
	private static final String TOKEN_CONFIGURATION_PROPERTIES_PREIX = "skaro.cookbook.security.token";

	@Bean
	@ConfigurationProperties(TOKEN_CONFIGURATION_PROPERTIES_PREIX)
	public TokenConfigurationProperties getTokenConfigurationProperties() {
		return new TokenConfigurationProperties();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManagerBean(WebSecurityConfigurerAdapter adapter) throws Exception {
		return adapter.authenticationManagerBean();
	}
	

}
