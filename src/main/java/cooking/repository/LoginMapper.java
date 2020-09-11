package cooking.repository;

import org.apache.ibatis.annotations.Mapper;

import cooking.entity.User;

@Mapper
public interface LoginMapper {

	public User findByUsername(String username);
}
