package com.timesheet.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.timesheet.entity.Client;
import com.timeshet.db.ClientDAO;

public class GetClientsHandler implements RequestStreamHandler {
	
	LambdaLogger logger = null;
	
	//returns the clients' names obtained from db. returns null if failed.
	public ArrayList<Client> retrieveClients() {
		
		try {
			return new ClientDAO().retrieveClients();
		} catch(Exception e) {
			logger.log(e.toString());
			return null;
		}
	}
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	
    	logger = context.getLogger();
    	JSONObject jsonResponse = new JSONObject();
    	JSONObject headerJson = new JSONObject();
        headerJson.put("Content-Type",  "application/json");
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
    	headerJson.put("Access-Control-Allow-Headers", "*");
    	jsonResponse.put("headers", headerJson);
    	
    	GetClientsResponse response = null;
    	boolean processed = false;
    	
    	try {
    		
    		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    		JSONParser parser = new JSONParser();
    		JSONObject jsonRequest = (JSONObject)parser.parse(reader);
    		String requestType = (String)jsonRequest.get("httpMethod");
    		
    		if(requestType != null && requestType.equalsIgnoreCase("OPTIONS")) {
    			response = new GetClientsResponse(200, null);
    			jsonResponse.put("body", new Gson().toJson(response));
    			processed = true;
    		} //there is never anything in the body of a GET request
    		//so we don't need to process this any further
    	} catch(ParseException e) {
    		logger.log(e.toString());
    		response = new GetClientsResponse(400, null);
			jsonResponse.put("body", new Gson().toJson(response));
			processed = true;
    	}
    	
    	if(!processed) { //get clients (or not) then prepare appropiate response
    		ArrayList<Client> clients = retrieveClients();
    		
    		if(clients == null) {
    			response = new GetClientsResponse(400, null);
    			jsonResponse.put("body", new Gson().toJson(response));
    		} else {
    			response = new GetClientsResponse(200, clients);
    			jsonResponse.put("body", new Gson().toJson(response));
    		}
    		
    	}
    	
    	logger.log("result: " + jsonResponse.toJSONString());
    	OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(jsonResponse.toJSONString());  
        writer.close();
        
    }

}
