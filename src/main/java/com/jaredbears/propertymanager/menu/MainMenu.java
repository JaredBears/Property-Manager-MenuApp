package com.jaredbears.propertymanager.menu;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import com.jaredbears.propertymanager.entity.City;
import com.jaredbears.propertymanager.entity.State;

class MainMenu extends Menu {
  Controller controller;
  MainMenu(Controller controller) {
    this.controller = controller;
    //@formatter:off
    this.setOperations(List.of(
      "1) Select State",
      "2) Select City",
      "3) Manage properties",
      "4) Manage units",
      "5) Manage employees",
      "99) Print All Info"
      ));
    //@formatter:on
  }

  void selectState() {
    controller.curState = null;
    controller.curCity = null;
    controller.curProperty = null;
    controller.curUnit = null;

    EnumSet<State> states = EnumSet.allOf(State.class);
    states.forEach(System.out::println);
    String state = controller.getStringInput("\nEnter the two-letter state code").toUpperCase();
    try {
      controller.curState = State.valueOf(state);
    } catch (Exception e) {
      System.out.println("\nInvalid State.  Please try again.");
    }
  }

  void selectCity() {
    controller.curCity = null;
    controller.curProperty = null;
    controller.curUnit = null;

    if (Objects.isNull(controller.curState)) {
      System.out.println("\nYou must select a state first.");
      selectState();
    }
    Character firstChar =
        controller.getCharInput("\nEnter the first letter of the city you would like to work in");
    listCities(firstChar);
    Integer cityId = controller.getIntInput("\nEnter the city ID number");

    controller.curCity = controller.propertyService.fetchCityByID(cityId);
  }

  private void listCities(Character firstChar) {
    List<City> cities = controller.propertyService.fetchCities(controller.curState, firstChar);

    System.out.println("City ID)    City Name, State");
    for (City city : cities) {
      System.out
          .println(city.getCityID() + ")     " + city.getCityName() + ", " + city.getStateCode());
    }
  }

  void manageProperties() {
    controller.processUserSelection(2);
  }

  void manageUnits() {
    controller.processUserSelection(3);
  }
  
  void manageEmployees() {
    // TODO Auto-generated method stub

  }

  void printAll() {
    // TODO Auto-generated method stub

  }
}
