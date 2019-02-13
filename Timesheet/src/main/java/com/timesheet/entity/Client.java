package com.timesheet.entity;

public class Client {
	
	String manager; //name of manager
	String name; //name of client
	String position;
	
	public Client(String manager, String name, String position) {
		this.manager = manager;
		this.name = name;
		this.position = position;
	}
	
	public String getManager() {
		return manager;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPosition() {
		return position;
	}
	
}
