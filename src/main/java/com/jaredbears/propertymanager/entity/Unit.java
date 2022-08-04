package com.jaredbears.propertymanager.entity;

import java.math.BigDecimal;

public class Unit {
  private Integer unitID;
  private Integer propertyID;
  private String unitNumber;
  private BigDecimal rent;
  private Boolean leased;
  private Tenant tenant;
  
  public Integer getUnitID() {
    return unitID;
  }
  
  public void setUnitID(Integer unitID) {
    this.unitID = unitID;
  }

  public Integer getPropertyID() {
    return propertyID;
  }

  public void setPropertyID(Integer propertyID) {
    this.propertyID = propertyID;
  }

  public String getUnitNumber() {
    return unitNumber;
  }

  public void setUnitNumber(String unitNumber) {
    this.unitNumber = unitNumber;
  }

  public BigDecimal getRent() {
    return rent;
  }

  public void setRent(BigDecimal rent) {
    this.rent = rent;
  }

  public Boolean getLeased() {
    return leased;
  }

  public void setLeased(Boolean leased) {
    this.leased = leased;
  }

  public Tenant getTenant() {
    return tenant;
  }

  public void setTenant(Tenant tenant) {
    this.tenant = tenant;
  }

  
}
