package com.example;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
  auth.inMemoryAuthentication()
      .withUser("user1").password(passwordEncoder().encode("123")).roles("USER")
      .and()
      .withUser("user2").password(passwordEncoder().encode("123")).roles("USER")
      .and()
      .withUser("admin").password(passwordEncoder().encode("123")).roles("ADMIN");
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
  http
    .csrf().disable()
    .authorizeRequests()
    .antMatchers("/admin/**").hasRole("ADMIN")
    .antMatchers("/anonymous*").anonymous()
    .antMatchers("/login*").permitAll()
    .anyRequest().authenticated()
    .and()
    .formLogin()
    .loginPage("/login.html")
    .loginProcessingUrl("/login")
    .defaultSuccessUrl("/user/home.html", true)
    .failureUrl("/error.html")
    //.failureHandler(authenticationFailureHandler())
    .and()
    .logout()
    .logoutUrl("/logout")
    .deleteCookies("JSESSIONID");
    //.logoutSuccessHandler(logoutSuccessHandler());
  }
}
