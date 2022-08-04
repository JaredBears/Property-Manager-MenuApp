package com.jaredbears.propertymanager.entity;

public class City {
  private Integer cityId;
  private String stateCode;
  private String cityName;

  
  public Integer getCityID() {
    return cityId;
  }
  
  public void setCityID(Integer cityID) {
    this.cityId = cityID;
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
  
  @Override
  public String toString() {
    // TODO
    return null;
  }
  
  
}
