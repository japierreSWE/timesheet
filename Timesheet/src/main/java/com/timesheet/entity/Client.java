package com.timesheet.entity;

public class Client {
	
	String manager;
	String name;
	String position;
	//Timesheet[] pendingTimesheets;
	
	public Client(String manager, String name, String position) {
		this.manager = manager;
		this.name = name;
		this.position = position;
	}
	
}
