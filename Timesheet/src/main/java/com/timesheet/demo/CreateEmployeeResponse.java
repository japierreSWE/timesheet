package com.timesheet.demo;

public class CreateEmployeeResponse {
	int httpCode;
	String ID;
	
	public CreateEmployeeResponse(int code, String id) {
		httpCode = code;
		ID = id;
	}
	
}
