package com.jaredbears.propertymanager.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public abstract class DaoBase {
  
  protected void startTransaction(Connection conn) throws SQLException {
    conn.setAutoCommit(false);
  }
  protected void commitTransaction(Connection conn) throws SQLException {
    conn.commit();
  }
  protected void rollbackTransaction(Connection conn) throws SQLException {
    conn.rollback();
  }
  protected void setParameter(PreparedStatement stmt, int parameterIndex, Object value,
      Class<?> classType) throws SQLException {
    int sqlType = convertJavaClassToSqlType(classType);

    if(Objects.isNull(value)) {
      stmt.setNull(parameterIndex, sqlType);
    }
    else {
      switch(sqlType) {
        case Types.DECIMAL:
          stmt.setBigDecimal(parameterIndex, (BigDecimal)value);
          break;

        case Types.DOUBLE:
          stmt.setDouble(parameterIndex, (Double)value);
          break;

        case Types.INTEGER:
          stmt.setInt(parameterIndex, (Integer)value);
          break;

        case Types.OTHER:
          stmt.setObject(parameterIndex, value);
          break;

        case Types.VARCHAR:
          stmt.setString(parameterIndex, (String)value);
          break;

        default:
          throw new DaoException("Unknown parameter type: " + classType);
      }
    }
  }
  private int convertJavaClassToSqlType(Class<?> classType) {
    if(Integer.class.equals(classType)) {
      return Types.INTEGER;
    }

    if(String.class.equals(classType)) {
      return Types.VARCHAR;
    }

    if(Double.class.equals(classType)) {
      return Types.DOUBLE;
    }

    if(BigDecimal.class.equals(classType)) {
      return Types.DECIMAL;
    }

    if(LocalTime.class.equals(classType)) {
      return Types.OTHER;
    }

    throw new DaoException("Unsupported class type: " + classType.getName());
  }
  protected Integer getNextSequenceNumber(Connection conn, Integer id, String tableName,
      String idName) throws SQLException {
    String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + idName + " = ?";

    try(PreparedStatement stmt = conn.prepareStatement(sql)) {
      setParameter(stmt, 1, id, Integer.class);

      try(ResultSet rs = stmt.executeQuery()) {
        if(rs.next()) {
          return rs.getInt(1) + 1;
        }

        return 1;
      }
    }
  }
  protected Integer getLastInsertId(Connection conn, String table) throws SQLException {
    String sql = String.format("SELECT LAST_INSERT_ID() FROM %s", table);

    try(Statement stmt = conn.createStatement()) {
      try(ResultSet rs = stmt.executeQuery(sql)) {
        if(rs.next()) {
          return rs.getInt(1);
        }

        throw new SQLException("Unable to retrieve the primary key value. No result set!");
      }
    }
  }
  protected <T> T extract(ResultSet rs, Class<T> classType) {
    try {
      /* Obtain the constructor and create an object of the correct type. */
      Constructor<T> con = classType.getConstructor();
      T obj = con.newInstance();

      /* Get the list of fields and loop through them. */
      for(Field field : classType.getDeclaredFields()) {
        String colName = camelCaseToSnakeCase(field.getName());
        Class<?> fieldType = field.getType();

        /*
         * Set the field accessible flag which means that we can populate even private fields
         * without using the setter.
         */
        field.setAccessible(true);
        Object fieldValue = null;

        try {
          fieldValue = rs.getObject(colName);
        }
        catch(SQLException e) {
          /*
           * An exception caught here means that the field name isn't in the result set. Don't take
           * any action.
           */
        }

        /*
         * Only set the value in the object if there is a value with the same name in the result
         * set. This will preserve instance variables (like lists) that are assigned values when the
         * object is created.
         */
        if(Objects.nonNull(fieldValue)) {
          /*
           * Convert the following types: Time -> LocalTime, and Timestamp -> LocalDateTime.
           */
          if(fieldValue instanceof Time && fieldType.equals(LocalTime.class)) {
            fieldValue = ((Time)fieldValue).toLocalTime();
          }
          else if(fieldValue instanceof Timestamp && fieldType.equals(LocalDateTime.class)) {
            fieldValue = ((Timestamp)fieldValue).toLocalDateTime();
          }

          field.set(obj, fieldValue);
        }
      }

      return obj;

    }
    catch(Exception e) {
      throw new DaoException("Unable to create object of type " + classType.getName(), e);
    }
  }

  private String camelCaseToSnakeCase(String identifier) {
    StringBuilder nameBuilder = new StringBuilder();

    for(char ch : identifier.toCharArray()) {
      if(Character.isUpperCase(ch)) {
        nameBuilder.append('_').append(Character.toLowerCase(ch));
      }
      else {
        nameBuilder.append(ch);
      }
    }

    return nameBuilder.toString();
  }

  @SuppressWarnings("serial")
  static class DaoException extends RuntimeException {

    public DaoException(String message, Throwable cause) {
      super(message, cause);
    }

    public DaoException(String message) {
      super(message);
    }
  }
}
