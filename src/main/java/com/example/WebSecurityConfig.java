package com.example;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;


@Configuration
@EnableWebSecurity
@ComponentScan
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationSuccessHandler appAuthenticationSuccessHandler(){
       return new AppAuthenticationSuccessHandler();
  }
  /*
  @Value("${postgres://auaitxqyiisnvi:551b9daa9ff01f53183c0b48855a0043189e38249ab97382c5399360f56af6dc@ec2-52-54-167-8.compute-1.amazonaws.com:5432/dd3k7h39t68knm}")
  private String dbUrl;*/
  @Autowired DataSource dataSource;

  @Autowired
  protected void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
	  
	  //String encoded = new BCryptPasswordEncoder().encode("123");
	  //System.out.println("\n\n" + encoded + "\n\n");
	  //PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	  auth.jdbcAuthentication()
	  .passwordEncoder(passwordEncoder())
      .dataSource(dataSource)
      .usersByUsernameQuery("SELECT \"employeeName\", password, 1" + " from login where \"employeeName\"=?")
      .authoritiesByUsernameQuery("SELECT \"employeeName\", 'ROLE_USER' AS authority FROM login where \"employeeName\"=?");
	  
	  auth.inMemoryAuthentication()
	  .withUser("admin").password(passwordEncoder().encode("123")).roles("ADMIN");
	  /*
	  auth.inMemoryAuthentication()
      .withUser("AW").password(passwordEncoder().encode("AW")).roles("USER")
      .and()
      .withUser("bobman").password(passwordEncoder().encode("123")).roles("USER")
      .and()
      .withUser("admin").password(passwordEncoder().encode("123")).roles("ADMIN");*/
  }

  @Autowired
  SecurityHandler securityHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
	  CharacterEncodingFilter filter = new CharacterEncodingFilter();
      filter.setEncoding("UTF-8");
      filter.setForceEncoding(true);
      http.addFilterBefore(filter,CsrfFilter.class);
  http
    .csrf().disable()
    .authorizeRequests()
      	.antMatchers("/login", "/error", "/nouser", "/", "/logout").permitAll()
	    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
	    .antMatchers("/user/**").access("hasRole('ROLE_USER')")
	    .antMatchers("/stylesheets/style.css", "/login*", "/error", "/nouser", "/", "/logout").permitAll()
	    .anyRequest().authenticated()
	    .and()
    .formLogin()
	    .loginPage("/login")
	    .loginProcessingUrl("/login")
	    .usernameParameter("username")
	    .passwordParameter("password")
	    //.successForwardUrl("/admin/clients")
	    .successHandler(securityHandler)
	    //.defaultSuccessUrl("/admin/clients", false)
	    .failureUrl("/nouser")
  	.and()
  	  .exceptionHandling().accessDeniedPage("/nouser")
  		//.failureHandler(authenticationFailureHandler())
	    .and()
    .logout()
	    .logoutUrl("/logout.html")
	    .logoutSuccessUrl("/login")
	    .invalidateHttpSession(true)  
	    .deleteCookies("JSESSIONID");
	   	//.logoutSuccessHandler(logoutSuccessHandler());*/
  }


}
