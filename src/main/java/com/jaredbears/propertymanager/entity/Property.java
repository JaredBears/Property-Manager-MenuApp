package com.jaredbears.propertymanager.entity;

import java.math.BigDecimal;

public class Property {
  private Integer propertyID;
  private Integer cityID;
  private String streetAddress;
  private BigDecimal taxes;
  private BigDecimal mortgage;
  
  public Integer getPropertyID() {
    return propertyID;
  }
  
  public void setPropertyID(Integer propertyID) {
    this.propertyID = propertyID;
  }
  
  public Integer getCityID() {
    return cityID;
  }
  
  public void setCityID(Integer cityID) {
    this.cityID = cityID;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  public BigDecimal getTaxes() {
    return taxes;
  }

  public void setTaxes(BigDecimal taxes) {
    this.taxes = taxes;
  }

  public BigDecimal getMortgage() {
    return mortgage;
  }

  public void setMortgage(BigDecimal mortgage) {
    this.mortgage = mortgage;
  }
  

}
