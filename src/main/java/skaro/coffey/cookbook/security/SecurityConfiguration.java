package skaro.coffey.cookbook.security;

import static skaro.coffey.cookbook.security.LoginController.LOGIN_ENDPOINT;

import javax.servlet.Filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import skaro.coffey.cookbook.security.token.JsonWebTokenService;
import skaro.coffey.cookbook.security.token.TokenConfigurationProperties;
import skaro.coffey.cookbook.security.token.TokenSecurityFilter;
import skaro.coffey.cookbook.security.token.TokenService;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final String TOKEN_SECURITY_FILTER_BEAN = "tokenSecurityFilter";
	private static final String TOKEN_CONFIGURATION_PROPERTIES_PREIX = "skaro.cookbook.security.token";

	private UserDetailsService userDetailsSerivce;
	
	public SecurityConfiguration(UserDetailsService userDetailsSerivce) {
		this.userDetailsSerivce = userDetailsSerivce;
	}
	
	@Bean
	@ConfigurationProperties(TOKEN_CONFIGURATION_PROPERTIES_PREIX)
	public TokenConfigurationProperties getTokenConfigurationProperties() {
		return new TokenConfigurationProperties();
	}
	
	@Bean
	public TokenService getTokenService() {
		return new JsonWebTokenService(getTokenConfigurationProperties());
	}
	
	@Bean
	public AuthenticationFacade getAuthenticationFacade() {
		return new SpringAuthenticationFacade();
	}
	
	@Bean(TOKEN_SECURITY_FILTER_BEAN)
	public Filter getTokenFilter() {
		return new TokenSecurityFilter(getTokenService(), userDetailsSerivce, getAuthenticationFacade());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsSerivce)
			.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
			.authorizeRequests().antMatchers(LOGIN_ENDPOINT).permitAll()
			.anyRequest().anonymous()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.addFilterBefore(getTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
