package com.jaredbears.propertymanager.entity;

public class UnitEmployee {
  private Integer unitId;
  private Integer employeeId;
  
  public Integer getUnitId() {
    return unitId;
  }
  
  public void setUnitId(Integer unitId) {
    this.unitId = unitId;
  }

  public Integer getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Integer employeeId) {
    this.employeeId = employeeId;
  }
  
  @Override
  public String toString() {
    //TODO
    return null;
  }
}
