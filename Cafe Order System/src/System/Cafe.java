package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Cafe {
	
	private int ID;
	private String name;
	private Address location;
	private ArrayList<Food> food;
	private ArrayList<Order> order;
	private ArrayList<Profit> gains;
	private int balance;
	public Cafe() {
		location = new Address();
		food = new ArrayList<Food>();
	}
	public Cafe(int a) {
		ID= a;
		location = new Address();
		food = new ArrayList<Food>();
		order = new ArrayList<Order>();
		gains = new ArrayList<Profit>();
	}

	public static void showcafes() {
		Connection con = Runner.connection();
		Statement stmt;
		try {
			stmt = con.createStatement();
	        ResultSet rs=stmt.executeQuery("select cafe_id,cafe_name,building_number,street,city from cafe natural join address");
	        System.out.println("\t\tCafes");
	        System.out.println("ID\tName\t\tAddress");
	        while(rs.next())
	        System.out.println(rs.getInt(1)+". "+rs.getString(2)+"\t"+rs.getInt(3)+", "+rs.getString(4)+", "+rs.getString(5));
	    	con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void showmenu() {
//	    System.out.println("S/N\tName\t\t\tType\tPrice");
	    System.out.printf("%2s %15s %12s %7s", "S/N", "Name", "Type", "Price");
	    System.out.println();
	    for(int i=0; i<food.size(); i++) {
//	    	System.out.println((i+1)+". "+food.get(i).getName()+"\t"+food.get(i).getType()+"\t"+food.get(i).getPrice());
	    	System.out.format("%2d %20s %10s %5d",
	                i+1, food.get(i).getName(), food.get(i).getType(), food.get(i).getPrice());
	        System.out.println();
	    }
	    System.out.printf("%2s %20s","0", "Go Back");
	    System.out.println();
	}
	
	public void showorders() {
		
	    for(int i=0; i<order.size(); i++) {
	    	System.out.println("Order no. "+(i+1));
	    	order.get(i).showorder();
	    	System.out.println();
	    }
	}
	public boolean updatebalance() {
		try {
			Connection con= Runner.connection();
			String query = "update cafe set balance=? where cafe_id =?";
			PreparedStatement stmt=con.prepareStatement(query);
			stmt.setInt(1, balance);
			stmt.setInt(2, ID);
			stmt.execute();
	        con.close();
	        
	    	return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
		
	}
	
	public void setbalance() {
		balance =0;
		for(int i=0;i<order.size();i++) {
			Order ord = order.get(i);
			ord.itemprice();
			for(int j=0;j<ord.getOrderfood().size();j++)
					balance += ord.getPrice().get(j);
		}
		balance-=Donations.cafedonated(ID);
		updatebalance();
	}
	     
	public boolean donate_amount(int amount, int ch_id){
		//input amount to be donated from profit
		//if amount less than profit
		//add it to db
		Donations donate;
		
		if(amount<balance) {
			donate = new Donations(this.ID,ch_id,amount);
			balance -= amount;//donate amount to charity with id = ch_id
			donate.addtodb();
			updatebalance();
			return true;
		}
		else
			return false; //can't donate this amount
		
	}
	
	public int profit_calc(String month_year) { //MM-YY

		String[] mon = new String[3];
		int profit=0;
		String[] giv  = month_year.split("-");
		for(int i=0;i<order.size();i++) {
			Order ord = order.get(i);
			mon= ord.getDate().split("-");
			ord.itemprice();
			if(mon[1].equals(giv[0]) && mon[2].equals(giv[1]))
				for(int j=0;j<ord.getOrderfood().size();j++)
					profit += ord.getPrice().get(j);
		}
		
		return profit;
	}
	
	public boolean getorders() {
		try {
			Connection con= Runner.connection();
	        Statement stmt = con.createStatement();
	        String query="select name,type,price,bill,to_char(order_date,'DD-MM-YY'),quantity,order_id,person_id from orders natural join "
	        		+ "food_in_order natural join (food natural join cafe_has_food) natural join cafe where cafe_id="+this.ID;
	        ResultSet rs=stmt.executeQuery(query);
	        int i=-1;
	        int ordid = 0;
	        int j=0;
	        while(rs.next()) {
	        //food.add(new Food());
	        //this.name = rs.getString(4);
	        if(ordid!=rs.getInt(7)) {
	        	i++;
	        	order.add(new Order(rs.getInt(7),rs.getInt(4),rs.getString(5),new Person(rs.getInt(8))));
//	        	order.get(i).setId(rs.getInt(7));
//	        	order.get(i).setTotal_bill(rs.getInt(4));
//	        	order.get(i).setDate(rs.getString(5));
//	        	order.get(i).setOrdee(new Person(rs.getInt(8)));
	        	order.get(i).getOrdee().findid();
	        	order.get(i).addcafe(ID);
	        	ordid = rs.getInt(7);
	        	j=0;
	        }
	        order.get(i).addquantity(rs.getInt(6));
	        order.get(i).addfood(new Food(rs.getString(1),rs.getString(2),rs.getInt(3)));
//	        order.get(i).getOrderfood().get(j).setName(rs.getString(1));
//	        order.get(i).getOrderfood().get(j).setType(rs.getString(2));
//	        order.get(i).getOrderfood().get(j).setPrice(rs.getInt(3));
	        j++;
	        
	        }
	        return true;
		} catch (SQLException e) {
			e.getMessage();
			return false;
		}
	}
	
	public boolean getfood() {
		try { 
			Connection con= Runner.connection();
	    	Statement stmt = con.createStatement();
			String query="select food_id,name,type,price from food natural join cafe_has_food natural join cafe where cafe_id="+this.ID;
			ResultSet rs=stmt.executeQuery(query);
			int i=0;
			while(rs.next()) {
				food.add(new Food(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4)));
				i++;
			}
			return true;
		} catch (SQLException e) {
			e.getMessage();
			return false;
		}
		
	}
	public static boolean getfood(String a,Order b) {
		try { 
			Connection con= Runner.connection();
			String query="select cafe_id,food_id,type from food natural join cafe_has_food natural join cafe where food.name=?";
			PreparedStatement stmt=con.prepareStatement(query);
			stmt.setString(1, a);
			ResultSet rs=stmt.executeQuery();
			int ind = b.foodsearch(a);
			while(rs.next()) {
				if(ind!=-1) 
//					continue;  && rs.getInt(1)==b.getCafe_id().get(ind)
					b.getQuantity().set(ind, b.getQuantity().get(ind)+1);
				else {
				b.addfood(new Food(rs.getInt(2),a,rs.getString(3),0));
				b.addcafe(rs.getInt(1));
				b.addquantity(1);
				b.addprice(0);
				}
				break;
			}
			return true;
		} catch (SQLException e) {
			e.getMessage();
			return false;
		}
	}
	
	public boolean cafedetails() {
		try { 
			Connection con= Runner.connection();
	    	Statement stmt = con.createStatement();
			String query="select * from cafe natural join address where cafe_id="+this.ID;
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()) {
			this.name = rs.getString("cafe_name");
			this.balance = rs.getInt("balance");
			this.location.setBuilding_number(rs.getInt("building_number"));
			this.location.setStreet(rs.getString("street"));
			this.location.setCity(rs.getString("city"));
			}
			return true;
		} catch (SQLException e) {
			e.getMessage();
			return false;
		}
	}
	
	public static String cafename(int id) {
		String name = null;
		try { 
			Connection con= Runner.connection();
	    	Statement stmt = con.createStatement();
			String query="select cafe_name from cafe where cafe_id="+id;
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next())
				name = rs.getString(1);
			con.close();
			return name;
		} catch (SQLException e) {
			e.getMessage();
			return null;
		}
	}
	
	public void setID(int iD) {
		ID = iD;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLocation(Address location) {
		this.location = location;
	}
	public void setFood(ArrayList<Food> food) {
		this.food = food;
	}
	public void setOrder(ArrayList<Order> order) {
		this.order = order;
	}
	public void setGains(ArrayList<Profit> gains) {
		this.gains = gains;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public Address getLocation() {
		return location;
	}
	public ArrayList<Food> getFood() {
		return food;
	}
	public ArrayList<Order> getOrder() {
		return order;
	}
	public ArrayList<Profit> getGains() {
		return gains;
	}
	public int getBalance() {
		return balance;
	}

}
