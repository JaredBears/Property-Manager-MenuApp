package com.jaredbears.propertymanager;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.jaredbears.propertymanager.entity.City;
import com.jaredbears.propertymanager.entity.Employee;
import com.jaredbears.propertymanager.entity.Property;
import com.jaredbears.propertymanager.entity.State;
import com.jaredbears.propertymanager.entity.Unit;
import com.jaredbears.propertymanager.service.PropertyService;
import com.jaredbears.propertymanager.exception.DbException;

// @SpringBootApplication
public class PropertyManagement {

  private Scanner sc = new Scanner(System.in);
  private PropertyService propertyService = new PropertyService();
  private State curState;
  private City curCity;
  private Property curProperty;
  private Unit curUnit;
  private Employee curEmployee;

  //@formatter:off
  private List<String> operations = List.of(
      "1) Select State",
      "2) Select City",
      "3) Manage properties",
      "4) Manage units",
      "5) Manage employees"
  );
  //@formatter:on

  public static void main(String[] args) {
    // SpringApplication.run(PropertyManagement.class, args);
    new PropertyManagement().processUserSelection();
  }

  private void processUserSelection() {
    boolean done = false;

    while (!done) {
      try {
        int selection = getUserSelection();

        switch (selection) {
          case -1:
            done = exitMenu();
            break;
          case 1:
            selectState();
            break;
          case 2:
            selectCity();
            break;
          case 3:
            manageProperties();
            break;
          case 4:
            manageUnits();
            break;
          case 5:
            manageEmployees();
            break;
          default:
            System.out.println("\n" + selection + " is not a valid selection. Try again.");
            break;
        }
      } catch (Exception e) {
        System.out.println("Error: " + e + " Try Again.");
      }
    }
  }


  private void selectState() {
    curState = null;
    curCity = null;
    curProperty = null;
    curUnit = null;
    
    EnumSet<State> states = EnumSet.allOf(State.class);
    states.forEach(System.out::println);
    String state = getStringInput("\nEnter the two-letter state code");
    try {
      curState = State.valueOf(state);
    } catch(Exception e) {
      System.out.println("\nInvalid State.  Please try again.");
    }
    

  }

  private void selectCity() {
    curCity = null;
    curProperty = null;
    curUnit = null;
    
    if(Objects.isNull(curState)) {
      System.out.println("\nYou must select a state first.");
      return;
    }
    Character firstChar = getCharInput("\nEnter the first letter of the city you would like to work in");
    listCities(firstChar);
    Integer cityID = getIntInput("\nEnter the city ID number");
    
    curCity = propertyService.fetchCityByID(cityID);
  }

  private void listCities(Character firstChar) {
    List<City> cities = propertyService.fetchCities(curState, firstChar);
    
    System.out.println("City ID)    City Name, State");
    for(City city : cities) {
      System.out.println(city.getCityID() + ") " + city.getCityName() + ", " + city.getStateCode());
    }
  }

  private void manageProperties() {
    // TODO Auto-generated method stub

  }

  private void manageUnits() {
    // TODO Auto-generated method stub

  }

  private void manageEmployees() {
    // TODO Auto-generated method stub

  }

  private boolean exitMenu() {
    System.out.println("\nClosing the menu.");
    return true;
  }

  // prints the menu options
  private void printOperations() {
    System.out.println("\nThese are the available selections. Press the Enter key to quit.");
    operations.forEach(line -> System.out.println("  " + line));

    if (Objects.isNull(curState)) {
      System.out.println("\nYou do not have a state selected");
    } else if (Objects.isNull(curCity)) {
      System.out.println(
          "\nYou are working in State: " + curState + ". \nYou do not have a city selected.");
    } else if (Objects.isNull(curProperty)) {
      System.out.println("\nYou are working in " + curCity.getCityName() + ", "
          + curCity.getStateCode() + ". \nYou do not have a property selected.");
    } else if (Objects.isNull(curUnit)) {
      System.out.println("\nYou are working with \n" + curProperty.getStreetAddress() + "\n"
          + curCity.getCityName() + ", " + curCity.getStateCode()
          + "\nYou do not have a unit selected.");
    } else {
      System.out.println("\nYou are working with \n Unit: " + curUnit.getUnitNumber() + "\n"
          + curProperty.getStreetAddress() + "\n" + curCity.getCityName() + ", "
          + curCity.getStateCode());
    }
  }

  private int getUserSelection() {
    printOperations();

    Integer input = getIntInput("\nEnter a menu selection");

    return Objects.isNull(input) ? -1 : input;
  }

  // gathers user input
  private String getStringInput(String prompt) {
    System.out.print(prompt + ": ");
    String input = sc.nextLine();

    return input.isBlank() ? null : input.trim();
  }


  // gathers user input and converts to Integer
  private Integer getIntInput(String prompt) {
    String input = getStringInput(prompt);

    if (Objects.isNull(input)) {
      return null;
    }
    try {
      return Integer.valueOf(input);
    } catch (NumberFormatException e) {
      throw new DbException(input + " is not a valid number.");
    }
  }
  
  private Character getCharInput(String prompt) {
    String input = getStringInput(prompt);
    
    if(Objects.isNull(input)) {
      return null;
    }
    try {
      if(Character.isLetter(input.charAt(0))){
        return input.toUpperCase().charAt(0);
      } else {
        throw new DbException(input + " is not a valid entry.");
      }
    } catch(Exception e) {
      throw new DbException(input + " is not a valid entry.");
    }
  }

  // gathers user input and converts to BigDecimal
  private BigDecimal getDecimalInput(String prompt) {
    String input = getStringInput(prompt);

    if (Objects.isNull(input)) {
      return null;
    }
    try {
      return new BigDecimal(input).setScale(2);
    } catch (NumberFormatException e) {
      throw new DbException(input + " is not a valid decimal number.");
    }
  }



}
