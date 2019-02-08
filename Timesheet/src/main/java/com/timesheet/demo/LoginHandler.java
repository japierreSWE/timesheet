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
import com.timesheet.entity.Employee;
import com.timeshet.db.EmployeeDAO;

public class LoginHandler implements RequestStreamHandler {
	
	private int verify(String user, String pass) {
		try {
			return new EmployeeDAO().verify(user, pass);
		} catch(Exception e) {
			logger.log(e.toString());
			return 400;
		}
	}
	
	
	LambdaLogger logger = null;
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	
    	logger = context.getLogger();
    	logger.log("Logging in");
    	
    	JSONObject headerJson = new JSONObject(); //create headers
        headerJson.put("Content-Type",  "application/json");
 		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
 	    headerJson.put("Access-Control-Allow-Origin",  "*");
     	headerJson.put("Access-Control-Allow-Headers", "*");
     	JSONObject responseJson = new JSONObject();
     	
     	responseJson.put("headers", headerJson); //put in response
     	String httpBody = null; //body of request
     	boolean processed = false;
     	
     	try {
     		
     		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
     		JSONParser parser = new JSONParser();
     		JSONObject jsonRequest = (JSONObject)parser.parse(reader);
     		logger.log("Input was: " + jsonRequest.toJSONString());
     		String requestType = (String)jsonRequest.get("httpMethod"); //get the HTTP request type
     		
     		if(requestType != null && requestType.equalsIgnoreCase("OPTIONS")) { //provide a 200 for options requests
     			logger.log("Options");
     			LoginResponse httpResponse = new LoginResponse(200, null);
     			responseJson.put("body", new Gson().toJson(httpResponse));
     			processed = true;
     		} else { //attempt to retreive the body, otherwise don't extract it
     			httpBody = (String)jsonRequest.get("body");
     			if(httpBody == null) {
     				httpBody = jsonRequest.toJSONString();
     			}
     		}
     		
     	} catch(ParseException e) {
     		logger.log(e.toString());
     		LoginResponse httpResponse = new LoginResponse(400, null);
 			responseJson.put("body", new Gson().toJson(httpResponse));
 			processed = true;
     	}
     	
     	
     	if(!processed) {
     		
     		LoginRequest httpRequest = new Gson().fromJson(httpBody, LoginRequest.class);
     		String username = httpRequest.username;
     		String password = httpRequest.password;
     		int code = verify(username, password);
     		//verify the user and password
     		try {
     			LoginResponse httpResponse = new LoginResponse(code, new EmployeeDAO().getEmployee(username, password));
     			responseJson.put("body", new Gson().toJson(httpResponse));
     		} catch(Exception e) {
     			LoginResponse httpResponse = new LoginResponse(400, null);
     			responseJson.put("body", new Gson().toJson(httpResponse));
     		}
     	}
     	
     	logger.log("Output is: " + responseJson.toJSONString());
     	OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
     	
    }

}
