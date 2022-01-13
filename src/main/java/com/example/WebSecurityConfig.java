package com.example;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  auth.inMemoryAuthentication()
      .withUser("user1").password(passwordEncoder().encode("123")).roles("USER")
      .and()
      .withUser("bobman").password(passwordEncoder().encode("123")).roles("USER")
      .and()
      .withUser("admin").password(passwordEncoder().encode("123")).roles("ADMIN");
  }
  
  @Autowired
  SecurityHandler securityHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
  http
    .csrf().disable()
    .authorizeRequests()
	    .antMatchers("/admin/**").hasRole("ADMIN")
	    .antMatchers("/user/**").hasRole("USER")
	    .antMatchers("/stylesheets/style.css", "/", "/login*", "/error.html", "/nouser.html").permitAll()
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
	    .failureUrl("/nouser.html")
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
