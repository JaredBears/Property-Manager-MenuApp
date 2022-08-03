package com.jaredbears.propertymanager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.jaredbears.propertymanager.exception.DbException;

public class DbConnection {

  private static String HOST = "localhost";
  private static String PASSWORD = "propertymanager";
  private static int PORT = 3306;
  private static String SCHEMA = "propertymanager";
  private static String USER = "propertymanager";

  public static java.sql.Connection getConnection() {
    String uri = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", HOST, PORT, SCHEMA,
        USER, PASSWORD);
    try {
      Connection conn = DriverManager.getConnection(uri);
      System.out.println("Connection to " + SCHEMA + " is successful\n");
      return conn;
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Unable to connect to " + SCHEMA);
      throw new DbException("Unable to connect to " + SCHEMA);
    } 
  }
}
