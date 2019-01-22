package com.timeshet.db;

import java.sql.Connection;

public class DAO {
	
	private Connection conn;
	
	public DAO() {
		try {
			conn = DBUtils.connect();
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
}
