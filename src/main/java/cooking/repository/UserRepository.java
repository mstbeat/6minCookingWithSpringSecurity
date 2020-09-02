package cooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cooking.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findByUserId(int userID);

	public User findByUsername(String username);

}
