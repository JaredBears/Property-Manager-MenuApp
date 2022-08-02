package com.jaredbears.propertymanager.entity;

public class City {
  private Integer cityID;
  private Integer stateID;
  private String cityName;

  
  public Integer getCityID() {
    return cityID;
  }
  
  public void setCityID(Integer cityID) {
    this.cityID = cityID;
  }

  public Integer getStateID() {
    return stateID;
  }

  public void setStateID(Integer stateID) {
    this.stateID = stateID;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }
  
  
}
