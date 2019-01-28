package com.timesheet.entity;

public class Client {
	
	String username;
	String password;
	String manager; //name of manager
	String name; //name of client
	String position;
	//Timesheet[] pendingTimesheets;
	
	public Client(String username, String password, String manager, String name, String position) {
		this.manager = manager;
		this.name = name;
		this.position = position;
	}
	
}
