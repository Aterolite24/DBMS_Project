package com.example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	// @Bean(name="entityManagerFactory")
	// public LocalSessionFactoryBean sessionFactory() {
	// LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	// return sessionFactory;
	// }
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
