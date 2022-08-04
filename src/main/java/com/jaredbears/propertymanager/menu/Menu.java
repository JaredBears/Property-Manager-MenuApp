package com.jaredbears.propertymanager.menu;

import java.util.List;

abstract class Menu {
  private List<String> operations;

  List<String> getOperations() {
    return operations;
  }
  
  void setOperations(List<String> operations){
    this.operations = operations;
  }
}
