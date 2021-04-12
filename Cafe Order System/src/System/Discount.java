package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Discount {

	private int percentage;
	private String code;
	private int price;
	public Discount() {

	}
	public Discount(int a,String b) {
		this.percentage=a;
		this.code=b;
	}
	public Discount(String b) {
		this.code=b;
	}
	
	public Discount(Discount a) {
		this.code=a.code;
		this.percentage=a.percentage;
		this.price=a.price;
	}
	public boolean discountdetails() {
		try {
			Connection con= Runner.connection();
			PreparedStatement stmt=con.prepareStatement("select discount_percentage,price from discount where code=?");    
		    stmt.setString(1,code);
	        ResultSet rs=stmt.executeQuery();
	        while(rs.next()) {
	        	this.percentage= rs.getInt(1);
	        	this.price = rs.getInt(2);
	        }
	    	con.close();
	    	return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
	}
	
	public static boolean discountexists(String code) {
		try {
			Connection con= Runner.connection();
			PreparedStatement stmt=con.prepareStatement("select code from discount where code=?");
			stmt.setString(1, code);
			ResultSet rs=stmt.executeQuery();
			while(rs.next())
				if(rs.getString(1) != null && !rs.getString(1).isEmpty()) {
					con.close();
					return true;
				}
				else {
			    	con.close();
			    	return false;
			    }
	    
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
		return false;
	}
	
	public static boolean showalldiscounts() {
		try {
			Connection con= Runner.connection();
			Statement stmt=con.createStatement();
	        ResultSet rs=stmt.executeQuery("select * from discount");
	        System.out.println("   Discounts");
	        System.out.println("Code Percentage Price");
	        while(rs.next())
	        	System.out.println(rs.getString(2)+"\t"+rs.getInt(1)+"%\t"+rs.getInt(3));
	    	con.close();
	    	return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
