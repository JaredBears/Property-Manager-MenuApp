package com.jaredbears.propertymanager;

import com.jaredbears.propertymanager.menu.Controller;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
public class PropertyManagement {

  public static void main(String[] args) {
    // SpringApplication.run(PropertyManagement.class, args);
    new Controller().processUserSelection(1);
  }

}
