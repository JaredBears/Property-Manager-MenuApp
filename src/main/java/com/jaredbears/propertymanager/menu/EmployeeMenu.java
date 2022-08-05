package com.jaredbears.propertymanager.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import com.jaredbears.propertymanager.entity.Employee;

class EmployeeMenu extends Menu {
  Controller controller;

  EmployeeMenu(Controller controller) {
    this.controller = controller;
    //@formatter:off
    this.setOperations(List.of(
      "15) Hire Employee",
      "16) Select Employee",
      "17) Fire Current Employee",
      "18) Give Raise to Current Employee",
      "19) Add Current Employee to Current Unit",
      "20) Remove an Employee from Current Unit",
      "99) Print All Info"
    ));
    //@formatter:on
  }

  void hire() {
    controller.curEmployee = null;
    Employee employee = new Employee();
    String name = controller.getStringInput("Enter the Employee's name");
    String phone = controller.getStringInput("Enter the Employee's phone number");
    String email = controller.getStringInput("Enter the Employee's email");
    BigDecimal salary = controller.getDecimalInput("Enter the Employee's salary");

    employee.setName(name);
    employee.setPhone(phone);
    employee.setEmail(email);
    employee.setSalary(salary);
    employee.setId(controller.propertyService.addEmployee(employee));

    controller.curEmployee = employee;
  }
  
  void selectEmployee() {
    controller.curEmployee = null;
    listEmployees();

    Integer employeeID = controller.getIntInput("Enter the Employee ID");

    controller.curEmployee = controller.propertyService.fetchEmployeeByID(employeeID);

  }

  private void listEmployees() {
    System.out.println("Employee ID)  Employee Name");
    for (Employee employee : controller.propertyService.fetchEmployees()) {
      System.out.println(employee.getId() + ")    " + employee.getName());
      System.out.println("   Responsible for Units: ");
      // TODO
    }
  }

  void fire() {
    if (Objects.isNull(controller.curEmployee)) {
      System.out.println("\nYou do not have an Employee selected.");
      selectEmployee();
    }

    System.out.println("\nYou are working with the following employee:");
    System.out.println(controller.curEmployee.toString());

    Integer input = controller
        .getIntInput("Enter the Employee ID to confirm deletion, or press Enter to return to the menu");

    if (input == controller.curEmployee.getId()) {
      controller.propertyService.deleteEmployee(controller.curEmployee.getId());
    } else if (Objects.isNull(input)) {
      System.out.println("\nReturning to menu.");
    } else {
      System.out.println("\nInvalid entry. Returning to menu.");
    }
    controller.curEmployee = null;

  }

  void raise() {
    System.out.println("\nThe Employee's current salary is: $" + controller.curEmployee.getSalary());
    BigDecimal raise = controller.getDecimalInput("By how much would you like to raise the salary");
    raise.add(controller.curEmployee.getSalary());
    controller.curEmployee.setSalary(raise);
    controller.propertyService.updateEmployee(controller.curEmployee);
  }

  void addToUnit() {
    // TODO Auto-generated method stub

  }

  void removeFromUnit() {
    // TODO Auto-generated method stub

  }

}
