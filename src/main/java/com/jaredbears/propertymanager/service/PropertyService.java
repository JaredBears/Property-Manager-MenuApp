package com.jaredbears.propertymanager.service;

import java.util.List;
import java.util.NoSuchElementException;
import com.jaredbears.propertymanager.dao.PropertyDao;
import com.jaredbears.propertymanager.entity.*;
import com.jaredbears.propertymanager.exception.DbException;

public class PropertyService {
  
  private PropertyDao propertyDao = new PropertyDao();

  public City fetchCityByID(Integer cityID) {
    return propertyDao.fetchCityByID(cityID).orElseThrow(() -> new NoSuchElementException("City with City ID=" + cityID + " does not exist."));
  }

  public List<City> fetchCities(State curState, Character firstChar) {
    return propertyDao.fetchCities(curState, firstChar);
  }

  public Property addProperty(Property property) {
    return propertyDao.insertProperty(property);
  }

  public void deleteProperty(Integer propertyID) {
    if(!propertyDao.deleteProperty(propertyID)) {
      throw new DbException("Property with Property ID =" + propertyID + " does not exist."); 
    }
  }

  public List<Property> fetchProperties(Integer cityID) {
    return propertyDao.fetchProperties(cityID);
  }

  public Property fetchPropertyByID(Integer propertyID) {
    return propertyDao.fetchPropertyByID(propertyID).orElseThrow(() -> new NoSuchElementException("Property with Property ID =" + propertyID + " does not exist."));
  }
  
  public Unit fetchUnitByID(Integer unitID) {
    return propertyDao.fetchUnitByID(unitID).orElseThrow(() -> new NoSuchElementException("Unit with Unit ID=" + unitID + "does not exist"));
  }
  
  public Integer addUnit(Unit unit) {
    return propertyDao.insertUnit(unit);
  }

  public void deleteUnit(Integer unitID) {
    if(!propertyDao.deleteUnit(unitID)) {
      throw new DbException("Property with Unit ID=" + unitID + " does not exist.");
    }
    
  }

  public Unit updateUnit(Unit curUnit) {
    return propertyDao.updateUnit(curUnit);
    
  }

  public Integer addTenant(Tenant tenant) {
    return propertyDao.insertTenant(tenant);
    
  }

  public void terminateTenant(Tenant tenant) {
    propertyDao.deleteTenant(tenant.getId());
    
  }

  public Integer addEmployee(Employee employee) {
    // TODO Auto-generated method stub
    return null;
  }

  public void deleteEmployee(Integer id) {
    // TODO Auto-generated method stub
    
  }

  public Employee fetchEmployeeByID(Integer employeeID) {
    // TODO Auto-generated method stub
    return null;
  }

  public List<Employee> fetchEmployees() {
    // TODO Auto-generated method stub
    return null;
  }

  public Employee updateEmployee(Employee curEmployee) {
    // TODO Auto-generated method stub
    return null;
  }

}
