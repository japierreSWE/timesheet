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
import com.timesheet.entity.Client;

public class CreateClientHandler implements RequestStreamHandler {
	
	LambdaLogger logger = null;
	
	private int addToDatabase(Client cl) {
		//section here where we use a DAO
		return 0;
	}
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	
    	logger = context.getLogger();
    	logger.log("Creating client");
    	
    	JSONObject headerJson = new JSONObject();
        headerJson.put("Content-Type",  "application/json");
 		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
 	    headerJson.put("Access-Control-Allow-Origin",  "*");
     	headerJson.put("Access-Control-Allow-Headers", "*");
     	
     	JSONObject jsonResponse = new JSONObject(); //json object containing response
     	jsonResponse.put("headers", headerJson);
     	
     	CreateClientResponse httpResponse = null;
     	String httpBody = null;
     	boolean processed = true;
     	
     	try {
     		//parse request
     		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
     		JSONParser parser = new JSONParser();
     		JSONObject jsonRequest = (JSONObject)parser.parse(reader);
     		
     		logger.log("Input was: " + jsonRequest.toJSONString());
     		String requestType = (String)jsonRequest.get("httpMethod");
     		
     		if(requestType != null && requestType.equalsIgnoreCase("OPTIONS")) {
     			//provide 200 response for OPTIONS and end
     			
     			logger.log("Options");
     			httpResponse = new CreateClientResponse(200);
     			jsonResponse.put("body", new Gson().toJson(httpResponse));
     			processed = true;
     		} else {
     			httpBody = (String)jsonRequest.get("body");
     			if(httpBody == null) {
     				httpBody = jsonRequest.toJSONString();
     			}
     		}
     		
     	} catch(ParseException e) {
     		logger.log("Couldn't parse JSON input");
     		logger.log(e.toString());
     		httpResponse = new CreateClientResponse(400);
     		jsonResponse.put("body", new Gson().toJson(httpResponse));
     		processed = true;
     	}
        
     	if(!processed) {
     		CreateClientRequest request = new Gson().fromJson(httpBody, CreateClientRequest.class);
     		String manager = request.manager;
     		String name = request.name;
     		String position = request.position;
     		
     		Client cl = new Client(manager, name, position);
     		int code = addToDatabase(cl);
     		httpResponse = new CreateClientResponse(code);
     		jsonResponse.put("body", new Gson().toJson(httpResponse));
     	}
     	
     	logger.log("Output is: " + jsonResponse.toJSONString());
     	OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(jsonResponse.toJSONString());  
        writer.close();
    }

}
