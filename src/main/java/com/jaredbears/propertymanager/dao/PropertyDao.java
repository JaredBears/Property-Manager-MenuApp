package com.jaredbears.propertymanager.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.jaredbears.propertymanager.entity.City;
import com.jaredbears.propertymanager.entity.Property;
import com.jaredbears.propertymanager.entity.State;
import com.jaredbears.propertymanager.entity.Tenant;
import com.jaredbears.propertymanager.entity.Unit;
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
              city = new City();
              city.setCityID(rs.getInt("city_id"));
              city.setCityName(rs.getString("city_name"));
              city.setStateCode(rs.getString("state_code"));
            }
          }
        }

        /*
         * if (Objects.nonNull(city)) { city.getProperties().addAll(fetchProperties(conn, cityID));
         * }
         */
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
    // @formatter:off
    String sql = ""
      + "SELECT * FROM " + CITY_TABLE + " "
      + "WHERE state_code = ? AND LEFT(city_name, 1) = ? "
      + "ORDER BY city_name";
    // @formatter:on
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
            city.setCityID(rs.getInt("city_id"));
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

  public Boolean deleteProperty(Integer propertyID) {
    String sql = "DELETE FROM " + PROPERTY_TABLE + " WHERE property_id = ?";
    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        setParameter(stmt, 1, propertyID, Integer.class);

        boolean deleted = stmt.executeUpdate() == 1;
        commitTransaction(conn);

        return deleted;

      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  public List<Property> fetchProperties(Integer cityID) {
    // @formatter:off
    String sql = ""
      + "SELECT * FROM " + PROPERTY_TABLE + " "
      + "WHERE city_id = ? "
      + "ORDER BY street_address";
    // @formatter:on
    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        setParameter(stmt, 1, cityID, Integer.class);
        try (ResultSet rs = stmt.executeQuery()) {
          List<Property> properties = new LinkedList<>();

          while (rs.next()) {
            Property property = new Property();
            property.setStreetAddress(rs.getString("street_address"));
            property.setMortgage(rs.getBigDecimal("monthly_mortgage"));
            property.setTaxes(rs.getBigDecimal("yearly_taxes"));
            property.setPropertyID(rs.getInt("property_id"));
            property.setCityID(rs.getInt("city_id"));
            properties.add(property);
          }

          return properties;
        }
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  public Optional<Property> fetchPropertyByID(Integer propertyID) {
    String sql = "SELECT * FROM " + PROPERTY_TABLE + " WHERE property_id = ?";

    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);
      try {
        Property property = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          setParameter(stmt, 1, propertyID, Integer.class);

          try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
              property = new Property();
              property.setPropertyID(rs.getInt("property_id"));
              property.setStreetAddress(rs.getString("street_address"));
              property.setTaxes(rs.getBigDecimal("yearly_taxes"));
              property.setMortgage(rs.getBigDecimal("monthly_mortgage"));
            }
          }
        }


        if (Objects.nonNull(property)) {
          property.setUnits(fetchUnitsForProperty(conn, propertyID));
        }

        commitTransaction(conn);
        return Optional.ofNullable(property);
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  private List<Unit> fetchUnitsForProperty(Connection conn, Integer propertyID) {
    String sql = "SELECT * FROM " + UNIT_TABLE + " WHERE property_id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      setParameter(stmt, 1, propertyID, Integer.class);
      try (ResultSet rs = stmt.executeQuery()) {
        List<Unit> units = new LinkedList<>();

        while (rs.next()) {
          Unit unit = new Unit();
          unit.setLeased(rs.getBoolean("leased"));
          unit.setPropertyID(rs.getInt("property_id"));
          unit.setRent(rs.getBigDecimal("monthly_rent"));
          unit.setUnitID(rs.getInt("unit_id"));
          unit.setUnitNumber(rs.getString("unit_number"));
          if (unit.getLeased()) {
            unit.setTenant(fetchTenantForUnit(conn, unit.getUnitID()));
          }
          units.add(unit);
        }

        return units;
      }
    } catch (Exception e) {
      throw new DbException(e);
    }
  }

  public Tenant fetchTenantForUnit(Connection conn, Integer unitID) {
    String sql = "SELECT * FROM " + TENANT_TABLE + " WHERE unit_id = ?";
    
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      setParameter(stmt, 1, unitID, Integer.class);
      try (ResultSet rs = stmt.executeQuery()) {
        Tenant tenant = null;

        if(rs.next()) {
          tenant = new Tenant();
          tenant.setID(rs.getInt("tenant_id"));
          tenant.setUnitID(unitID);
          tenant.setName(rs.getString("tenant_name"));
          tenant.setPhone(rs.getString("tenant_phone"));
          tenant.setEmail(rs.getString("tenant_email"));;
        }
        
        return tenant;

      }
    } catch (Exception e) {
      throw new DbException(e);
    }
  }

  public Property insertProperty(Property property) {
    // @formatter:off
    String sql = "" 
      +"INSERT INTO " + PROPERTY_TABLE + " "
      +"(city_id, street_address, yearly_taxes, monthly_mortgage) "
      +"VALUES "
      +"(?, ?, ?, ?)";
    // @formatter:on
    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareCall(sql)) {
        setParameter(stmt, 1, property.getCityID(), Integer.class);
        setParameter(stmt, 2, property.getStreetAddress(), String.class);
        setParameter(stmt, 3, property.getTaxes(), BigDecimal.class);
        setParameter(stmt, 4, property.getMortgage(), BigDecimal.class);

        stmt.executeUpdate();

        Integer propertyID = getLastInsertId(conn, PROPERTY_TABLE);
        commitTransaction(conn);

        property.setPropertyID(propertyID);
        return property;
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  public Integer insertUnit(Unit unit) {
    // @formatter:off
    String sql = "" 
      +"INSERT INTO " + UNIT_TABLE + " "
      +"(property_id, unit_number, monthly_rent, leased) "
      +"VALUES "
      +"(?, ?, ?, ?)";
    // @formatter:on
    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareCall(sql)) {
        setParameter(stmt, 1, unit.getPropertyID(), Integer.class);
        setParameter(stmt, 2, unit.getUnitNumber(), String.class);
        setParameter(stmt, 3, unit.getRent(), BigDecimal.class);
        if(unit.getLeased()) {
          setParameter(stmt, 4, 1, Integer.class);
        } else {
          setParameter(stmt, 4, 0, Integer.class);
        }
        

        stmt.executeUpdate();

        Integer unitID = getLastInsertId(conn, UNIT_TABLE);
        commitTransaction(conn);

        unit.setUnitID(unitID);
        return unitID;
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  public Optional<Unit> fetchUnitByID(Integer unitID) {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean deleteUnit(Integer unitID) {
    // TODO Auto-generated method stub
    return false;
  }

  public void updateUnit(Unit curUnit) {
    // TODO Auto-generated method stub

  }

  public void addTenant(Tenant tenant) {
    // TODO Auto-generated method stub

  }

  public void terminateTenant(Integer unitID) {
    // TODO Auto-generated method stub

  }

}
