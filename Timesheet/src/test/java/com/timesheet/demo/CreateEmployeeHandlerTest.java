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

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CreateEmployeeHandlerTest {
	
	Context createContext(String name) {
		TestContext con = new TestContext();
		con.setFunctionName(name);
		return con;
	}
	

    private static final String SAMPLE_INPUT_STRING = "{\"foo\": \"bar\"}";
    private static final String EXPECTED_OUTPUT_STRING = "{\"FOO\": \"BAR\"}";

    @Test
    public void testCreateEmployeeHandler() throws IOException {
        CreateEmployeeHandler handler = new CreateEmployeeHandler();
        
        CreateEmployeeRequest req = new CreateEmployeeRequest();
        req.address = "1 Test Ave";
        req.name = "John Doe";
        req.password = "12345";
        req.username = "jdoe3";
        String jsonRequest = new Gson().toJson(req);
        
        
        InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
        OutputStream output = new ByteArrayOutputStream();

        handler.handleRequest(input, output, createContext("random"));

        // TODO: validate output here if needed.
        String sampleOutputString = output.toString();
        System.out.println(sampleOutputString);
        String body = null;
        try {
        	JSONParser parser = new JSONParser();
        	JSONObject jsonResponse = (JSONObject)parser.parse(sampleOutputString);
        	body = (String)jsonResponse.get("body");
        } catch(Exception e) {
        	System.out.println("problem");
        }
        
        CreateEmployeeResponse resp = new Gson().fromJson(body, CreateEmployeeResponse.class);
        
        Assert.assertEquals(200, resp.httpCode);
    }
    
}
