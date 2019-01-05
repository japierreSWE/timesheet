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

public class CreateEmployeeHandler implements RequestStreamHandler {
	
	private void addToDatabase(Employee e) throws Exception {
		//TODO: Add the part where we add it to the database
	}
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

        LambdaLogger logger = context.getLogger();
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
    		String periodStart = request.periodStart;
    		LocalDate startDate = null; //this will be the period start parsed into a date
    		boolean invalidInput = false;
    		
    		try {
    			startDate = LocalDate.parse(periodStart); //parse date
    		} catch(DateTimeParseException e) {
    			logger.log("Could not parse date");
        		logger.log(e.toString());
        		httpResponse = new CreateEmployeeResponse(400, null);
        		jsonResponse.put("body", new Gson().toJson(httpResponse));
        		invalidInput = true;
    		}
    		
    		if(!invalidInput) {
    			
    			Employee emp = new Employee(name, address, user, pass, startDate);
    			//make employee and try to put it in database
    			try {
    				addToDatabase(emp);
    			} catch(Exception e) {
    				logger.log("Could not add to database");
    				logger.log(e.toString());
    				httpResponse = new CreateEmployeeResponse(400, null);
            		jsonResponse.put("body", new Gson().toJson(httpResponse));
    			}
    			
    			httpResponse = new CreateEmployeeResponse(200, emp.getID());
        		jsonResponse.put("body", new Gson().toJson(httpResponse));	
    		}
    		
    		logger.log("result: " + jsonResponse.toJSONString());
        	OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
            writer.write(jsonResponse.toJSONString());  
            writer.close();
    	}
    	
    }

}
