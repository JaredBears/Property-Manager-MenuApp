package com.jaredbears.propertymanager.entity;

import java.util.List;

public class City {
  private Integer cityID;
  private String stateCode;
  private String cityName;

  
  public Integer getCityID() {
    return cityID;
  }
  
  public void setCityID(Integer cityID) {
    this.cityID = cityID;
  }

  public String getStateCode() {
    return stateCode;
  }

  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public List<Property> getProperties() {
    // TODO Auto-generated method stub
    return null;
  }
  
  
}
