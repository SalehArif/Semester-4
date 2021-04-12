package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Donations {
	private int id;
	private Cafe cafe;
	private Charity charity;
	private int amount;
	private static ArrayList<Donations> donations = new ArrayList<Donations>();
	public Donations() {

	}

	public Donations(int a,int b,int c) {
		cafe = new Cafe(a);
		charity = new Charity(b);
		amount = c;
	}
	
	public void donate(int money) {
		
	}
	
	public int maxid() {
		try {
			int i=0;
			Connection con= Runner.connection();
	    	Statement stmt = con.createStatement();
			String query="select max(id) from donations";
			ResultSet rs=stmt.executeQuery(query);
			//make loop, arraylist for donation objects, store in there
			
			while(rs.next()){
				i=rs.getInt(1);
			}
			return i;
		} catch (SQLException e) {
			e.getMessage();
			return -1;
		}
	}
	
	public boolean addtodb() {
		try {
			//update total_donations from charity where charity_id = given id;
			Connection con= Runner.connection();
			String query = "insert into donations(id,cafe_id, charity_id, amount) values(?,?,?,?)";
			PreparedStatement stmt=con.prepareStatement(query);
			if(this.maxid()!=-1)
		    	this.id=this.maxid()+1;
			stmt.setInt(1, this.id);
			stmt.setInt(2,this.cafe.getID());  
		    stmt.setInt(3,this.charity.getId());
		    stmt.setInt(4,amount);
		    
	        stmt.execute();
	        con.close();
	        
	    	return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
	}
	
	public static boolean donatedetails() {
		try {
			Connection con= Runner.connection();
	    	Statement stmt = con.createStatement();
			String query="select * from cafe natural join donations natural join charity";
			ResultSet rs=stmt.executeQuery(query);
			//make loop, arraylist for donation objects, store in there
			int i=0;
			while(rs.next()){
				donations.add(new Donations(rs.getInt(2),rs.getInt(1),rs.getInt(7)));
				
				donations.get(i).cafe.setName(rs.getString(3));
//				donations.get(i).cafe.getLocation().setBuilding_number(rs.getInt(3));
				donations.get(i).charity.setName(rs.getString(8));
//				donations.get(i).charity.setTotal_donated(rs.getInt(9));
				i++;
			}
			return true;
		} catch (SQLException e) {
			e.getMessage();
			return false;
		}
	}
	
	public static int cafedonated(int id) {
		try {
			int i=0;
			Connection con= Runner.connection();
	    	Statement stmt = con.createStatement();
			String query="select amount from cafe natural join donations natural join charity where cafe_id="+id;
			ResultSet rs=stmt.executeQuery(query);
			//make loop, arraylist for donation objects, store in there
			
			while(rs.next()){
				i+=rs.getInt(1);
			}
			return i;
		} catch (SQLException e) {
			e.getMessage();
			return -1;
		}
	}
	
	public static boolean charitydonated(int id) {
		try {
			Connection con= Runner.connection();
	    	Statement stmt = con.createStatement();
			String query="select cafe_name,amount from cafe natural join donations natural join charity where charity_id="+id;
			ResultSet rs=stmt.executeQuery(query);
			//make loop, arraylist for donation objects, store in there
			int i=0;
			System.out.println("S/N Cafe\t\tAmount");
			while(rs.next()){
				System.out.println((i+1)+". "+rs.getString(1)+"\t"+rs.getInt(2));
				i++;
			}
			return true;
		} catch (SQLException e) {
			e.getMessage();
			return false;
		}
	}
	
	public static void showdonate() {
		System.out.println("Charity Cafe Donated Amount");
		for(int i=0;i<donations.size();i++)
			System.out.println(donations.get(i).charity.getName()+" "+donations.get(i).cafe.getName()+" "+donations.get(i).amount);
	}
	public Cafe getCafe() {
		return cafe;
	}

	public void setCafe(Cafe cafe) {
		this.cafe = cafe;
	}

	public Charity getCharity() {
		return charity;
	}

	public void setCharity(Charity charity) {
		this.charity = charity;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
