package cooking.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cooking.entity.User;
import cooking.repository.LoginMapper;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	LoginMapper loginMapper;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = Optional.ofNullable(loginMapper.findByUsername(username))
				.orElseThrow(() -> new UsernameNotFoundException("該当するユーザー名は存在しません。"));

		//		Collection<? extends GrantedAuthority> roles = user.getAuthorities();
		//		for (GrantedAuthority role : roles) {
		//			System.out.println(role.getAuthority());
		//		}

		return user;
	}
}
