package com.jaredbears.propertymanager.menu;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Scanner;
import com.jaredbears.propertymanager.entity.City;
import com.jaredbears.propertymanager.entity.Employee;
import com.jaredbears.propertymanager.entity.Property;
import com.jaredbears.propertymanager.entity.State;
import com.jaredbears.propertymanager.entity.Unit;
import com.jaredbears.propertymanager.exception.DbException;
import com.jaredbears.propertymanager.service.PropertyService;

public class Controller {
  Scanner sc = new Scanner(System.in);
  PropertyService propertyService = new PropertyService();
  State curState;
  City curCity;
  Property curProperty;
  Unit curUnit;
  Employee curEmployee;

  MainMenu mainMenu = new MainMenu(this);
  PropertyMenu propertyMenu = new PropertyMenu(this);
  UnitMenu unitMenu = new UnitMenu(this);
  EmployeeMenu employeeMenu = new EmployeeMenu(this);

  public void processUserSelection(int menuLevel) {
    boolean done = false;

    while (!done) {
      try {
        int selection = getUserSelection(menuLevel);

        switch (selection) {
          case -1:
            if (menuLevel == 1) {
              done = exitMenu();
            } else {
              processUserSelection(1);
            }
            break;
          /*
           * Main Menu
           */
          case 1:
            mainMenu.selectState();
            break;
          case 2:
            mainMenu.selectCity();
            break;
          case 3:
            mainMenu.manageProperties();
            break;
          case 4:
            mainMenu.manageUnits();
            break;
          case 5:
            mainMenu.manageEmployees();
            break;
          /*
           * Property Menu
           */
          case 6:
            propertyMenu.selectProperty();
            break;
          case 7:
            propertyMenu.addProperty();
            break;
          case 8:
            propertyMenu.deleteProperty();
            break;
          /*
           * Unit Menu
           */
          case 9:
            unitMenu.selectUnit();
            break;
          case 10:
            unitMenu.addUnits();
            break;
          case 11:
            unitMenu.deleteUnit();
            break;
          case 12:
            unitMenu.lease();
            break;
          case 13:
            unitMenu.terminate();
            break;
          case 14:
            unitMenu.raiseRent();

            /*
             * Employee Menu
             */
          case 15:
            employeeMenu.hire();
            break;
          case 16:
            employeeMenu.selectEmployee();
          case 17:
            employeeMenu.fire();
            break;
          case 18:
            employeeMenu.raise();
            break;
          case 19:
            employeeMenu.addToUnit();
            break;
          case 20:
            employeeMenu.removeFromUnit();
            break;

          /*
           * All Menus
           */
          case 99:
            mainMenu.printAll();
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

  private boolean exitMenu() {
    System.out.println("\nClosing the menu.");
    System.exit(0);
    return true;
  }

  private int getUserSelection(int menuLevel) {
    printStatus();
    if (menuLevel == 2) {
      System.out.println(
          "\nThese are the available selections. Press the Enter key to return to main menu.");
      propertyMenu.getOperations().forEach(line -> System.out.println("    " + line));
    } else if (menuLevel == 3) {
      System.out.println(
          "\nThese are the available selections. Press the Enter key to return to main menu.");
      unitMenu.getOperations().forEach(line -> System.out.println("    " + line));
    } else if (menuLevel == 4) {
      System.out.println(
          "\nThese are the available selections. Press the Enter key to return to main menu.");
      employeeMenu.getOperations().forEach(line -> System.out.println("    " + line));
    } else {
      System.out.println("\nThese are the available selections. Press the Enter key to quit.");
      mainMenu.getOperations().forEach(line -> System.out.println("  " + line));
    }
    Integer input = getIntInput("\nEnter a menu selection");

    return Objects.isNull(input) ? -1 : input;
  }


  String getStringInput(String prompt) {
    System.out.print(prompt + ": ");
    String input = sc.nextLine();

    return input.isBlank() ? null : input.trim();
  }

  Integer getIntInput(String prompt) {
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

  Character getCharInput(String prompt) {
    String input = getStringInput(prompt);

    if (Objects.isNull(input)) {
      return null;
    }
    try {
      if (Character.isLetter(input.charAt(0))) {
        return input.toUpperCase().charAt(0);
      } else {
        throw new DbException(input + " is not a valid entry.");
      }
    } catch (Exception e) {
      throw new DbException(input + " is not a valid entry.");
    }
  }

  BigDecimal getDecimalInput(String prompt) {
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

  private void printStatus() {
    if (Objects.isNull(curState)) {
      System.out.println("\nYou do not have a state selected");
    } else if (Objects.isNull(curCity)) {
      System.out.println(
          "\nYou are working in State: " + curState + ". \nYou do not have a city selected.");
    } else if (Objects.isNull(curProperty)) {
      System.out.println("\nYou are working in " + curCity.getCityName() + ", "
          + curCity.getStateCode() + "\nYou do not have a property selected.");
    } else if (Objects.isNull(curUnit)) {
      System.out.println("\nYou are working with \n" + curProperty.getStreetAddress() + "\n"
          + curCity.getCityName() + ", " + curCity.getStateCode()
          + "\nYou do not have a unit selected.");
    } else {
      System.out.println("\nYou are working with \n Unit: " + curUnit.getUnitNumber() + "\n"
          + curProperty.getStreetAddress() + "\n" + curCity.getCityName() + ", "
          + curCity.getStateCode());
      System.out.println("Rental Rate: $" + curUnit.getRent());

      if (curUnit.getLeased()) {
        System.out.println("Tenant: " + curUnit.getTenant().getName());
        System.out.println("Phone: " + curUnit.getTenant().getPhone());
        System.out.println("Email: " + curUnit.getTenant().getEmail());
      }
    }

    if (Objects.isNull(curEmployee)) {
      System.out.println("\nYou do not have an employee selected.");
    } else {
      System.out.println("\nCurrent Employee: " + curEmployee.getName());
      System.out.println("Employee Phone: " + curEmployee.getPhone());
      System.out.println("Employee Email: " + curEmployee.getEmail());
      System.out.println("Employee Salary: " + curEmployee.getSalary());
    }
  }

}
