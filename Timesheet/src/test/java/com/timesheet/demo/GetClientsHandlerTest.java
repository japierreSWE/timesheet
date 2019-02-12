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
public class GetClientsHandlerTest {
	
	Context createContext(String name) {
		TestContext con = new TestContext();
		con.setFunctionName(name);
		return con;
	}
	
	private static final String SAMPLE_INPUT_STRING = "{\"foo\": \"bar\"}";

    @Test
    public void testGetClientsHandler() throws IOException {
        GetClientsHandler handler = new GetClientsHandler();

        InputStream input = new ByteArrayInputStream(SAMPLE_INPUT_STRING.getBytes());;
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
        	System.out.println("problem");
        }
        
        System.out.println(sampleOutputString);
        CreateEmployeeResponse resp = new Gson().fromJson(body, CreateEmployeeResponse.class);
        Assert.assertEquals(200, resp.httpCode);
    }
}
