package com.jaredbears.propertymanager.entity;

public abstract class People {
  private Integer personId;
  private String name;
  private String phone;
  private String email;
  
  public Integer getId() {
    return personId;
  }
  
  public void setId(Integer id) {
    this.personId = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
  
  
}
