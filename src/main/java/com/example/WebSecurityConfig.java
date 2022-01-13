package com.example;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableWebSecurity
@ComponentScan
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationSuccessHandler appAuthenticationSuccessHandler(){
       return new AppAuthenticationSuccessHandler();
  }
  @Value("${spring.datasource.url}")
  private String dbUrl;
  @Autowired DataSource dataSource;
  
  @Autowired
  protected void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
	  auth.jdbcAuthentication()
      .dataSource(dataSource)
      .usersByUsernameQuery("SELECT \"employeeName\", password, true" + " from login where \"employeeName\"=?")
      .authoritiesByUsernameQuery("SELECT 'USER' FROM login where \"employeeName\"=?");
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
	    .antMatchers("/admin/**").hasRole("ADMIN")
	    .antMatchers("/user/**").hasRole("USER")
	    .antMatchers("/stylesheets/style.css", "/", "/login*", "/error", "/nouser").permitAll()
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
  	.exceptionHandling().accessDeniedPage("/nouser");
  		//.failureHandler(authenticationFailureHandler())
	    /*.and()
    .logout()
	    .logoutUrl("/logout.html")
	    .deleteCookies("JSESSIONID");
	   	//.logoutSuccessHandler(logoutSuccessHandler());*/
  }
  

}
