package com.jaredbears.propertymanager.entity;

import java.math.BigDecimal;
import java.util.List;

public class Property {
  private Integer propertyId;
  private Integer cityId;
  private String streetAddress;
  private BigDecimal taxes;
  private BigDecimal mortgage;
  private List<Unit> units;
  
  public Integer getPropertyId() {
    return propertyId;
  }
  
  public void setPropertyId(Integer propertyId) {
    this.propertyId = propertyId;
  }
  
  public Integer getCityId() {
    return cityId;
  }
  
  public void setCityId(Integer cityId) {
    this.cityId = cityId;
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

  public List<Unit> getUnits() {
    return units;
  }

  public void setUnits(List<Unit> units) {
    this.units = units;
  }
  
  @Override
  public String toString() {
    // TODO
    return null;
  }

}
