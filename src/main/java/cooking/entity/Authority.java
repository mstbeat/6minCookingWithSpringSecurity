package cooking.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "Authorities")
public class Authority implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2309086467629463443L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "roleid", nullable = false, unique = true)
	private Long roleId;

	@Column(name = "role", nullable = false)
	private String authority;

	@ManyToMany(targetEntity = User.class, mappedBy = "authorities", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<User> users = new HashSet<>();;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return authority;
	}

	public void setRole(String role) {
		this.authority = role;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

	//	@Id
	//	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	//    @JoinColumn(name = "userid", referencedColumnName = "userid")
	//	private User userId;
	//
	//	@Column(nullable = false)
	//	private String authority;
	//	
	//	@Override
	//	public String getAuthority() {
	//		return this.authority;
	//	}
	//
	//	public User getUserId() {
	//		return userId;
	//	}
	//
	//	public void setUserId(User userId) {
	//		this.userId = userId;
	//	}
	//
	//	public void setAuthority(String authority) {
	//		this.authority = authority;
	//	}

}
