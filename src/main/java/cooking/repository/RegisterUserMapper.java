package cooking.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cooking.entity.Authority;
import cooking.entity.User;

@Mapper
public interface RegisterUserMapper {

	public void insertUserInfo(User user);
	
	public void insertUserAuthorityInfo(@Param("userId") Long userId, @Param("a") Authority authority);
}
