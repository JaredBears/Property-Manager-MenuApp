package com.jaredbears.propertymanager;

import java.util.List;
import java.util.Scanner;
import com.jaredbears.propertymanager.entity.City;
import com.jaredbears.propertymanager.entity.Employee;
import com.jaredbears.propertymanager.entity.Property;
import com.jaredbears.propertymanager.entity.State;
import com.jaredbears.propertymanager.entity.Unit;
import com.jaredbears.propertymanager.service.PropertyService;

public class Controller {
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
    "5) Manage employees",
    "99) Print All Info"
  );
  private List<String> propMenu = List.of(
    "6) Select a Property",
    "7) Add a Property",
    "8) Delete a Property",
    "99) Print All Info"
  );
  private List<String> unitMenu = List.of(
    "9) Select a Unit",
    "10) Add Unit(s) to Property",
    "11) Delete Unit from Property",
    "12) Lease to Tenant",
    "13) Terminate Lease",
    "99) Print All Info"
  );
  //@formatter:on

  
}
