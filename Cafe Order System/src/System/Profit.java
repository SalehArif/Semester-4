package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Profit {
	
	private int id;
	private int amount;
	private String month;
	private static ArrayList<Profit> profits = new ArrayList<Profit>();
	
	public Profit(int a, String b,int c) {
		this.amount=a;
		this.month=b;
		this.id=c;
	}

	public static void showprofits(Cafe a) {
		System.out.println("Cafe ID: "+a.getID()+" Cafe Name: "+a.getName()+"\nAmount Date");
		for(int i=0;i<profits.size();i++)
			System.out.println(profits.get(i).getAmount()+" "+profits.get(i).getMonth());
	}
	
	public static boolean getprofits(int id) {
		try {
			Connection con= Runner.connection();
			Statement stmt=con.createStatement();
	        ResultSet rs=stmt.executeQuery("select cafe_profit,to_char(profit_month,'MM-YY') from profit where cafe_id="+id);
	        while(rs.next()) {
	        	profits.add(new Profit(rs.getInt(1),rs.getString(2),id));
	        }
	    con.close();
	    return true;
		}catch(Exception e){
	        System.out.println(e);
	        return false;
		}
	}
	
	public boolean addtodb() {
		try {
			Connection con= Runner.connection();
			//Statement stmt=con.createStatement();
			PreparedStatement stmt=con.prepareStatement("insert into Profit(cafe_id,cafe_profit,profit_month) values(?,?,TO_DATE(?,'MM-YY'))");  
		    stmt.setInt(1,this.id);//1 specifies the first parameter in the query
		    stmt.setInt(2,this.amount);
		    stmt.setString(3,this.month);
	        stmt.execute();
	    con.close();
	    return true;
		}catch(Exception e) {
//	        System.out.println(e);
	        return false;
		}
	}
	public void setId(int id) {
		this.id = id;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public String getMonth() {
		return month;
	}
	
	
	
}
