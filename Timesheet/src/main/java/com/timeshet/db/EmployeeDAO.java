package com.timeshet.db;

import java.sql.Date;
import java.sql.PreparedStatement;

import com.timesheet.entity.Employee;

public class EmployeeDAO extends DAO {
	
	public EmployeeDAO() {
		super();
	}
	
	public void createEmployee(Employee emp) throws Exception {
		
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Employee (employeeID, name, address, username, password, periodStart) VALUES (?,?,?,?,?,?)");
			ps.setString(1, emp.getID());
			ps.setString(2, emp.name);
			ps.setString(3, emp.address);
			ps.setString(4, emp.username);
			ps.setString(5, emp.password);
			ps.setDate(6, Date.valueOf(emp.periodStart));
			ps.execute();
		} catch(Exception e) {
			System.out.println(e.toString());
			throw e;
		}
	}
	
}
