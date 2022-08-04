package com.jaredbears.propertymanager.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import com.jaredbears.propertymanager.entity.Property;

class PropertyMenu extends Menu {
  Controller controller;
  PropertyMenu(Controller controller){
    this.controller = controller;
    //@formatter:off
    this.setOperations(List.of(
      "6) Select a Property",
      "7) Add a Property",
      "8) Delete a Property",
      "99) Print All Info"
      ));
    //@formatter:on
  }

  public void selectProperty() {
    controller.curProperty = null;
    controller.curUnit = null;
    if (Objects.isNull(controller.curCity)) {
      System.out.println("You must select a city first");
      controller.mainMenu.selectCity();
    }

    listProperties();

    Integer propertyId = controller.getIntInput("\nEnter the property ID number");

    controller.curProperty = controller.propertyService.fetchPropertyByID(propertyId);
    
  }

  private void listProperties() {
    List<Property> properties = controller.propertyService.fetchProperties(controller.curCity.getCityID());

    System.out.println("Property ID)    Street Address");
    for (Property property : properties) {
      System.out.println(property.getPropertyId() + ")    " + property.getStreetAddress());
    }
    
  }

  public void addProperty() {
    controller.curProperty = null;
    controller.curUnit = null;
    if (Objects.isNull(controller.curState)) {
      System.out.println("\nYou must select a state before adding a property.");
      controller.mainMenu.selectState();
    }
    if (Objects.isNull(controller.curCity)) {
      System.out.println("\nYou must select a city before adding a property.");
      controller.mainMenu.selectCity();
    }

    String streetAddress = controller.getStringInput("Enter the street address");
    BigDecimal taxes = controller.getDecimalInput("Enter the yearly taxes for " + streetAddress);
    BigDecimal mortgage = controller.getDecimalInput("Enter the monthly mortgage for " + streetAddress);

    Property property = new Property();

    property.setCityId(controller.curCity.getCityID());
    property.setStreetAddress(streetAddress);
    property.setTaxes(taxes);
    property.setMortgage(mortgage);

    Property dbProperty = controller.propertyService.addProperty(property);
    System.out.println("You have successfully added " + dbProperty.getStreetAddress() + " to "
        + controller.curCity.getCityName());

    controller.curProperty = dbProperty;

    controller.unitMenu.addUnits();

    
  }

  public void deleteProperty() {
    if (Objects.isNull(controller.curProperty)) {
      System.out.println("\nYou do not have a property selected.");
      selectProperty();
    }

    System.out.println("\nYou are working with the following property:");
    System.out.println(controller.curProperty.toString());

    Integer input = controller.getIntInput(
        "Enter the Property ID to confirm deletion, or press Enter to return to the menu");

    if (input == controller.curProperty.getPropertyId()) {
      controller.propertyService.deleteProperty(controller.curProperty.getPropertyId());
    } else if (Objects.isNull(input)) {
      System.out.println("\nReturning to menu.");
    } else {
      System.out.println("\nInvalid entry. Returning to menu");
    }
    controller.curProperty = null;

    
  }
}
