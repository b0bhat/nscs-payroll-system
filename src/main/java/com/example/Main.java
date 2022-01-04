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
import org.w3c.dom.ranges.Range;
import org.springframework.http.MediaType;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.sql.Date;
import java.sql.*;
import java.io.*;

@Controller
@SpringBootApplication
public class Main {
  boolean flag = false;
  String logID = new String();

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

  @GetMapping("/login")
  String loginPageHandler(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      flag = false;
      Employee user = new Employee();
      model.put("user", user);
      return "login";
    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @PostMapping(path = "/login", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String login(Map<String, Object> model, Employee user) throws Exception {
    String employeeName = user.getName();
    String password = user.getPassword();

    if (employeeName.equals("admin") && password.equals("123")){
      flag = true;
      logID = "admin";
      return "redirect:/admin/records"; //CHANGE TO MAINPAGE FOR EACH LOGIN TYPE
    }

    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM login";
      ResultSet rs = stmt.executeQuery(sql);

      while (rs.next()) {
        String compareName = rs.getString("employeeName");
        String comparePassword = rs.getString("password");
        if (employeeName.equals(compareName) && password.equals(comparePassword)) {
          //System.out.println("user: " + employeeName + ", " + password);
          flag = true;
          logID = employeeName;
          return "redirect:/user/home";
        }
      } return "nouser";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

//==================================== EMPLOYEES ====================================//

  @GetMapping("/admin/employees")
  String employeeList(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      /*stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, startdate date, enddate date)");*/
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

//==================================== Work Types ====================================//

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

      /*stmt.executeUpdate(
          "CREATE TABLE IF NOT EXISTS employees (id varchar(40), name varchar(40), position varchar(10), role varchar(40),"
              + "team varchar(40), status boolean, startdate date, enddate date)");*/
      String sql = "SELECT * FROM records";
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
    /*
    String list = "SELECT \"employeeName\" FROM login";
    ResultSet rsl = stmt.executeQuery(list);
    ArrayList<String> elist = new ArrayList<String>();
    while (rsl.next()) {
      elist.add(rsl.getString("employeeName"));
    }*/

    String sql = "SELECT SUM(\"workHours\") AS \"workHours\", \"employeeName\", \"workType\", \"clientName\" "
    + " FROM records GROUP BY \"employeeName\", \"workType\", \"clientName\"";
    ResultSet rs = stmt.executeQuery(sql);

    ArrayList<Biweekly> output = new ArrayList<Biweekly>();
    while (rs.next()) {
      Biweekly ret = new Biweekly();
      ret.setEmployeeName(rs.getString("employeeName"));
      ret.setClientName(rs.getString("clientName"));
      ret.setWorkHours(rs.getFloat("workHours"));
      ret.setWorkType(rs.getString("workType"));
      output.add(ret);
    }
    model.put("biweekly", output);
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


//==================================== USER ====================================//

@GetMapping("/user/home")
String recordListUser(Map<String, Object> model) {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    String sql = "SELECT * FROM records WHERE \"employeeName\" = '" + logID + "'";
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
    }
    model.put("workTypes", output);

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
