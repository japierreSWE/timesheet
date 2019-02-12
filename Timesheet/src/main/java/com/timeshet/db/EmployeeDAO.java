package com.timeshet.db;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.timesheet.entity.Employee;

public class EmployeeDAO extends DAO {
	
	public EmployeeDAO() {
		super();
	}
	
	//puts an employee in the db and returns the corresponding http code
	public int createEmployee(Employee emp) throws Exception {
		
		try {
			//check for employees w/ the same username
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
			//put the employee into the database
			return 200;
		} catch(Exception e) {
			System.out.println(e.toString());
			throw e;
		}
	}
	
	//verifies that an account exists with this username and password and
	//returns the corresponding http code.
	public int verify(String user, String pass) throws Exception {
		
		try {
			//find an employee with the corresponding user and password
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Employee WHERE username = ? AND password = ?");
			ps.setString(1, user);
			ps.setString(2, pass);
			ResultSet results = ps.executeQuery();
			
			while(results.next()) { //if there is, return 200
				return 200;
			}
			
			return 430; //otherwise, don't. use 430 for invalid user/pass.
			
		} catch(Exception e) {
			System.out.println(e.toString());
			throw e;
		}
	}
	
	public Employee getEmployee(String user, String pass) throws Exception {
		
		try {
			//find an employee with the corresponding user and password
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM Employee WHERE username = ? AND password = ?");
			ps.setString(1, user);
			ps.setString(2, pass);
			ResultSet results = ps.executeQuery();
			
			while(results.next()) { //if there is one, return it
				Employee emp = new Employee(results.getString("employeeID"), results.getString("name"), results.getString("address"), results.getString("timesheet"), results.getString("username"), results.getString("password"), results.getDate("periodStart").toLocalDate());
				return emp;
			}
			
			return null; //otherwise, don't
			
		} catch(Exception e) {
			System.out.println(e.toString());
			throw e;
		}
	}
	
}
