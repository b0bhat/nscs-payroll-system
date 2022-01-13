/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;*/

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.sql.Date;
import java.io.*;

@Controller
@SpringBootApplication
public class Main {
  boolean flag = true;
  String logID = "admin"; //fix me
  java.sql.Date baseDate = java.sql.Date.valueOf("2000-01-01");
  Date startDate = baseDate;
  Date endDate = baseDate;
/*
  @Autowired
  private LoggedUserManagementService userService;*/

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index(Map<String, Object> model) {
    return "redirect:/login";
  }

  @GetMapping
    public String getLoginPage() {
        return "login";
    }


/*
  @GetMapping("/login")
  String loginPageHandler(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      //Statement stmt = connection.createStatement();
      Employee user = new Employee();
      model.put("user", user);
      System.out.println("pre");
      return "login";
    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  //@PostMapping(path = "/login", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  //public String login(Map<String, Object> model, Employee user) throws Exception {
	System.out.println("post");
	return "redirect:/admin/clients";
    String employeeName = user.getName();
    String password = user.getPassword();
    System.out.println("login: " + employeeName + ", " + password);
    if (employeeName.equals("admin") && password.equals("123")){
      flag = true;
      logID = "admin";
      return "redirect:/admin/records"; //CHANGE TO MAINPAGE FOR EACH LOGIN TYPE
    }

    //LoginController loginController = new LoginController(userService);

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM login";
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String compareName = rs.getString("employeeName");
        String comparePassword = rs.getString("password");
        if (employeeName.equals(compareName) && password.equals(comparePassword)) {
          System.out.println("user: " + compareName + ", " + comparePassword);
          flag = true;
          logID = employeeName;
          return "redirect:/user/home";
        }
      } return "nouser";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }*/

//==================================== EMPLOYEES ====================================//

