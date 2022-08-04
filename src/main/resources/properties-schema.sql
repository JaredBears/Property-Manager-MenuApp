DROP TABLE IF EXISTS unit_employee;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS tenant;
DROP TABLE IF EXISTS unit;
DROP TABLE IF EXISTS property;
DROP TABLE IF EXISTS city;
DROP TABLE IF EXISTS state;

CREATE TABLE state(
    state_code CHAR(2) NOT NULL,
    PRIMARY KEY (state_code)
);

CREATE TABLE city(
    city_id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    state_code CHAR(2) NOT NULL,
    city_name VARCHAR(64) NOT NULL,
    PRIMARY KEY (city_id),
    UNIQUE KEY (city_name, state_code),
    FOREIGN KEY (state_code) REFERENCES state (state_code) ON DELETE CASCADE
);

CREATE TABLE property(
    property_id INT UNSIGNED UNSIGNED AUTO_INCREMENT NOT NULL,
    city_id INT UNSIGNED UNSIGNED NOT NULL,
    street_address VARCHAR(64) NOT NULL,
    yearly_taxes DECIMAL(8, 2) NOT NULL,
    monthly_mortgage DECIMAL(7, 2) NOT NULL,
    PRIMARY KEY (property_id),
    UNIQUE KEY (street_address, city_id),
    FOREIGN KEY (city_id) REFERENCES city (city_id) ON DELETE CASCADE
);

CREATE TABLE unit(
    unit_id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    property_id INT UNSIGNED NOT NULL,
    unit_number VARCHAR(4) NOT NULL,
    monthly_rent DECIMAL(7, 2) NOT NULL,
    leased BOOLEAN NOT NULL,
    PRIMARY KEY (unit_id),
    UNIQUE KEY (unit_number, property_id),
    FOREIGN KEY (property_id) REFERENCES property (property_id) ON DELETE CASCADE
);

CREATE TABLE tenant(
    person_id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    unit_id INT UNSIGNED NOT NULL,
    name VARCHAR(64) NOT NULL,
    phone CHAR(10) NOT NULL,
    email VARCHAR(64) NOT NULL,
    PRIMARY KEY (person_id),
    UNIQUE KEY (name, unit_id),
    FOREIGN KEY (unit_id) REFERENCES unit (unit_id) ON DELETE CASCADE
);

CREATE TABLE employee(
    person_id INT UNSIGNED AUTO_INCREMENT NOT NULL,
    name VARCHAR(64) NOT NULL,
    phone CHAR(10) NOT NULL,
    email VARCHAR(64) NOT NULL,
    salary DECIMAL(8, 2) NOT NULL,
    PRIMARY KEY (person_id)
);

CREATE TABLE unit_employee(
    unit_id INT UNSIGNED NOT NULL,
    employee_id INT UNSIGNED NOT NULL,
    FOREIGN KEY (unit_id) REFERENCES unit (unit_id) ON DELETE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employee (employee_id) ON DELETE CASCADE,
    UNIQUE KEY (unit_id, employee_id)
);