package com.timesheet.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.timesheet.entity.Employee;
import com.timeshet.db.EmployeeDAO;

public class CreateEmployeeHandler implements RequestStreamHandler {
	
	LambdaLogger logger = null;
	
	private int addToDatabase(Employee e) {
		try{
			return new EmployeeDAO().createEmployee(e);
		} catch(Exception ex) {
			logger.log("Couldn't add to database");
			return 400;
		}
	}
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

        logger = context.getLogger();
        logger.log("Creating employee");
        JSONObject headerJson = new JSONObject();
        headerJson.put("Content-Type",  "application/json");
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
    	headerJson.put("Access-Control-Allow-Headers", "*");
    	
    	JSONObject jsonResponse = new JSONObject();
    	jsonResponse.put("headers", headerJson);
    	//add headers
    	CreateEmployeeResponse httpResponse = null;
    	String httpBody = null;
    	boolean processed = false;
    	
    	try {
    		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    		JSONParser parser = new JSONParser();
    		JSONObject jsonRequest = (JSONObject)parser.parse(reader);
    		//parse request
    		
    		logger.log("Input was " + jsonRequest.toJSONString());
    		String requestType = (String)jsonRequest.get("httpMethod");
    		
    		if(requestType != null && requestType.equalsIgnoreCase("OPTIONS")) {
    			//provide 200 response for OPTIONS, and end
    			
    			logger.log("Options");
    			httpResponse = new CreateEmployeeResponse(200, null);
    			jsonResponse.put("body", new Gson().toJson(httpResponse));
    			processed = true;
    		} else {
    			httpBody = (String)jsonRequest.get("body");
    			if(httpBody == null) {
    				httpBody = jsonRequest.toJSONString();
    			}
    		}
    		
    	} catch(ParseException e) {
    		logger.log("Could not parse JSON input");
    		logger.log(e.toString());
    		httpResponse = new CreateEmployeeResponse(400, null);
    		jsonResponse.put("body", new Gson().toJson(httpResponse));
    		processed = true;
    	}
    	
    	if(!processed) {
    		
    		CreateEmployeeRequest request = new Gson().fromJson(httpBody, CreateEmployeeRequest.class);
    		String name = request.name;
    		String address = request.address;
    		String user = request.username;
    		String pass = request.password;
    		boolean invalidInput = false;
    		
    			
			Employee emp = new Employee(name, address, user, pass);
			boolean added = false;
			//make employee and try to put it in database
			try {
				
				int code = addToDatabase(emp);
				
				if(code == 200) {
					httpResponse = new CreateEmployeeResponse(code, emp.getID());
	        		jsonResponse.put("body", new Gson().toJson(httpResponse));	
				} else {
					httpResponse = new CreateEmployeeResponse(code, null);
            		jsonResponse.put("body", new Gson().toJson(httpResponse));
				}
				
			} catch(Exception e) {
				logger.log("Could not add to database");
				logger.log(e.toString());
				httpResponse = new CreateEmployeeResponse(400, null);
        		jsonResponse.put("body", new Gson().toJson(httpResponse));
			}
			    		
    		logger.log("result: " + jsonResponse.toJSONString());
        	OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
            writer.write(jsonResponse.toJSONString());  
            writer.close();
    	}
    	
    }

}
