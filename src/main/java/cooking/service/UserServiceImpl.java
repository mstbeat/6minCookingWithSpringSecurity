package cooking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cooking.entity.User;
import cooking.repository.UserRepository;

@Service
@Primary
public class UserServiceImpl implements UserDetailsService {

	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (StringUtils.isEmpty(username)) {
			throw new UsernameNotFoundException("");
		}

		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("");
		}

		//		Collection<? extends GrantedAuthority> roles = user.getAuthorities();
		//		for (GrantedAuthority role : roles) {
		//			System.out.println(role.getAuthority());
		//		}

		return user;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<User> findAll() {
		List<User> users = userRepository.findAll();
		return users;
	}
	
	public User findOne(Long userId) {
		User user = userRepository.findByUserId(userId);
		return user;
	}

}
