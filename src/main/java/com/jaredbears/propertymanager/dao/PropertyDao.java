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
              // city = extract(rs, City.class);
              city = new City();
              city.setCityName(rs.getString("city_name"));
              city.setStateCode(rs.getString("state_code"));
              city.setCityID(rs.getInt("city_id"));
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
            // cities.add(extract(rs, City.class));
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
            // properties.add(extract(rs, Property.class));
            Property property = new Property();
            property.setStreetAddress(rs.getString("street_address"));
            property.setMortgage(rs.getBigDecimal("mortgage"));
            property.setTaxes(rs.getBigDecimal("taxes"));
            property.setPropertyId(rs.getInt("property_id"));
            property.setCityId(cityID);
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

  public Optional<Property> fetchPropertyByID(Integer propertyId) {
    String sql = "SELECT * FROM " + PROPERTY_TABLE + " WHERE property_id = ?";

    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);
      try {
        Property property = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          setParameter(stmt, 1, propertyId, Integer.class);

          try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
              // property = extract(rs, Property.class);
              property = new Property();
              property.setStreetAddress(rs.getString("street_address"));
              property.setMortgage(rs.getBigDecimal("mortgage"));
              property.setTaxes(rs.getBigDecimal("taxes"));
              property.setPropertyId(propertyId);
              property.setCityId(rs.getInt("city_id"));
            }
          }
        }


        if (Objects.nonNull(property)) {
          property.setUnits(fetchUnitsForProperty(conn, propertyId));
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

  private List<Unit> fetchUnitsForProperty(Connection conn, Integer propertyId) {
    String sql = "SELECT * FROM " + UNIT_TABLE + " WHERE property_id = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      setParameter(stmt, 1, propertyId, Integer.class);
      try (ResultSet rs = stmt.executeQuery()) {
        List<Unit> units = new LinkedList<>();

        while (rs.next()) {
          // Unit unit = extract(rs, Unit.class);
          Unit unit = new Unit();
          unit.setLeased(rs.getBoolean("leased"));
          unit.setPropertyId(propertyId);
          unit.setRent(rs.getBigDecimal("rent"));
          unit.setUnitId(rs.getInt("unit_id"));
          unit.setUnitNumber(rs.getString("unit_number"));
          if (unit.getLeased()) {
            unit.setTenant(fetchTenantForUnit(conn, unit.getUnitId()));
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

        if (rs.next()) {
          //tenant = extract(rs, Tenant.class);
          tenant = new Tenant();
          tenant.setId(rs.getInt("people_id"));
          tenant.setUnitId(unitID);
          tenant.setName(rs.getString("name"));
          tenant.setPhone(rs.getString("phone"));
          tenant.setEmail(rs.getString("email"));;
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
      +"(city_id, street_address, taxes, mortgage) "
      +"VALUES "
      +"(?, ?, ?, ?)";
    // @formatter:on
    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareCall(sql)) {
        setParameter(stmt, 1, property.getCityId(), Integer.class);
        setParameter(stmt, 2, property.getStreetAddress(), String.class);
        setParameter(stmt, 3, property.getTaxes(), BigDecimal.class);
        setParameter(stmt, 4, property.getMortgage(), BigDecimal.class);

        stmt.executeUpdate();

        Integer propertyID = getLastInsertId(conn, PROPERTY_TABLE);
        commitTransaction(conn);

        property.setPropertyId(propertyID);
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
      +"(property_id, unit_number, rent, leased) "
      +"VALUES "
      +"(?, ?, ?, ?)";
    // @formatter:on
    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);

      try (PreparedStatement stmt = conn.prepareCall(sql)) {
        setParameter(stmt, 1, unit.getPropertyId(), Integer.class);
        setParameter(stmt, 2, unit.getUnitNumber(), String.class);
        setParameter(stmt, 3, unit.getRent(), BigDecimal.class);
        if (unit.getLeased()) {
          setParameter(stmt, 4, 1, Integer.class);
        } else {
          setParameter(stmt, 4, 0, Integer.class);
        }


        stmt.executeUpdate();

        Integer unitID = getLastInsertId(conn, UNIT_TABLE);
        commitTransaction(conn);

        unit.setUnitId(unitID);
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
    String sql = "SELECT * FROM " + UNIT_TABLE + " WHERE unit_id = ?";

    try (Connection conn = DbConnection.getConnection()) {
      startTransaction(conn);
      try {
        Unit unit = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
          setParameter(stmt, 1, unitID, Integer.class);

          try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
              // unit = extract(rs, Unit.class);
              unit = new Unit();
              unit.setLeased(rs.getBoolean("leased"));
              unit.setPropertyId(rs.getInt("property_id"));
              unit.setRent(rs.getBigDecimal("rent"));
              unit.setUnitId(rs.getInt("unit_id"));
              unit.setUnitNumber(rs.getString("unit_number"));
              if (unit.getLeased()) {
                setParameter(stmt, 4, 1, Integer.class);
              } else {
                setParameter(stmt, 4, 0, Integer.class);
              }
            }
          }
        }

        commitTransaction(conn);
        return Optional.ofNullable(unit);
      } catch (Exception e) {
        rollbackTransaction(conn);
        throw new DbException(e);
      }
    } catch (SQLException e) {
      throw new DbException(e);
    }
  }

  public boolean deleteUnit(Integer unitID) {
    // TODO Auto-generated method stub
    return false;
  }

  public Unit updateUnit(Unit curUnit) {
    // TODO Auto-generated method stub
    return null;
  }

  public Integer addTenant(Tenant tenant) {
    // TODO Auto-generated method stub
    return null;
  }

  public void terminateTenant(Integer unitID) {
    // TODO Auto-generated method stub

  }

}
