// package com.example.demo.repository;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import com.example.demo.model.User;



// @Repository
// public interface UserRepository extends JpaRepository<User, Long> {
    
// 	User findByEmail (String email);
// }
package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// RowMapper to map ResultSet to User
	private RowMapper<User> userRowMapper = new RowMapper<>() {
		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getLong("id"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setRole(rs.getString("role"));
			user.setFullname(rs.getString("fullname"));
			return user;
		}
	};

	// Save a new user to the database
	public User save(User user) {
		String sql = "INSERT INTO users (email, password, role, fullname) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getRole(), user.getFullname());
		return user; // Optionally, you might want to return the saved user with an ID assigned
	}

	// Find a user by email
	public User findByEmail(String email) {
		String sql = "SELECT * FROM users WHERE email = ?";
		return jdbcTemplate.queryForObject(sql, userRowMapper, email);
	}

	// Optionally, you might add a method to find a user by ID
	public User findById(Long id) {
		String sql = "SELECT * FROM users WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, userRowMapper, id);
	}
}
