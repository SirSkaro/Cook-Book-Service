package skaro.coffey.cookbook.security;

import static skaro.coffey.cookbook.security.AuthorityService.NAMED_USER_AUTHORITY;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import skaro.coffey.cookbook.security.user.UserRepository;

@Service
public class CookbookUserDetialsService implements UserDetailsService {

	private UserRepository userRepository;
	
	public CookbookUserDetialsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findById(username)
				.map(user -> new User(user.getUsername(), user.getPassword(), createNamedUserGrantedAuthorities()))
				.orElseThrow(() -> new UsernameNotFoundException("User "+username+" not found"));
	}
	
	private List<GrantedAuthority> createNamedUserGrantedAuthorities() {
		return List.of(new SimpleGrantedAuthority(NAMED_USER_AUTHORITY));
	}

}
