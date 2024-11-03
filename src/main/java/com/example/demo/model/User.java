package com.example.demo.model;

// import java.sql.Date;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import lombok.Data;

// @Data
// // @Entity
// @Table(name="User")
// public class User {

//     @Id
//     @GeneratedValue(strategy = GenerationType.AUTO)
//     private Integer id;


//     private String name;
//     private String email;
//     private String password;
//     private String gender;
//     private String profession;
//     private Date birthday;
//     public void setPhoneNumber1(Object phoneNumber) {
//     }
//     public void setPhoneNumber2(Object phoneNumber) {
//     }
// }

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import jakarta.persistence.UniqueConstraint;

// @Entity
// @Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
// public class User {
	
// 	@Id
// 	@GeneratedValue(strategy = GenerationType.AUTO)
// 	private Long id;
	
// 	private String email;
// 	private String password;
// 	private String role;
// 	private String fullname;
	
// 	public User() {
// 		super();
// 	}

// 	public User(String email, String password, String role, String fullname) {
		
// 		this.email = email;
// 		this.password = password;
// 		this.role = role;
// 		this.fullname = fullname;
// 	}

// 	public Long getId() {
// 		return id;
// 	}

// 	public void setId(Long id) {
// 		this.id = id;
// 	}

// 	public String getEmail() {
// 		return email;
// 	}

// 	public void setEmail(String email) {
// 		this.email = email;
// 	}

// 	public String getPassword() {
// 		return password;
// 	}

// 	public void setPassword(String password) {
// 		this.password = password;
// 	}

// 	public String getRole() {
// 		return role;
// 	}

// 	public void setRole(String role) {
// 		this.role = role;
// 	}

// 	public String getFullname() {
// 		return fullname;
// 	}

// 	public void setFullname(String fullname) {
// 		this.fullname = fullname;
// 	}
// package com.example.demo.model;

public class User {

	private Long id; // Changed to Long to match the original User model
	private String email;
	private String password;
	private String role;
	private String fullname;

	public User() {
		// Default constructor
	}

	public User(String email, String password, String role, String fullname) {
		this.email = email;
		this.password = password;
		this.role = role;
		this.fullname = fullname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
}
	
	
	
	
	
	
	
	
	






