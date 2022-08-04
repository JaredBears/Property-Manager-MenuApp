package com.jaredbears.propertymanager.menu;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import com.jaredbears.propertymanager.entity.Tenant;
import com.jaredbears.propertymanager.entity.Unit;

class UnitMenu extends Menu {
  Controller controller;

  UnitMenu(Controller controller) {
    this.controller = controller;
    //@formatter:off
    this.setOperations(List.of(
      "9) Select a Unit",
      "10) Add Unit(s) to Property",
      "11) Delete Unit from Property",
      "12) Lease to Tenant",
      "13) Terminate Lease",
      "99) Print All Info"
    ));
    //@formatter:on
  }

  void selectUnit() {
    controller.curUnit = null;
    if (Objects.isNull(controller.curProperty)) {
      System.out.println("\nYou must select a property.");
      controller.propertyMenu.selectProperty();
    }
    listUnits();

    Integer unitID = controller.getIntInput("Enter the Unit ID");

    controller.curUnit = controller.propertyService.fetchUnitByID(unitID);

  }

  void listUnits() {
    System.out.println("Unit ID)  Unit Number");
    for (Unit unit : controller.curProperty.getUnits()) {
      System.out.println(unit.getUnitId() + ")    " + unit.getUnitNumber());
      if (unit.getLeased()) {
        System.out.println("        Tenant: " + unit.getTenant().getName());
      }
    }
  }

  void addUnits() {
    controller.curUnit = null;
    List<Unit> units = new LinkedList<>();
    Integer loop = controller.getIntInput("How many units to add");
    if (Objects.isNull(controller.curProperty)) {
      System.out.println("You must select a property first.");
      controller.propertyMenu.selectProperty();
    }
    for (int i = 0; i < loop; i++) {
      Unit unit = new Unit();
      Integer propertyID = controller.curProperty.getPropertyId();
      String unitNumber = controller.getStringInput("Enter the Unit Number");
      BigDecimal rent = controller.getDecimalInput("Enter the monthly rent");

      unit.setPropertyId(propertyID);
      unit.setUnitNumber(unitNumber);
      unit.setRent(rent);
      unit.setLeased(false);
      unit.setTenant(null);
      unit.setUnitId(controller.propertyService.addUnit(unit));
      units.add(unit);
    }
    controller.curProperty.setUnits(units);

  }

  void deleteUnit() {
    if (Objects.isNull(controller.curUnit)) {
      System.out.println("\nYou do not have a Unit selected.");
      selectUnit();
    }

    System.out.println("\nYou are working with the following unit:");
    System.out.println(controller.curUnit.toString());

    Integer input = controller
        .getIntInput("Enter the Unit ID to confirm deletion, or press Enter to return to the menu");

    if (input == controller.curUnit.getUnitId()) {
      controller.propertyService.deleteUnit(controller.curUnit.getUnitId());
    } else if (Objects.isNull(input)) {
      System.out.println("\nReturning to menu.");
    } else {
      System.out.println("\nInvalid entry. Returning to menu.");
    }
    controller.curProperty.getUnits().remove(controller.curUnit);
    controller.curUnit = null;

  }

  void lease() {
    if (Objects.isNull(controller.curUnit)) {
      System.out.println("You must select a unit first.");
      selectUnit();
    }

    if (controller.curUnit.getLeased()) {
      System.out
          .println("This unit is already leased.  You must terminate the existing lease first.");
    } else {
      Tenant tenant = new Tenant();
      String name = controller.getStringInput("Enter the Tenant's name");
      String phone = controller.getStringInput("Enter the Tenant's phone number");
      String email = controller.getStringInput("Enter the Tenant's email");

      tenant.setUnitId(controller.curUnit.getUnitId());
      tenant.setName(name);
      tenant.setPhone(phone);
      tenant.setEmail(email);

      controller.curUnit.setLeased(true);
      controller.curUnit.setTenant(tenant);

      controller.propertyService.updateUnit(controller.curUnit);
      controller.propertyService.addTenant(tenant);
    }
  }

  void terminate() {
    if (Objects.isNull(controller.curUnit)) {
      System.out.println("\nYou do not have a Unit selected.");
      selectUnit();
    }
    if (!controller.curUnit.getLeased()) {
      System.out.println("\nThis unit is not currently leased.");
      return;
    }
    System.out.println("\nYou are working with the following unit:");
    System.out.println(controller.curUnit.toString());

    Integer input = controller.getIntInput(
        "Enter the Unit ID to confirm lease termination, or press Enter to return to the menu");

    if (input == controller.curUnit.getUnitId()) {
      controller.curUnit.setLeased(false);
      controller.propertyService.terminateTenant(controller.curUnit.getTenant());
      controller.curUnit.setTenant(null);
      controller.propertyService.updateUnit(controller.curUnit);
    } else if (Objects.isNull(input)) {
      System.out.println("\nReturning to menu.");
    } else {
      System.out.println("\nInvalid entry. Returning to menu.");
    }
  }
}
