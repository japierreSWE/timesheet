package com.timesheet.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.timesheet.entity.Row;
import com.timesheet.entity.Timesheet;
import com.timeshet.db.TimesheetDAO;

public class CreateTimesheetHandler implements RequestStreamHandler {
	
	LambdaLogger logger = null;
	
	private int addTimesheetToDatabase(Timesheet ts) {
		try {
			return new TimesheetDAO().createTimesheet(ts);
		} catch(Exception e) {
			logger.log(e.toString());
			return 400;
		}
	}
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	
    	logger = context.getLogger();
        logger.log("Creating Timesheet");
        JSONObject headerJson = new JSONObject();
        headerJson.put("Content-Type",  "application/json");
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
    	headerJson.put("Access-Control-Allow-Headers", "*");
    	
    	JSONObject jsonResponse = new JSONObject();
    	jsonResponse.put("headers", headerJson);
    	//add headers
    	CreateTimesheetResponse httpResponse = null;
    	String httpBody = null;
    	boolean processed = false;
    	
    	try {
    		
    		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    		JSONParser parser = new JSONParser();
    		JSONObject jsonRequest = (JSONObject)parser.parse(reader);
    		//parse request
    		
    		logger.log("Input was: " + jsonRequest.toJSONString());
    		String requestType = (String)jsonRequest.get("httpMethod");
    		
    		if(requestType != null && requestType.equalsIgnoreCase("OPTIONS")) {
    		//handle options request with 200	
    			
    			logger.log("Options");
    			httpResponse = new CreateTimesheetResponse(200);
    			jsonResponse.put("body", new Gson().toJson(httpResponse));
    			processed = true;
    			
    		} else { //otherwise, get body. if there's no body, check the json itself
    			httpBody = (String)jsonRequest.get("body");
    			
    			if(httpBody == null) {
    				httpBody = jsonRequest.toJSONString();
    			}
    		}
    			
    	} catch(ParseException e) {
    		logger.log("Could not parse JSON input");
    		logger.log(e.toString());
    		httpResponse = new CreateTimesheetResponse(400);
    		jsonResponse.put("body", new Gson().toJson(httpResponse));
    		processed = true;
    	}
    	
    	
    	if(!processed) {
    		CreateTimesheetRequest request = new Gson().fromJson(httpBody, CreateTimesheetRequest.class);
    		String workTitle = request.workTitle;
    		Row[] rows = request.rows;
    		String employeeID = request.employeeID;
    		String clientName = request.clientName;
    		Timesheet ts = new Timesheet(employeeID, workTitle, clientName, rows);
    		int code = addTimesheetToDatabase(ts); //make the rows and timesheet, then add them to db
    		
    		httpResponse = new CreateTimesheetResponse(code);
    		jsonResponse.put("body", new Gson().toJson(httpResponse));
    		
    	}
    	
    	logger.log("result: " + jsonResponse.toJSONString());
    	OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(jsonResponse.toJSONString());  
        writer.close();
    }

}
