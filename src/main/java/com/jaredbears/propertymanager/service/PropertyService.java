package com.jaredbears.propertymanager.service;

import java.util.List;
import java.util.NoSuchElementException;
import com.jaredbears.propertymanager.dao.PropertyDao;
import com.jaredbears.propertymanager.entity.City;
import com.jaredbears.propertymanager.entity.Property;
import com.jaredbears.propertymanager.entity.State;
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

}
