package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Address{

	private int Building_number;
	private String street;
	private String city;
	
	public Address() {
		
	}
	
	public Address(int a,String b,String c) {
		Building_number=a;
		street=b;
		city=c;
	
	}
	public Address(Address a) {
		Building_number=a.Building_number;
		city=a.city;
		street=a.street;
	}
	
	public boolean newaddress() {
		try {
			Connection con= Runner.connection();
			//Statement stmt=con.createStatement();
			PreparedStatement stmt=con.prepareStatement("insert into Address(building_number,street,city) values(?,?,?)");  
		    stmt.setInt(1,this.Building_number);//1 specifies the first parameter in the query  
		    stmt.setString(2,this.street);
		    stmt.setString(3,this.city);
	        stmt.execute();
	    con.close();
	    return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
	}
	
	public boolean getaddress() {
		try { 
			Connection con= Runner.connection();
	    	Statement stmt = con.createStatement();
			String query="select * from address where building_number="+this.Building_number;
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()) {
			this.street = rs.getString(2);
			this.city = rs.getString(3);
			}
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
	}
	
	public int getBuilding_number() {
		return Building_number;
	}
	public void setBuilding_number(int Building_number) {
		this.Building_number = Building_number;
	}
	public String getStreet() {
		return street;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void display() {
		System.out.println(Building_number +" " + city + " " + street);
	}
	
	public String toString() {
		return Building_number +", " + street + ", " + city; 
	}
	
	
}