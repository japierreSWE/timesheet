package com.timesheet.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CreateClientHandlerTest {
	
	Context createContext(String name) {
		TestContext con = new TestContext();
		con.setFunctionName(name);
		return con;
	}
	
	//only works if db has been emptied of this client
    @Test
    public void testCreateClientHandler() throws IOException {
        CreateClientHandler handler = new CreateClientHandler();
        
        CreateClientRequest req = new CreateClientRequest();
        req.manager = "John Doe";
        req.name = "John Appleseed";
        req.position = "Senior Manager";
        String jsonRequest = new Gson().toJson(req);
        
        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());;
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("random"));
        String sampleOutputString = output.toString();
        
        String body = null;
        try {
        	JSONParser parser = new JSONParser();
        	JSONObject jsonResponse = (JSONObject)parser.parse(sampleOutputString);
        	body = (String)jsonResponse.get("body");
        } catch(Exception e) {
        	System.out.println("problem");
        }
        
        CreateClientResponse resp = new Gson().fromJson(body, CreateClientResponse.class);
        
        // TODO: validate output here if needed.
        System.out.println(sampleOutputString);
        Assert.assertEquals(200, resp.httpCode);
        
        //do it again, should be 430 this time
        input = new ByteArrayInputStream(jsonRequest.getBytes());;
        output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("random"));
        sampleOutputString = output.toString();
        
        body = null;
        try {
        	JSONParser parser = new JSONParser();
        	JSONObject jsonResponse = (JSONObject)parser.parse(sampleOutputString);
        	body = (String)jsonResponse.get("body");
        } catch(Exception e) {
        	System.out.println("problem");
        }
        
        resp = new Gson().fromJson(body, CreateClientResponse.class);
        System.out.println(sampleOutputString);
        Assert.assertEquals(430, resp.httpCode);
    }
}
