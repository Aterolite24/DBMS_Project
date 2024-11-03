// package com.example.demo.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import com.example.demo.dto.UserDto;
// import com.example.demo.model.User;
// import com.example.demo.repository.UserRepository;


// @Service
// public class UserServiceImpl implements UserService {
	
// 	@Autowired
//     JdbcTemplate jdbcTemplate;
// 	@Autowired
// 	private PasswordEncoder passwordEncoder;
	
// 	@Autowired
// 	private UserRepository userRepository;

// 	@Override
// 	public User save(UserDto userDto) {
// 		User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()) , userDto.getRole(), userDto.getFullname());
// 		return userRepository.save(user);
// 	}

//     @Override
//     public UserDetails loadUserByUsername(String name) {
//         throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
//     }

// 	@Override
//     public boolean emailExists(String email) {
//         return userRepository.findByEmail(email) != null; // Ensure findByEmail method exists in the repository
//     }

// }
package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User; // Your custom User model

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User save(UserDto userDto) {
		String sql = "INSERT INTO users (email, password, role, fullname) VALUES (?, ?, ?, ?)";

		// Use JdbcTemplate to execute the insert
		jdbcTemplate.update(sql, userDto.getEmail(),
				passwordEncoder.encode(userDto.getPassword()),
				userDto.getRole(),
				userDto.getFullname());

		// If you want to return the saved user, you may want to retrieve it back
		return findByEmail(userDto.getEmail());
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		// Implement the logic to retrieve the user using JDBC
		String sql = "SELECT * FROM users WHERE email = ?";
		User user = jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) ->
				new User(
						rs.getString("email"),
						rs.getString("password"),
						rs.getString("role"),
						rs.getString("fullname")
				)
		);

		// Convert the User to UserDetails
		if (user != null) {
			return new org.springframework.security.core.userdetails.User(
					user.getEmail(),
					user.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
			);
		}
		throw new UsernameNotFoundException("User not found: " + email);
	}

	@Override
	public boolean emailExists(String email) {
		String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
		Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
		return count != null && count > 0;
	}

	// public User findByEmail(String email) {
	// 	String sql = "SELECT * FROM users WHERE email = ?";
	// 	return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) ->
	// 			new User(
	// 					rs.getString("email"),
	// 					rs.getString("password"),
	// 					rs.getString("role"),
	// 					rs.getString("fullname")
	// 			)
	// 	);
	// }
	public User findByEmail(String email) {
		String sql = "SELECT * FROM users WHERE email = ?";
		try {
			List<User> users = jdbcTemplate.query(sql, new Object[]{email}, (rs, rowNum) ->
					new User(
							rs.getString("email"),
							rs.getString("password"),
							rs.getString("role"),
							rs.getString("fullname")
					)
			);
			return users.isEmpty() ? null : users.get(0);
		} catch (DataAccessException e) {
			e.printStackTrace(); // Log the exception or handle it appropriately
			return null; // Or throw a custom exception
		}
	}
	
	
}
