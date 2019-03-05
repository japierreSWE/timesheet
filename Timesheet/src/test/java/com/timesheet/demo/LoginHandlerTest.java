package com.timesheet.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.timesheet.entity.Employee;
import com.timeshet.db.EmployeeDAO;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class LoginHandlerTest {
	
	Context createContext(String name) {
		TestContext con = new TestContext();
		con.setFunctionName(name);
		return con;
	}


    @Test
    public void testLoginHandler() throws IOException {
        LoginHandler handler = new LoginHandler();
        
        Employee emp = new Employee("Test", "1 Test Way", "test", "test");
        
        try {
        	new EmployeeDAO().createEmployee(emp);
        } catch(Exception e) {
        	System.out.println("internal test exception");
        	return;
        }
        
        LoginRequest req = new LoginRequest();
        req.password = "test";
        req.username = "test";
        String jsonRequest = new Gson().toJson(req);
        
        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());;
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("random"));
        
        String sampleOutputString = output.toString();
        System.out.println(sampleOutputString);
        String body = null;
        try {
        	JSONParser parser = new JSONParser();
        	JSONObject jsonResponse = (JSONObject)parser.parse(sampleOutputString);
        	body = (String)jsonResponse.get("body");
        } catch(Exception e) {
        	System.out.println("internal test exception");
        	return;
        }
        
        LoginResponse resp = new Gson().fromJson(body, LoginResponse.class);

        
        Assert.assertEquals(200, resp.httpCode);
        
        
        req.username = "does not exist"; //would have made a separate test case
        //but since usernames are unique, you can only make one account inside a test
        
        jsonRequest = new Gson().toJson(req);
        input = new ByteArrayInputStream(jsonRequest.getBytes());;
        output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("random"));
        
        sampleOutputString = output.toString();
        System.out.println(sampleOutputString);
        body = null;
        try {
        	JSONParser parser = new JSONParser();
        	JSONObject jsonResponse = (JSONObject)parser.parse(sampleOutputString);
        	body = (String)jsonResponse.get("body");
        } catch(Exception e) {
        	System.out.println("internal test exception");
        	return;
        }
        
        resp = new Gson().fromJson(body, LoginResponse.class);

        
        Assert.assertEquals(430, resp.httpCode);
        
    }
}
