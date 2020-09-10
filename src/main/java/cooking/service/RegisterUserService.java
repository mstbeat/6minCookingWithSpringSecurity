package cooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cooking.entity.Authority;
import cooking.entity.User;
import cooking.repository.RegisterUserMapper;

@Service
@Transactional
public class RegisterUserService {

	@Autowired
	RegisterUserMapper registerUserMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	public void registerUser(User user, Authority authority) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		registerUserMapper.insertUserInfo(user);
	}
	
	public void registerRole(User user, Authority authority) {
		registerUserMapper.insertUserAuthorityInfo(user.getUserId(), authority);
	}
}
