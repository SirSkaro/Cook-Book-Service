package skaro.coffey.cookbook.security;

import static skaro.coffey.cookbook.security.LoginController.LOGIN_ENDPOINT;
import static skaro.coffey.cookbook.security.token.TokenSecurityFilter.TOKEN_SECURITY_FILTER_BEAN;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class CookbookSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

	private UserDetailsService userDetailsSerivce;
	private Filter tokenFilter;
	private PasswordEncoder passwordEnconder;
	
	public CookbookSecurityConfigurerAdapter(UserDetailsService userDetailsSerivce, 
			@Qualifier(TOKEN_SECURITY_FILTER_BEAN) Filter tokenFilter,
			PasswordEncoder passwordEnconder) {
		this.userDetailsSerivce = userDetailsSerivce;
		this.tokenFilter = tokenFilter;
		this.passwordEnconder = passwordEnconder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsSerivce)
			.passwordEncoder(passwordEnconder);
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors().disable()
	        .authorizeRequests()
	        	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	        	.antMatchers(LOGIN_ENDPOINT).permitAll()
				.antMatchers(HttpMethod.GET).anonymous()
				.antMatchers(HttpMethod.GET).authenticated()
				.antMatchers(HttpMethod.GET).permitAll()
				.anyRequest().authenticated()
	        .and()
	        	.httpBasic()
	        .and()
	        	.csrf().disable()
	        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
}
