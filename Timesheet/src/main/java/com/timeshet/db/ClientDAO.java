package com.timeshet.db;

import java.sql.PreparedStatement;

import com.timesheet.entity.Client;

public class ClientDAO extends DAO {
	
	public ClientDAO() {
		super();
	}
	
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
	
}
