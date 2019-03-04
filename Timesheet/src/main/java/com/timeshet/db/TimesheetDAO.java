package com.timeshet.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.timesheet.entity.Row;
import com.timesheet.entity.Timesheet;

public class TimesheetDAO extends DAO {
	
	public TimesheetDAO() {
		super();
	}
	
	public int createTimesheet(Timesheet ts) throws Exception {
		
		
		
		try {
			//put the timesheet in the database
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Timesheet (timesheetID, employee, workTitle, clientName) VALUES (?,?,?,?)");
			ps.setString(1, ts.getID());
			ps.setString(2, ts.getEmployee());
			ps.setString(3, ts.getWorkTitle());
			ps.setString(4, ts.getClient());
			ps.execute();
			
			Row[] rows = ts.getRows();
			//then put in all of its rows
			for(Row row : rows) {
				row.setTimesheet(ts.getID());
				ps = conn.prepareStatement("INSERT INTO Row (timesheet, date, startTime, endTime, comments) VALUES (?,?,?,?,?)");
				ps.setString(1, row.getTimesheet());
				ps.setString(2, row.getDate());
				ps.setString(3, row.getStartTime());
				ps.setString(4, row.getEndTime());
				ps.setString(5, row.getComments());
				ps.execute();
			}
			
			return 200;
		} catch(Exception e) {
			throw e;
		}
		
	}
	
}
