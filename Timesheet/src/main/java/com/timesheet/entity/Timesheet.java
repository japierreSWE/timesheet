package com.timesheet.entity;

import java.util.UUID;

public class Timesheet {
	
	String timesheetID;
	String employee; //id of the employee that made this one
	String workTitle;
	String clientName;
	Row[] rows;
	
	public Timesheet() { //no-arg
		
	}
	
	public Timesheet(String employee, String workTitle, String client, Row[] rows) {
		timesheetID = UUID.randomUUID().toString();
		this.employee = employee;
		this.workTitle = workTitle;
		this.clientName = client;
		this.rows = rows;
	}
	
	public String getEmployee() {
		return employee;
	}
	
	public String getID() {
		return timesheetID;
	}
	
	public String getWorkTitle() {
		return workTitle;
	}
	
	public String getClient() {
		return clientName;
	}
	
	public Row[] getRows() {
		return rows;
	}
	
}
