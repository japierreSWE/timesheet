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
import com.timesheet.entity.Row;
import com.timesheet.entity.Timesheet;
import com.timeshet.db.EmployeeDAO;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class CreateTimesheetHandlerTest {
	
	Context createContext(String name) {
		TestContext con = new TestContext();
		con.setFunctionName(name);
		return con;
	}

    @Test
    public void testCreateTimesheetHandler() throws IOException {
    	
    	Employee emp = new Employee("Test", "1 Test Way", "test1", "test", LocalDate.of(2001, 1, 1));
        
        try {
        	new EmployeeDAO().createEmployee(emp);
        } catch(Exception e) {
        	System.out.println("internal test exception");
        	return;
        }
        
        Row row = new Row("2001-01-01", "07:00", "13:00", "comment");
        Row[] rows = new Row[1];
        rows[0] = row;
        
        CreateTimesheetRequest request = new CreateTimesheetRequest();
        request.clientName = "GenericCorp";
        request.employeeID = emp.employeeID;
        request.rows = rows;
        request.workTitle = "Work";
        String jsonRequest = new Gson().toJson(request);
    	
        CreateTimesheetHandler handler = new CreateTimesheetHandler();

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
        
        CreateTimesheetResponse resp = new Gson().fromJson(body, CreateTimesheetResponse.class);
        Assert.assertEquals(200, resp.httpCode);
    }
}
