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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Controller
@SpringBootApplication
public class Main {
  boolean flag = true;
  boolean edit = true;

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }

  @GetMapping("/employees")
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
      if (flag && edit) {
        return "employees";
      } else {
        return "error";
      }
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping("/employees/add")
  public String returnEmployeeAdd(Map<String, Object> model) throws Exception {
    Employee employee = new Employee();
    model.put("employee", employee);
    if (flag && edit) {
      return "employees/addEmployee";
    } else {
      return "error";
    }
  }

  @PostMapping(path = "/employees/add", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String handleEmployeeAdd(Map<String, Object> model, Employee employee) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

      String sql = "INSERT INTO login VALUES ('"
          + employee.getName() + "','" + employee.getPassword() + "')";

      stmt.executeUpdate(sql);
      return "redirect:/employees"; // Directly returns to employee homepage
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }


/*
  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }*/

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
