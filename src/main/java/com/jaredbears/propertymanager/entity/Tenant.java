package com.jaredbears.propertymanager.entity;

public class Tenant extends People {
  private Integer unitId;

  public Integer getUnitId() {
    return unitId;
  }

  public void setUnitId(Integer unitId) {
    this.unitId = unitId;
  }
  
  @Override
  public String toString() {
    // TODO
    return null;
  }

}
