package com.timesheet.entity;

public class Row {
	
	String date;
	String startTime;
	String endTime;
	String comments;
	String timesheet; //id of the timesheet this is in
	
	public void setTimesheet(String str) {
		timesheet = str;
	}

	public String getDate() {
		return date;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getComments() {
		return comments;
	}

	public String getTimesheet() {
		return timesheet;
	}
	
	
	
}
