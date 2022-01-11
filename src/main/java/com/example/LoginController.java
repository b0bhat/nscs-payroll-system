package com.example;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;

@Component
@Scope("request")
public class LoginController {

    private final LoggedUserManagementService userService;
    private String name;
    private String password;

    public LoginController (LoggedUserManagementService userService) {
      this.userService = userService;
    }
    
    public boolean login(String compareName, String comparePassword) {
      if ((this.getName()).equals(compareName) && (this.getPassword()).equals(comparePassword)) {
        userService.setName(this.getName());
        return true;
      } else return false;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }
    public String getPassword() {
        return this.password;
    }

}
