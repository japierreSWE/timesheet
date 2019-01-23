package com.timeshet.db;

import java.sql.PreparedStatement;

import com.timesheet.entity.Employee;

public class EmployeeDAO extends DAO {
	
	public EmployeeDAO() {
		super();
	}
	
	public boolean createEmployee(Employee emp) {
		
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Employee (employeeID, name, address, username, password) VALUES (?,?,?,?,?)");
			ps.setString(1, emp.getID());
			ps.setString(2, emp.name);
			ps.setString(3, emp.address);
			ps.setString(4, emp.username);
			ps.setString(5, emp.password);
			ps.execute();
			return true;
		} catch(Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}
	
}
