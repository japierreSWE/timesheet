package com.timesheet.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Employee {
	
	public String name; //name and address
	public String address;
	public String timesheet; //ID of this employee's timesheet
	public String username; //employee's username, password
	public String password;
	public String employeeID; //ID of this account
	
	public Employee() { //no-arg
		
	}
	
	public Employee(String name, String address, String username, String password) {
		//for creating a new employee
		this.name = name;
		this.address = address;
		this.username = username;
		this.password = password;
		this.employeeID = UUID.randomUUID().toString();
		this.timesheet = null; //employee doesn't have a timesheet yet
	}
	
	public Employee(String employeeID, String name, String address, String timesheet, String username, String password) {
		//for retrieving from db
		this.employeeID = employeeID;
		this.name = name;
		this.address = address;
		this.timesheet = timesheet;
		this.username = username;
		this.password = password;
	}
	
	public String getID() {
		return this.employeeID;
	}
	
}
