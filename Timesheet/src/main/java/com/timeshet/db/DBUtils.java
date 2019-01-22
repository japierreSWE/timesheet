package com.timeshet.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtils {
	
	public final static String rdsMySqlDatabaseUrl = "timesheetdb.c88gqs6ae68r.us-east-2.rds.amazonaws.com";
	public final static String username = "timesheetAdmin";
	public final static String pass = "timesheetPass";
	
	
	public final static String jdbcTag = "jdbc:mysql://";
	public final static String port = "3306";
	public final static String multiQueries = "?allowMultiQueries=true";
	
	public final static String dbname = "innodb";
	
	static Connection conn;
	
	static Connection connect() throws Exception {
		if(conn != null) {return conn; }
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcTag + rdsMySqlDatabaseUrl + ":" + port + "/" + dbname + multiQueries,
					username, pass);
			return conn;
		} catch (Exception e) {
			throw new Exception("Could not connect to db");
		}
		
	}
	
}
