// package com.example.demo.service;

// import java.io.IOException;
// import java.util.Collection;

// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
// import org.springframework.stereotype.Service;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Service
// public class CustomSuccessHandler implements AuthenticationSuccessHandler {

//     @Override
//     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                         Authentication authentication) throws IOException, ServletException {
//         Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

//         boolean isAdmin = authorities.stream()
//             .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//         boolean isClient = authorities.stream()
//             .anyMatch(authority -> authority.getAuthority().equals("ROLE_CLIENT"));
//         boolean isLawyer = authorities.stream()
//             .anyMatch(authority -> authority.getAuthority().equals("ROLE_LAWYER"));
//         boolean isParalegal = authorities.stream()
//             .anyMatch(authority -> authority.getAuthority().equals("ROLE_PARALEGAL"));

//         if (isAdmin) {
//             response.sendRedirect("/dd");
//         } else if (isClient) {
//             response.sendRedirect("/client-page");
//         } else if (isLawyer) {
//             response.sendRedirect("/dashboard");
//         } else if (isParalegal) {
//             response.sendRedirect("/paralegals");
//         } else {
//             response.sendRedirect("/error"); // Handle case with no matching roles
//         }
//     }
// }
package com.example.demo.service;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isClient = authorities.stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_CLIENT"));
        boolean isLawyer = authorities.stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_LAWYER"));
        boolean isParalegal = authorities.stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_PARALEGAL"));

        if (isAdmin) {
            response.sendRedirect("/clients");
        } else if (isClient) {
            response.sendRedirect("/client-page");
        } else if (isLawyer) {
            response.sendRedirect("/dashboard");
        } else if (isParalegal) {
            response.sendRedirect("/paralegals");
        } else {
            response.sendRedirect("/error"); // Handle case with no matching roles
        }
    }
}
