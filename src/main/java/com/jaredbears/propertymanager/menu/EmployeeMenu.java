package com.jaredbears.propertymanager.menu;

import java.util.List;

class EmployeeMenu extends Menu {
  Controller controller;
  EmployeeMenu(Controller controller){
    this.controller = controller;
    //@formatter:off
    this.setOperations(List.of(
      "14) ",
      "99) Print All Info"
    ));
    //@formatter:on
  }

}
