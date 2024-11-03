// package com.example.demo.service;

// import java.util.Collection;
// import java.util.List;

// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;

// import com.example.demo.model.User;


// public class CustomUserDetail implements UserDetails {
	
// 	private User user;
	
// 	public CustomUserDetail(User user) {
// 		this.user = user;
// 	}

// 	@Override
// 	public Collection<? extends GrantedAuthority> getAuthorities() {

// 		return List.of(() -> user.getRole());
// 	}
	
// 	public String getFullname() {
// 		return user.getFullname();
// 	}

// 	@Override
// 	public String getPassword() {
// 		return user.getPassword();
// 	}

// 	@Override
// 	public String getUsername() {
// 		return user.getEmail();
// 	}

// 	@Override
// 	public boolean isAccountNonExpired() {

// 		return true;
// 	}

// 	@Override
// 	public boolean isAccountNonLocked() {
// 		// TODO Auto-generated method stub
// 		return true;
// 	}

// 	@Override
// 	public boolean isCredentialsNonExpired() {
// 		// TODO Auto-generated method stub
// 		return true;
// 	}

// 	@Override
// 	public boolean isEnabled() {
// 		// TODO Auto-generated method stub
// 		return true;
// 	}

// }
package com.example.demo.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.User;


public class CustomUserDetail implements UserDetails {
	
	private User user;
	
	public CustomUserDetail(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of(() -> user.getRole());
	}
	
	public String getFullname() {
		return user.getFullname();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}