package com.timeshet.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.timesheet.entity.Employee;

public class EmployeeDAO extends DAO {
	
	public EmployeeDAO() {
		super();
	}
	
	public int createEmployee(Employee emp) throws Exception {
		
		try {
			
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Employee WHERE username = ?");
			ps.setString(1, emp.username);
			ResultSet results = ps.executeQuery();
			
			while(results.next()) { //if there is any employee with the same username, reject this.
				return 430; //will use code 430 to represent same username conflict
			}
			
			ps = conn.prepareStatement("INSERT INTO Employee (employeeID, name, address, username, password, periodStart) VALUES (?,?,?,?,?,?)");
			ps.setString(1, emp.getID());
			ps.setString(2, emp.name);
			ps.setString(3, emp.address);
			ps.setString(4, emp.username);
			ps.setString(5, emp.password);
			ps.setDate(6, Date.valueOf(emp.periodStart));
			ps.execute();
			return 200;
		} catch(Exception e) {
			System.out.println(e.toString());
			throw e;
		}
	}
	
}
