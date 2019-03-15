package com.timesheet.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.timesheet.entity.Client;
import com.timeshet.db.ClientDAO;

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
	
	private boolean contains(ArrayList<Client> arr, String name) {
		
		for(Client c : arr) {
			if(name.equals(c.getName())) {
				return true;
			}
		}
		return false;
	}

    @Test
    public void testGetClientsHandler() throws IOException {
        GetClientsHandler handler = new GetClientsHandler();
        Client c = new Client("Manny Manager", "MannyCorp", "Head Manager");
        
        try {
        	new ClientDAO().createClient(c);
        } catch(Exception e) {
        	System.out.println("Internal test error");
        	return;
        }

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
        
        GetClientsResponse resp = new Gson().fromJson(body, GetClientsResponse.class);
        Assert.assertEquals(200, resp.httpCode);
        Assert.assertEquals(true, contains(resp.clients, c.getName()));
    }
}
