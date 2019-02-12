package com.timesheet.demo;

import java.util.ArrayList;

public class GetClientsResponse {
	
	int httpCode;
	ArrayList<String> clients;
	
	public GetClientsResponse(int code, ArrayList<String> arr) {
		httpCode = code;
		clients = arr;
	}
	
}
