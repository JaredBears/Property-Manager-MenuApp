package com.jaredbears.propertymanager.entity;

import java.math.BigDecimal;

public class Unit {
  private Integer unitId;
  private Integer propertyId;
  private String unitNumber;
  private BigDecimal rent;
  private Boolean leased;
  private Tenant tenant;
  
  public Integer getUnitId() {
    return unitId;
  }
  
  public void setUnitId(Integer unitID) {
    this.unitId = unitID;
  }

  public Integer getPropertyId() {
    return propertyId;
  }

  public void setPropertyId(Integer propertyID) {
    this.propertyId = propertyID;
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

  @Override
  public String toString() {
    // TODO
    return null;
  }
  
}