  @GetMapping("/admin/employees")
  String employeeList(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      String sql = "SELECT * FROM login";
      ResultSet rs = stmt.executeQuery(sql);

      ArrayList<Employee> output = new ArrayList<Employee>();
      while (rs.next()) {
        Employee emp = new Employee();
        emp.setName(rs.getString("employeeName"));
        emp.setPassword(rs.getString("password"));
        output.add(emp);
      }
      model.put("employees", output);
      if (flag && logID == "admin") {
        return "admin/employees";
      } else {
        return "nouser";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/admin/addEmployee")
  public String returnEmployeeAdd(Map<String, Object> model) throws Exception {
    Employee employee = new Employee();
    model.put("employee", employee);
    if (flag && logID == "admin") {
      return "admin/addEmployee";
    } else {
      return "error";
    }
  }

  @PostMapping(path = "/admin/addEmployee", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String handleEmployeeAdd(Map<String, Object> model, Employee employee) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      String sql = "INSERT INTO login VALUES ('"
          + employee.getName() + "','" + employee.getPassword() + "')";

      stmt.executeUpdate(sql);
      return "redirect:/admin/employees"; // Directly returns to employee homepage
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/admin/deleteEmployee")
  public String deleteEmployee(Map<String, Object> model, @RequestParam String e_id) {
    try (Connection connection = dataSource.getConnection()) {
      String sql = "DELETE FROM login WHERE \"employeeName\" =?";
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setString(1, e_id);
      ps.executeUpdate();
      System.out.println(ps);

      return "redirect:/admin/employees";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

//==================================== CLIENTS ====================================//

  @GetMapping("/admin/clients")
  String clientList(Map<String, Object> model) {
	System.out.println("admin clients");
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      String sql = "SELECT * FROM clients";
      ResultSet rs = stmt.executeQuery(sql);

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add(rs.getString("clientName"));
      }
      model.put("clients", output);
      if (flag && logID == "admin") {
        return "admin/clients";
      } else {
        return "nouser";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/admin/addClient")
  public String returnClientAdd(Map<String, Object> model) throws Exception {
    String client = new String();
    model.put("client", client);
    if (flag && logID == "admin") {
      return "admin/addClient";
    } else {
      return "error";
    }
  }

  @PostMapping(path = "/admin/addClient", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String handleClientAdd(Map<String, Object> model, @RequestParam String client) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      String sql = "INSERT INTO clients VALUES ('"
          + client + "')";

      stmt.executeUpdate(sql);
      return "redirect:/admin/clients"; // Directly returns to employee homepage
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

//==================================== WORK TYPES ====================================//

    @GetMapping("/admin/worktypes")
    String workTypeList(Map<String, Object> model) {
      try (Connection connection = dataSource.getConnection()) {
        Statement stmt = connection.createStatement();

        String sql = "SELECT * FROM \"workTypes\"";
        ResultSet rs = stmt.executeQuery(sql);

        ArrayList<String> output = new ArrayList<String>();
        while (rs.next()) {
          output.add(rs.getString("workType"));
        }
        model.put("workTypes", output);
        if (flag && logID == "admin") {
          return "admin/worktypes";
        } else {
          return "nouser";
        }
      } catch (Exception e) {
        model.put("message", e.getMessage());
        return "error";
      }
    }

    @GetMapping("/admin/addWorkType")
    public String returnWorkTypeAdd(Map<String, Object> model) throws Exception {
      String workType = new String();
      model.put("workType", workType);
      if (flag && logID == "admin") {
        return "admin/addWorkType";
      } else {
        return "error";
      }
    }

    @PostMapping(path = "/admin/addWorkType", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public String handleWorkTypeAdd(Map<String, Object> model, @RequestParam String workType) throws Exception {
      try (Connection connection = dataSource.getConnection()) {
        Statement stmt = connection.createStatement();

        String sql = "INSERT INTO \"workTypes\" VALUES ('"
            + workType + "')";

        stmt.executeUpdate(sql);
        return "redirect:/admin/worktypes"; // Directly returns to employee homepage
      } catch (Exception e) {
        model.put("message", e.getMessage());
        return "error";
      }
    }

//==================================== RECORDS ====================================//

  @GetMapping("/admin/records")
  String recordListAll(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      String sql = "SELECT * FROM records ORDER BY \"workDate\" DESC";
      ResultSet rs = stmt.executeQuery(sql);

      ArrayList<Record> output = new ArrayList<Record>();
      while (rs.next()) {
        Record ret = new Record();
        ret.setEmployeeName(rs.getString("employeeName"));
        ret.setClientName(rs.getString("clientName"));
        ret.setRecordID(rs.getString("recordID"));
        ret.setWorkHours(rs.getFloat("workHours"));
        ret.setWorkType(rs.getString("workType"));
        ret.setWorkDate(rs.getDate("workDate"));
        output.add(ret);
      }
      model.put("records", output);
      if (flag && logID == "admin") {
        return "admin/records";
      } else {
        return "nouser";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

//==================================== TOOLS ====================================//

@GetMapping("/admin/biweekly")
String biweeklyTool(Map<String, Object> model) {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    String sql;
    if (startDate == baseDate || endDate == baseDate) {
      sql = "SELECT SUM(\"workHours\") AS \"workHours\", \"employeeName\", \"workType\" "
      + " FROM records GROUP BY \"employeeName\", \"workType\" ORDER BY \"employeeName\" ASC";
    } else {
      sql = "SELECT SUM(\"workHours\") AS \"workHours\", \"employeeName\", \"workType\" "
      + " FROM records WHERE (\"workDate\" >= '" + startDate + "' AND \"workDate\" <= '" + endDate + "') "
      + "GROUP BY \"employeeName\", \"workType\" ORDER BY \"employeeName\" ASC";
    } ResultSet rs = stmt.executeQuery(sql);

    ArrayList<Biweekly> output = new ArrayList<Biweekly>();
    while (rs.next()) {
      Biweekly ret = new Biweekly();
      ret.setEmployeeName(rs.getString("employeeName"));
      ret.setWorkHours(rs.getFloat("workHours"));
      ret.setWorkType(rs.getString("workType"));
      output.add(ret);
    }
    model.put("biweekly", output);

    dateRange date = new dateRange();
    if (startDate == baseDate || endDate == baseDate) {
      java.sql.Date curDate = new Date(System.currentTimeMillis());
      date.setStartDate(curDate);
      date.setEndDate(curDate);
    } else {
      date.setStartDate(startDate);
      date.setEndDate(endDate);
    }
    model.put("date", date);

    if (flag && logID == "admin") {
      return "admin/biweekly";
    } else {
      return "nouser";
    }
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

@PostMapping(path = "/admin/biweekly", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
public String handleBiweeklyDate(Map<String, Object> model, dateRange date) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    startDate = date.getStartDate();
    endDate = date.getEndDate();
    return "redirect:/admin/biweekly";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

@GetMapping("/admin/monthly")
String monthlyTool(Map<String, Object> model) {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    String select = "SELECT * FROM clients";
    ResultSet rsc = stmt.executeQuery(select);
    ArrayList<String> clientList = new ArrayList<String>();
    while (rsc.next()) {
      System.out.println("client \n");
      String client = rsc.getString("clientName");
      clientList.add(client);
    }
    ArrayList<MonthlyList> allList = new ArrayList<MonthlyList>();
    for (String client : clientList) {
      String sql;
      if (startDate == baseDate || endDate == baseDate) {
        sql = "SELECT \"employeeName\", \"workHours\", \"workDate\" "
        + "FROM records WHERE \"clientName\" = '" + client + "' ORDER BY \"clientName\", \"workDate\" ASC";
      } else {
        sql = "SELECT \"employeeName\", \"workHours\", \"workDate\" "
        + "FROM records WHERE \"clientName\" = '" + client + "' AND (\"workDate\" >= '" + startDate + "' AND \"workDate\" <= '" + endDate + "') "
        + "ORDER BY \"clientName\", \"workDate\" ASC";
      } ResultSet rs = stmt.executeQuery(sql);
      System.out.println("clients \n");
      ArrayList<Monthly> records = new ArrayList<Monthly>();
      int num = 0;
      while (rs.next()) {
        num++;
        Monthly ret = new Monthly();
        System.out.println("monthly \n");
        ret.setEmployeeName(rs.getString("employeeName"));
        ret.setWorkHours(rs.getFloat("workHours"));
        ret.setWorkDate(rs.getDate("workDate"));
        records.add(ret);
      }
      MonthlyList output = new MonthlyList();
      if (num != 0) {
        output.setRecords(records);
        output.setClientName(client);
        allList.add(output);
      }
    } model.put("allList", allList);

    dateRange date = new dateRange();
    if (startDate == baseDate || endDate == baseDate) {
      java.sql.Date curDate = new Date(System.currentTimeMillis());
      date.setStartDate(curDate);
      date.setEndDate(curDate);
    } else {
      date.setStartDate(startDate);
      date.setEndDate(endDate);
    }
    model.put("date", date);

    if (flag && logID == "admin") {
      return "admin/monthly";
    } else {
      return "nouser";
    }
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

@PostMapping(path = "/admin/monthly", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
public String handleMonthlySubmit(Map<String, Object> model, dateRange date) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    startDate = date.getStartDate();
    endDate = date.getEndDate();
    return "redirect:/admin/monthly";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

//==================================== USER ====================================//

@GetMapping("/user/home")
String recordListUser(Map<String, Object> model) {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    String sql = "SELECT * FROM records WHERE \"employeeName\" = '" + logID + "'ORDER BY \"workDate\" DESC";
    System.out.println(sql);
    ResultSet rs = stmt.executeQuery(sql);

    ArrayList<Record> output = new ArrayList<Record>();
    while (rs.next()) {
      Record ret = new Record();
      ret.setEmployeeName(rs.getString("employeeName"));
      ret.setClientName(rs.getString("clientName"));
      ret.setRecordID(rs.getString("recordID"));
      ret.setWorkHours(rs.getFloat("workHours"));
      ret.setWorkType(rs.getString("workType"));
      ret.setWorkDate(rs.getDate("workDate"));
      output.add(ret);
    }
    model.put("records", output);
    if (flag) {
      return "user/home";
    } else {
      return "nouser";
    }
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

@GetMapping("/user/addRecord")
public String returnRecordAdd(Map<String, Object> model) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();

    String sql = "SELECT * FROM \"workTypes\"";
    ResultSet rs = stmt.executeQuery(sql);
    ArrayList<String> output = new ArrayList<String>();
    while (rs.next()) {
      output.add(rs.getString("workType"));
    } model.put("workTypes", output);

    String sql_c = "SELECT * FROM clients";
    ResultSet rs_c = stmt.executeQuery(sql_c);
    ArrayList<String> output_c = new ArrayList<String>();
    while (rs_c.next()) {
      output_c.add(rs_c.getString("clientName"));
    } model.put("clients", output_c);

    Record record = new Record();
    model.put("record", record);
    if (flag) {
      return "user/addRecord";
    } else {
      return "nouser";
    }
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

@PostMapping(path = "/user/addRecord", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
public String handleRecordAdd(Map<String, Object> model, Record record) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    final String UniqueID = UUID.randomUUID().toString().replace("-", "");
    record.setRecordID(UniqueID);
    record.setEmployeeName(logID);

    String sql = "INSERT INTO records VALUES ('"
        + record.getRecordID() + "','" + record.getClientName() + "','" + record.getWorkHours() + "','"
        + record.getWorkType() + "','" + record.getWorkDate() + "','" + record.getEmployeeName() + "')";

    stmt.executeUpdate(sql);

    java.sql.Date sqlDate = new Date(System.currentTimeMillis());
    String save = "INSERT INTO \"oldRecords\" VALUES ('"
        + UUID.randomUUID().toString().replace("-", "") + "','"
        + record.getClientName() + "','" + record.getWorkHours() + "','" + record.getWorkType() + "','"
        + record.getWorkDate() + "','" + record.getEmployeeName() + "','"
        + sqlDate + "','add','" +
        record.getRecordID() + "')";
    stmt.executeUpdate(save);

    return "redirect:/user/home";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

@GetMapping("/user/deleteRecord")
public String deleteRecord(Map<String, Object> model, @RequestParam String e_id) {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    //System.out.println(e_id);
    String search = "SELECT * FROM records WHERE \"recordID\" = '" + e_id + "'";
    //System.out.println(search);
    ResultSet rs = stmt.executeQuery(search);
    Record ret = new Record();
    if (rs.next()) {
      ret.setEmployeeName(rs.getString("employeeName"));
      ret.setClientName(rs.getString("clientName"));
      ret.setRecordID(rs.getString("recordID"));
      ret.setWorkHours(rs.getFloat("workHours"));
      ret.setWorkType(rs.getString("workType"));
      ret.setWorkDate(rs.getDate("workDate"));

      java.sql.Date sqlDate = new Date(System.currentTimeMillis());
      String save = "INSERT INTO \"oldRecords\" VALUES ('"
          + UUID.randomUUID().toString().replace("-", "") + "','"
          + ret.getClientName() + "','" + ret.getWorkHours() + "','" + ret.getWorkType() + "','"
          + ret.getWorkDate() + "','" + ret.getEmployeeName() + "','"
          + sqlDate + "','del','" +
          ret.getRecordID() + "')";

      //System.out.println(save);
      stmt.executeUpdate(save);

      String sql = "DELETE FROM records WHERE \"recordID\" =?";
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setString(1, e_id);
      System.out.println(ps);
      ps.executeUpdate();
    }

    return "redirect:/user/home";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}
