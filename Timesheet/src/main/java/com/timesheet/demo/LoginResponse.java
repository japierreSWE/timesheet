package com.timesheet.demo;

import com.timesheet.entity.Employee;

public class LoginResponse {
	
	int httpCode;
	Employee employee;
	
	public LoginResponse(int code, Employee e) {
		httpCode = code;
		e = employee;
	}
	
}
