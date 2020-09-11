package cooking.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class UserRegistrationForm {

	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	@NotEmpty
	private Long[] roleId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long[] getRoleId() {
		return roleId;
	}

	public void setRoleId(Long[] roleId) {
		this.roleId = roleId;
	}
	
}
