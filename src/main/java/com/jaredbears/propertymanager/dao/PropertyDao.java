package com.jaredbears.propertymanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.jaredbears.propertymanager.entity.City;
import com.jaredbears.propertymanager.entity.State;
import com.jaredbears.propertymanager.exception.DbException;

@SuppressWarnings("unused")
public class PropertyDao extends DaoBase {
  private static final String STATE_TABLE = "state";
  private static final String CITY_TABLE = "city";
  private static final String PROPERTY_TABLE = "property";
  private static final String UNIT_TABLE = "unit";
  private static final String TENANT_TABLE = "tenant";
  private static final String EMPLOYEE_TABLE = "employee";
  private static final String UNIT_EMPLOYEE = "unit_employee";

  public Optional<City> fetchCityByID(Integer cityID) {
    String sql = "SELECT * FROM " + CITY_TABLE + " WHERE city_id = ?";

    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);
      try {
        City city = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          setParameter(stmt, 1, cityID, Integer.class);

          try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
              city = extract(rs, City.class);
            }
          }
        }

        if (Objects.nonNull(city)) {
          // city.getProperties().addAll(fetchProperties(conn, cityID));
        }
        commitTransaction(conn);
        return Optional.ofNullable(city);
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  public List<City> fetchCities(State curState, Character firstChar) {
    String sql = "SELECT * FROM " + CITY_TABLE + " WHERE state_code = ? AND LEFT(city_name, 1) = ?";

    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        setParameter(stmt, 1, curState.toString(), String.class);
        setParameter(stmt, 2, firstChar.toString(), String.class);
        try (ResultSet rs = stmt.executeQuery()) {
          List<City> cities = new LinkedList<>();

          while (rs.next()) {
            City city = new City();
            city.setCityName(rs.getString("city_name"));
            city.setStateCode(rs.getString("state_code"));
            city.setCityID(rs.getObject("city_id", Integer.class));
            cities.add(city);
          }

          return cities;
        }
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

}
