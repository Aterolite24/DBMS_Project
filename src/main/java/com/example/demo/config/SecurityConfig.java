// package com.example.demo.config;

// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// // import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// // import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// // import org.springframework.security.config.http.SessionCreationPolicy;
// // import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// // import org.springframework.security.crypto.password.PasswordEncoder;
// // import org.springframework.security.web.SecurityFilterChain;
// // import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
// // import org.springframework.security.core.session.SessionRegistry;
// // import org.springframework.security.core.session.SessionRegistryImpl;

// // import com.example.demo.service.CustomSuccessHandler;
// // import com.example.demo.service.CustomUserDetailsService;

// // @Configuration
// // @EnableWebSecurity
// // public class SecurityConfig {
    
// //     @Autowired
// //     CustomSuccessHandler customSuccessHandler;
    
// //     @Autowired
// //     CustomUserDetailsService customUserDetailsService;
    
// //     @Bean
// //     public static PasswordEncoder passwordEncoder() {
// //         return new BCryptPasswordEncoder();
// //     }

// //     @Bean
// //     public SessionRegistry sessionRegistry() {
// //         return new SessionRegistryImpl(); // Create an instance of SessionRegistryImpl
// //     }
    
// //     @Bean
// //     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// //         http.csrf(c -> c.disable())

// //                 .authorizeHttpRequests(request -> request.requestMatchers("/", "/login", "/registration", "/css/**").permitAll()
// //                         .requestMatchers("/dd").hasAuthority("ROLE_ADMIN")
// //                         .requestMatchers("/client-page").hasAuthority("ROLE_CLIENT")
// //                         .requestMatchers("/dashboard").hasAuthority("ROLE_LAWYER")
// //                         .requestMatchers("/paralegal-page").hasAuthority("ROLE_PARALEGAL")
// //                         .requestMatchers("/registration", "/css/**").permitAll()
// //                         .anyRequest().authenticated())

// //                 .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
// //                         .successHandler(customSuccessHandler).permitAll())

// //                 .logout(form -> form.invalidateHttpSession(true).clearAuthentication(true)
// //                         .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
// //                         .logoutSuccessUrl("/login?logout").permitAll())
                
// // ;
        
// //         return http.build();
// //     }
    
// //     @Autowired
// //     public void configure(AuthenticationManagerBuilder auth) throws Exception {
// //         auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
// //     }
// // }
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// import com.example.demo.service.CustomSuccessHandler;
// import com.example.demo.service.CustomUserDetailsService;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     private final CustomSuccessHandler customSuccessHandler;

//     public SecurityConfig(CustomSuccessHandler customSuccessHandler) {
//         this.customSuccessHandler = customSuccessHandler;
//     }

//     @Bean
//     public UserDetailsService userDetailsService() {
//         return new CustomUserDetailsService();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//         authProvider.setUserDetailsService(userDetailsService());
//         authProvider.setPasswordEncoder(passwordEncoder());
//         return authProvider;
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests(request -> request
//                 .requestMatchers("/", "/login", "/registration", "/css/**").permitAll()
//                 .requestMatchers("/dd").hasAuthority("ROLE_ADMIN")
//                 .requestMatchers("/client-page").hasAuthority("ROLE_CLIENT")
//                 .requestMatchers("/dashboard").hasAuthority("ROLE_LAWYER")
//                 .requestMatchers("/paralegal-page").hasAuthority("ROLE_PARALEGAL")
//                 .anyRequest().authenticated()
//             )
//             .formLogin(form -> form
//                 .loginPage("/login")
//                 .loginProcessingUrl("/login")
//                 .successHandler(customSuccessHandler)
//                 .permitAll()
//             )
//             .logout(logout -> logout
//                 .invalidateHttpSession(true)
//                 .clearAuthentication(true)
//                 .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                 .logoutSuccessUrl("/login?logout")
//                 .permitAll()
//             );

//         return http.build();
//     }


//     protected void configure(AuthenticationManagerBuilder auth) {
//         auth.authenticationProvider(authenticationProvider());
//     }
// }
package com.example.demo.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.example.demo.service.CustomSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    CustomSuccessHandler customSuccessHandler;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())

                .authorizeHttpRequests(request -> request.requestMatchers("/", "/login", "/registration", "/css/**").permitAll()
<<<<<<< HEAD
                        .requestMatchers("/dd").hasAuthority("ROLE_ADMIN")
=======
                        .requestMatchers("/clients").hasAuthority("ROLE_ADMIN")
>>>>>>> 373e9354fe26c33bbebf97387a080b62ba55e741
                        .requestMatchers("/client-page").hasAuthority("ROLE_CLIENT")
                        .requestMatchers("/dashboard").hasAuthority("ROLE_LAWYER")
                        .requestMatchers("/register/paralegal", "/paralegals", "/paralegal-page").hasAuthority("ROLE_PARALEGAL")
                        .anyRequest().authenticated())

                .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
                        .successHandler(customSuccessHandler).permitAll())

                .logout(form -> form.invalidateHttpSession(true).clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout").permitAll());

        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("SELECT email, password, true AS enabled FROM users WHERE email = ?")
                .authoritiesByUsernameQuery("SELECT email, role FROM users WHERE email = ?");
    }
}
