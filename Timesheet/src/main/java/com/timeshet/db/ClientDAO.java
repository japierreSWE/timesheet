package com.timeshet.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.timesheet.entity.Client;

public class ClientDAO extends DAO {
	
	public ClientDAO() {
		super();
	}
	
	//puts a client in the database and returns the corresponding http code
	public int createClient(Client c) throws Exception {
		
		try {
			
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Client (name, position, manager) VALUES (?,?,?)");
			ps.setString(1, c.getName());
			ps.setString(2, c.getPosition());
			ps.setString(3, c.getManager());
			ps.execute();
			return 200;
		} catch(Exception e) {
			System.out.println(e.toString());
			throw e;
		}
		
	}
	//gets all clients' names from the db
	public ArrayList<String> retrieveClients() throws Exception {
		
		ArrayList<String> arr = new ArrayList<String>();
		
		try {
			
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Client");
			ResultSet results = ps.executeQuery();
			
			while(results.next()) {
				arr.add(results.getString("name"));
			} //get all names and add them to arraylist
			
		} catch(Exception e) {
			throw e;
		}
		
		return arr;
		
	}
	
}
