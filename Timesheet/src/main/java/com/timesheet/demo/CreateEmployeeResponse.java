package com.timesheet.demo;

public class CreateEmployeeResponse {
	int httpCode;
	String ID;
	
	public CreateEmployeeResponse(int code, String ID) {
		httpCode = code;
		this.ID = ID;
	}
	
}
