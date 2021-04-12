package System;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class Prize {
	
	private int id;
	private String name;
	private int price;
	public Prize() {

	}
	public Prize(String a,int b) {
		name=a;
		id=b;
	}
	public Prize(Prize a) {
		id= a.id;
		name= a.name;
		price= a.price;
	}
		
	public String useprize(){
		switch(id) {
		case 1:
			Random rand = new Random();
			String[] cho = {"Small Pizza","Medium Pizza","Banana shake","Zinger Burger","Chicken Club Sandwich"};
			int ind = rand.nextInt(cho.length); 
			return cho[ind];
		case 2:
			return "Small Pizza";
		case 3:
			return "Medium Pizza";
		case 4:
			return "Large Pizza";
		case 5:
			return "Banana shake";
		case 6:
			return "Zinger Burger";
		case 7:
			return "Chicken Club Sandwich";
		default:
			return null;
		}
	}
	
	public static int getid(String a) {
		String query;
		try {
			Connection con= Runner.connection();
			Statement stmt=con.createStatement();
			query= "select prize_id from prize where name="+a;
			ResultSet rs=stmt.executeQuery(query);
			int ans = rs.getInt(1);
			con.close();
			return ans;
		}catch(Exception e) {
			System.out.println(e);
			return 0;
		}
	}
	
	public boolean getdetails(int a) {
		String query;
		try {
			Connection con= Runner.connection();
			Statement stmt=con.createStatement();
			query= "select name,points_price from prize where prize_id="+a;
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()) {
				this.name=rs.getString(1);
				this.price=rs.getInt(2);
			}
			con.close();
			return true;
		}catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public static boolean showallprizes() {
		try {
			Connection con= Runner.connection();
			Statement stmt=con.createStatement();
	        ResultSet rs=stmt.executeQuery("select * from prize");
	        System.out.println("\t\tPrizes");
	        System.out.println("ID\tName\t\t\t\tPrice");
	        while(rs.next())
	        	if(rs.getInt(1)==1 || rs.getInt(1)==5)
	        		System.out.println(rs.getInt(1)+" "+rs.getString(2)+"\t\t\t\t"+rs.getInt(3));
	        	else
	        		System.out.println(rs.getInt(1)+" "+rs.getString(2)+"\t\t\t"+rs.getInt(3));
	    	con.close();
	    	return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getPrice() {
		return price;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
}
