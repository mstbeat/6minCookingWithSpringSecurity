package cooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cooking.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findByUserId(Long userId);

	public User findByUsername(String username);
	
	public List<User> findAll();
}
