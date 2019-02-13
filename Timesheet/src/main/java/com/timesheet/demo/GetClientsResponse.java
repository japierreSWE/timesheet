package com.timesheet.demo;

import java.util.ArrayList;

import com.timesheet.entity.Client;

public class GetClientsResponse {
	
	int httpCode;
	ArrayList<Client> clients;
	
	public GetClientsResponse(int code, ArrayList<Client> arr) {
		httpCode = code;
		clients = arr;
	}
	
}
