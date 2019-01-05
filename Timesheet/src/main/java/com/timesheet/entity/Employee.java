package com.timesheet.entity;

import java.time.LocalDate;
import java.util.UUID;

public class Employee {
	
	String name; //name and address
	String address;
	String timesheet; //ID of this employee's timesheet
	String username; //employee's username, password
	String password;
	String employeeID; //ID of this account
	LocalDate periodStart; //date of the 1st period
	
	public Employee() { //no-arg
		
	}
	
	public Employee(String name, String address, String username, String password, LocalDate start) {
		
		this.name = name;
		this.address = address;
		this.username = username;
		this.password = password;
		this.employeeID = UUID.randomUUID().toString();
		this.timesheet = null; //employee doesn't have a timesheet yet
		this.periodStart = start;
	}
	
}
