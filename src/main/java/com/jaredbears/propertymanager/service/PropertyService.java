package com.jaredbears.propertymanager.service;

import java.util.List;
import java.util.NoSuchElementException;
import com.jaredbears.propertymanager.dao.PropertyDao;
import com.jaredbears.propertymanager.entity.City;
import com.jaredbears.propertymanager.entity.State;

public class PropertyService {
  
  private PropertyDao propertyDao = new PropertyDao();

  public City fetchCityByID(Integer cityID) {
    return propertyDao.fetchCityByID(cityID).orElseThrow(() -> new NoSuchElementException("City with City ID=" + cityID + " does not exist."));
  }

  public List<City> fetchCities(State curState, Character firstChar) {
    return propertyDao.fetchCities(curState, firstChar);
  }

}
