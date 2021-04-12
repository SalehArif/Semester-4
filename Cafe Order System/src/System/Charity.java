package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Charity {
	
	
	private String name;
	private int total_donated, id;
	public Charity(int a) {
		id = a;
	}
	
	public int getTotal_donated() {
		return total_donated;
	}
	public static void showcharity() {
		Connection con = Runner.connection();
		Statement stmt;
		try {
			stmt = con.createStatement();
	        ResultSet rs=stmt.executeQuery("select charity_id,name from charity");
	        System.out.println("\tCharity");
	        System.out.println("ID  Name");
	        while(rs.next())
	        System.out.println(rs.getInt(1)+". "+rs.getString(2));
	    	con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getdetails() {
		try { 
			Connection con= Runner.connection();
	    	Statement stmt = con.createStatement();
			String query="select charity_id,name,total_donations from charity where charity_id="+this.id;
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()) {
				this.name = rs.getString(2);
				this.total_donated = rs.getInt(3);
			}
			return true;
		} catch (SQLException e) {
			e.getMessage();
			return false;
		}
	}

	public boolean addtodb(int amount) {
		try {
			int total = total_donated+amount;
			Connection con= Runner.connection();
			String query = "update Charity set total_donations="+total+"where charity_id =" + id;
			PreparedStatement stmt=con.prepareStatement(query);
	        stmt.execute();
	        con.close();
	        
	    	return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setTotal_donated(int total_donated) {
		this.total_donated = total_donated;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
