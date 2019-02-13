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
	//gets all clients from the db
	public ArrayList<Client> retrieveClients() throws Exception {
		
		ArrayList<Client> arr = new ArrayList<Client>();
		
		try {
			
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Client");
			ResultSet results = ps.executeQuery();
			
			while(results.next()) {
				Client c = new Client(results.getString("manager"), results.getString("name"), results.getString("position"));
				arr.add(c);
			} //get all names and add them to arraylist
			
		} catch(Exception e) {
			throw e;
		}
		
		return arr;
		
	}
	
}
