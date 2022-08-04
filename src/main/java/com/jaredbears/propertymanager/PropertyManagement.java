package com.jaredbears.propertymanager;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.jaredbears.propertymanager.entity.City;
import com.jaredbears.propertymanager.entity.Employee;
import com.jaredbears.propertymanager.entity.Property;
import com.jaredbears.propertymanager.entity.State;
import com.jaredbears.propertymanager.entity.Tenant;
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
  private List<String> propMenu = List.of(
    "6) Select a Property",
    "7) Add a Property",
    "8) Delete a Property"
  );
  private List<String> unitMenu = List.of(
    "9) Select a Unit",
    "10) Add Unit(s) to Property",
    "11) Delete Unit from Property",
    "12) Lease to Tenant",
    "13) Terminate Lease"
  );
  //@formatter:on

  public static void main(String[] args) {
    // SpringApplication.run(PropertyManagement.class, args);
    new PropertyManagement().processUserSelection(1);
  }

  private void processUserSelection(int menuLevel) {
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
          /*
           * Property Menu
           */
          case 6:
            selectProperty();
            break;
          case 7:
            addProperty();
            break;
          case 8:
            deleteProperty();
            break;
          /*
           * Unit Menu
           */
          case 9:
            selectUnit();
            break;
          case 10:
            addUnits();
            break;
          case 11:
            deleteUnit();
            break;
          case 12:
            lease();
            break;
          case 13:
            terminate();
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
    String state = getStringInput("\nEnter the two-letter state code").toUpperCase();
    try {
      curState = State.valueOf(state);
    } catch (Exception e) {
      System.out.println("\nInvalid State.  Please try again.");
    }


  }

  private void selectCity() {
    curCity = null;
    curProperty = null;
    curUnit = null;

    if (Objects.isNull(curState)) {
      System.out.println("\nYou must select a state first.");
      selectState();
    }
    Character firstChar =
        getCharInput("\nEnter the first letter of the city you would like to work in");
    listCities(firstChar);
    Integer cityID = getIntInput("\nEnter the city ID number");

    curCity = propertyService.fetchCityByID(cityID);
  }

  private void listCities(Character firstChar) {
    List<City> cities = propertyService.fetchCities(curState, firstChar);

    System.out.println("City ID)    City Name, State");
    for (City city : cities) {
      System.out
          .println(city.getCityID() + ")     " + city.getCityName() + ", " + city.getStateCode());
    }
  }

  private void manageProperties() {
    processUserSelection(2);

  }

  private void selectProperty() {
    curProperty = null;
    curUnit = null;

    if (Objects.isNull(curState)) {
      System.out.println("You must select a state first");
      selectState();
    }
    if (Objects.isNull(curCity)) {
      System.out.println("You must select a city first");
      selectCity();
    }

    listProperties();

    Integer propertyID = getIntInput("\nEnter the property ID number");

    curProperty = propertyService.fetchPropertyByID(propertyID);
  }

  private void listProperties() {
    List<Property> properties = propertyService.fetchProperties(curCity.getCityID());

    System.out.println("Property ID)    Street Address");
    for (Property property : properties) {
      System.out.println(property.getPropertyID() + ")    " + property.getStreetAddress());
    }
  }

  private void addProperty() {
    curProperty = null;
    curUnit = null;
    if (Objects.isNull(curState)) {
      System.out.println("\nYou must select a state before adding a property.");
      selectState();
    }
    if (Objects.isNull(curCity)) {
      System.out.println("\nYou must select a city before adding a property.");
      selectCity();
    }

    String streetAddress = getStringInput("Enter the street address");
    BigDecimal taxes = getDecimalInput("Enter the yearly taxes for " + streetAddress);
    BigDecimal mortgage = getDecimalInput("Enter the monthly mortgage for " + streetAddress);

    Property property = new Property();

    property.setCityID(curCity.getCityID());
    property.setStreetAddress(streetAddress);
    property.setTaxes(taxes);
    property.setMortgage(mortgage);

    Property dbProperty = propertyService.addProperty(property);
    System.out.println("You have successfully added " + dbProperty.getStreetAddress() + " to "
        + curCity.getCityName());

    curProperty = dbProperty;

    addUnits();
  }

  private void deleteProperty() {
    if (Objects.isNull(curProperty)) {
      System.out.println("\nYou do not have a property selected.");
      selectProperty();
    }

    System.out.println("\nYou are working with the following property:");
    System.out.println("Property ID: " + curProperty.getPropertyID());
    System.out.println(curProperty.getStreetAddress());
    System.out.println(curCity.getCityName() + ", " + curCity.getStateCode());

    Integer input = getIntInput(
        "Enter the Property ID to confirm deletion, or press Enter to return to the menu");

    if (input == curProperty.getPropertyID()) {
      propertyService.deleteProperty(curProperty.getPropertyID());
    } else if (Objects.isNull(input)) {
      System.out.println("\nReturning to menu.");
    } else {
      System.out.println("\nInvalid entry. Returning to menu");
    }
    curProperty = null;
  }

  private void manageUnits() {
    processUserSelection(3);
  }

  private void selectUnit() {
    curUnit = null;
    if (Objects.isNull(curProperty)) {
      System.out.println("\nYou must select a property.");
      selectProperty();
    }
    listUnits();

    Integer unitID = getIntInput("Enter the Unit ID");

    curUnit = propertyService.fetchUnitByID(unitID);

  }

  private void listUnits() {
    System.out.println("Unit ID)  Unit Number");
    for (Unit unit : curProperty.getUnits()) {
      System.out.println(unit.getUnitID() + ")    " + unit.getUnitNumber());
      if (unit.getLeased()) {
        System.out.println("        Tenant: " + unit.getTenant().getName());
      }
    }
  }


  private void addUnits() {
    curUnit = null;
    List<Unit> units = new LinkedList<>();
    Integer loop = getIntInput("How many units to add");
    if (Objects.isNull(curProperty)) {
      System.out.println("You must select a property first.");
      selectProperty();
    }
    for (int i = 0; i < loop; i++) {
      Unit unit = new Unit();
      Integer propertyID = curProperty.getPropertyID();
      String unitNumber = getStringInput("Enter the Unit Number");
      BigDecimal rent = getDecimalInput("Enter the monthly rent");

      unit.setPropertyID(propertyID);
      unit.setUnitNumber(unitNumber);
      unit.setRent(rent);
      unit.setLeased(false);
      unit.setTenant(null);
      unit.setUnitID(propertyService.addUnit(unit));
      units.add(unit);
    }
    curProperty.setUnits(units);
  }

  private void deleteUnit() {
    if (Objects.isNull(curUnit)) {
      System.out.println("\nYou do not have a Unit selected.");
      selectUnit();
    }

    System.out.println("\nYou are working with the following unit:");
    System.out.println("Unit ID: " + curUnit.getUnitID());
    System.out.println(curUnit.getUnitNumber() + " - " + curProperty.getStreetAddress());
    System.out.println(curCity.getCityName() + ", " + curCity.getStateCode());

    Integer input =
        getIntInput("Enter the Unit ID to confirm deletion, or press Enter to return to the menu");

    if (input == curUnit.getUnitID()) {
      propertyService.deleteUnit(curUnit.getUnitID());
    } else if (Objects.isNull(input)) {
      System.out.println("\nReturning to menu.");
    } else {
      System.out.println("\nInvalid entry. Returning to menu.");
    }
    curProperty.getUnits().remove(curUnit);
    curUnit = null;
  }

  private void lease() {
    if (Objects.isNull(curUnit)) {
      System.out.println("You must select a unit first.");
      selectUnit();
    }

    if (curUnit.getLeased()) {
      System.out
          .println("This unit is already leased.  You must terminate the existing lease first.");
    } else {
      Tenant tenant = new Tenant();
      String name = getStringInput("Enter the Tenant's name");
      String phone = getStringInput("Enter the Tenant's phone number");
      String email = getStringInput("Enter the Tenant's email");

      tenant.setUnitID(curUnit.getUnitID());
      tenant.setName(name);
      tenant.setPhone(phone);
      tenant.setEmail(email);

      curUnit.setLeased(true);

      propertyService.updateUnit(curUnit);
      propertyService.addTenant(tenant);
    }
  }

  private void terminate() {
    if (Objects.isNull(curUnit)) {
      System.out.println("\nYou do not have a Unit selected.");
      selectUnit();
    }

    System.out.println("\nYou are working with the following unit:");
    System.out.println("Unit ID: " + curUnit.getUnitID());
    System.out.println(curUnit.getUnitNumber() + " - " + curProperty.getStreetAddress());
    System.out.println(curCity.getCityName() + ", " + curCity.getStateCode());

    Integer input = getIntInput(
        "Enter the Unit ID to confirm lease termination, or press Enter to return to the menu");

    if (input == curUnit.getUnitID()) {
      curUnit.setLeased(false);
      propertyService.updateUnit(curUnit);
      propertyService.terminateTenant(curUnit.getUnitID());
    } else if (Objects.isNull(input)) {
      System.out.println("\nReturning to menu.");
    } else {
      System.out.println("\nInvalid entry. Returning to menu.");
    }
    curUnit = null;
  }

  private void manageEmployees() {
    // TODO Auto-generated method stub

  }

  private boolean exitMenu() {
    System.out.println("\nClosing the menu.");
    System.exit(0);
    return true;
  }

  // prints the menu options
  private void printOperations() {
    System.out.println("\nThese are the available selections. Press the Enter key to quit.");
    operations.forEach(line -> System.out.println("  " + line));
  }

  private void printPropMenu() {
    System.out.println(
        "\nThese are the available selections. Press the Enter key to return to main menu.");
    propMenu.forEach(line -> System.out.println("    " + line));
  }

  private void printUnitMenu() {
    System.out.println(
        "\nThese are the available selections. Press the Enter key to return to main menu.");
    unitMenu.forEach(line -> System.out.println("    " + line));
  }

  private int getUserSelection(int menuLevel) {
    printStatus();
    if (menuLevel == 2) {
      printPropMenu();
    } else if (menuLevel == 3) {
      printUnitMenu();
    } else {
      printOperations();
    }
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
