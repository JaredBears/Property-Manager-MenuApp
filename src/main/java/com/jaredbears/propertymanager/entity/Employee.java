package com.jaredbears.propertymanager.entity;

import java.math.BigDecimal;

public class Employee extends People {
  private BigDecimal salary;

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }
  
  @Override
  public String toString() {
    // TODO
    return null;
  }
}
