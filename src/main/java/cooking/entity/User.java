package cooking.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "Users")
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -106222357649915159L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userid", nullable = false, unique = true)
	private Long userId;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(name = "enabled", nullable = false)
	private int enbaled;

	@ManyToMany(targetEntity = Authority.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "Users_Authorities",
			joinColumns = {
					@JoinColumn(name = "userid", referencedColumnName = "userid",
							nullable = false, updatable = false)},
			inverseJoinColumns = {
					@JoinColumn(name = "roleid", referencedColumnName = "roleid",
							nullable = false, updatable = false)})
	private Set<Authority> authorities = new HashSet<>();;
	
//	@OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    protected List<Authority> authorities;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities ;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getEnbaled() {
		return enbaled;
	}

	public void setEnbaled(int enbaled) {
		this.enbaled = enbaled;
	}

}
