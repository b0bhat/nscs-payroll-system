package com.example;
import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityHandler implements AuthenticationSuccessHandler {

     public void onAuthenticationSuccess(HttpServletRequest request,   HttpServletResponse response, Authentication authentication) throws IOException  {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ADMIN")) {
            response.sendRedirect("/admin/clients");
        } else if (roles.contains("USER")) {
            response.sendRedirect("/user/home");
        } else response.sendRedirect("/nouser");
    }
}