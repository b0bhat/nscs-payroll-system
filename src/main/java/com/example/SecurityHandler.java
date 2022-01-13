package com.example;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityHandler implements AuthenticationSuccessHandler {

     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException  {
    	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         String role = auth.getAuthorities().toString();
         System.out.println(role);
        if (role.contains("ADMIN")) {
            response.sendRedirect("/admin/clients");
        } else if (role.contains("USER")) {
            response.sendRedirect("/user/home");
        } else response.sendRedirect("/nouser");
    }
}