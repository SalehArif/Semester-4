package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Order {

	private int id;
	private Person ordee;
	private ArrayList<Food> orderfood;
	private ArrayList<Integer> quantity;
	private ArrayList<Integer> price;
	private ArrayList<Integer> cafe_id;
	private int total_bill;
	private String date;
	
	
	
	public Order() {
		orderfood = new ArrayList<Food>();
		quantity = new ArrayList<Integer>();
		price = new ArrayList<Integer>();
		cafe_id = new ArrayList<Integer>();
		this.total_bill=0;
	}
	public Order(int a,int b,String c,Person d) {
		this.id=a;
		this.total_bill=b;
		this.date=c;
		if(d instanceof Member)
			this.ordee = new Member((Member)d);
		else
			this.ordee= new Person(d);
		
		orderfood = new ArrayList<Food>();
		quantity = new ArrayList<Integer>();
		price = new ArrayList<Integer>();
		cafe_id = new ArrayList<Integer>();
	}
	
	public int[] uniquecafe() {
		int[] count = new int[10];
		boolean present =false;
		for(int i=0;i<this.cafe_id.size() && cafe_id.get(i)!=0;i++) {
			present = false;
			for(int j=0;j<10;j++)
				if(this.cafe_id.get(i)==count[j]) {
					present =true;
					break;
				}
		if(!present)
			count[i]=this.cafe_id.get(i);
		}
		return count;
	}
	
	public void receipt() {
		LocalDate dateobj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalTime timeobj = LocalTime.now();
		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm:ss");
		
		date = dateobj.format(formatter);
		String time = timeobj.format(formatter1);
		int[] count = this.uniquecafe();
		String[] name = new String[count.length];
		for(int i=0;i<name.length && count[i]!=0;i++)
			name[i] = Cafe.cafename(count[i]);
		
		if(this.getMaxid()!=-1)
			this.id= this.getMaxid()+1;
		System.out.println("\t\tReceipt");
		System.out.println("Customer Name: "+ this.ordee.getName());
		System.out.println("Order ID: "+this.id);
		System.out.print("Ordered From: ");
		for(int i=0;i<name.length && name[i]!=null ;i++) {
			if(i>0)
				System.out.print(", "+name[i]);
			else
				System.out.print(name[i]);
		}
		System.out.println();
		System.out.println("Time: "+time);
		System.out.println("Date: "+date);
		System.out.println();
		System.out.println("S/N Food\tQuantity\tUnit Price\tTotal");
		for(int i=0;i<orderfood.size();i++)
			System.out.println((i+1)+" "+orderfood.get(i).getName()+"\t"+quantity.get(i)+"\t\t"+
			orderfood.get(i).getPrice()+"\t\t"+this.price.get(i));
		System.out.println("Bill: "+this.total_bill);
		  
	}
	
	public void showorder() {
//		int[] count = this.uniquecafe();
		String name;
//		for(int i=0;i<name.length  && count[i]!=0;i++)
		name = Cafe.cafename(cafe_id.get(0));
		itemprice();
		System.out.println("\t\tReceipt");
		System.out.println("Customer Name: "+ this.ordee.getName());
		System.out.println("Order ID: "+ this.id);
		System.out.println("Ordered From ");
//		for(int i=0;i<name.length  && name[i]!=null;i++)
		System.out.print(name+" ");
		System.out.println();
		System.out.println("Date: "+date);
		System.out.println();
		System.out.println("S/N Food\tQuantity\tUnit Price\tTotal");
		for(int i=0;i<orderfood.size();i++)
			System.out.println((i+1)+" "+orderfood.get(i).getName()+"\t"+quantity.get(i)+"\t\t"+
			orderfood.get(i).getPrice()+"\t\t"+this.price.get(i));
		System.out.println("Bill: "+this.total_bill);
	}
	public int getMaxid() {
		int max = 0;
		try {
			Connection con= Runner.connection();
			Statement stmt=con.createStatement();
	        ResultSet rs=stmt.executeQuery("select max(order_id) from orders");
	        while(rs.next())
	        	max =  rs.getInt(1);
	        con.close();
	        return max;
		}catch(Exception e) {
	        System.out.println(e);
	        return -1;
		}
		
	}
	
	public boolean addordertodb() {
		String query;
		
		try {
			Connection con= Runner.connection();
			query = "insert into orders(order_id,person_id,bill,order_date) values(?,?,?,TO_DATE(?, 'DD/MM/YY'))";
			//INSERT INTO order(date) VALUES(TO_DATE(date, 'DD/MM/YYYY'));
			PreparedStatement stmt=con.prepareStatement(query);
//		    int max=this.getMaxid();
//		    if(max!=-1)
//		    	this.id=max+1;
		    stmt.setInt(1,this.id);
			stmt.setInt(2,this.ordee.getId());
		    stmt.setInt(3,this.total_bill);
		    stmt.setString(4, this.date);
		    stmt.execute();
		    
		    
		    String query1="insert into food_in_order(order_id,food_id,cafe_id,quantity) values(?,?,?,?)"; //loop query until food items are all entered
		    PreparedStatement stmt1=con.prepareStatement(query1);
		    for(int i=0;i<this.orderfood.size();i++) {
//		    	System.out.println(this.id+" "+this.cafe_id.get(i)+" "+this.orderfood.get(i).getId()+" "+quantity.get(i));
		    	stmt1.setInt(1,this.id);
		    	stmt1.setInt(3,this.cafe_id.get(i));
		    	stmt1.setInt(2,this.orderfood.get(i).getId());
		    	stmt1.setInt(4, quantity.get(i));
		    	stmt1.addBatch();
		    }
	        stmt1.executeBatch();
	        con.close();
	    	return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
	}
	
	public boolean addfoodtodb() {
		try {
			Connection con= Runner.connection();
			String query1="insert into food_in_order(order_id,food_id,cafe_id,quantity) values(?,?,?,?)"; //loop query until food items are all entered
		    PreparedStatement stmt1=con.prepareStatement(query1);
		    for(int i=0;i<this.orderfood.size();i++) {
		    	stmt1.setInt(1,this.id);
			   	stmt1.setInt(2,this.cafe_id.get(i));
			   	stmt1.setInt(3,this.orderfood.get(i).getId());
			   	stmt1.setInt(4, quantity.get(i));
		    	stmt1.addBatch();
		    }
		    stmt1.executeBatch();
		    con.close();
		   	return true;
			}catch(Exception e) {
		        System.out.println(e);
		        return false;
			}
	}
	
	public void useprize(int i) {
		Member a = null;
		if(ordee instanceof Member)
			a = (Member) ordee;
		a.loadPrizes();
		String ans = a.getPrize().get(i).useprize();
		Cafe.getfood(ans, this);
		a.deletePrize(a.getPrize().get(i).getId());
		this.setOrdee(a);
	}
	
	public void totalbill() {
		for(int i=0; i<price.size();i++)
			this.total_bill += price.get(i);
	}
	
	public void itemprice() {
		for(int i=0; i<orderfood.size();i++)
			price.add(orderfood.get(i).getPrice()*quantity.get(i));
	}
	
	public int calc_points() {
		// (quantity of item * price)/10 + no. of items in order * 10 + bill/10
		int ans = 0;
//		itemprice();
		for(int i=0;i<this.quantity.size();i++)
			ans += this.price.get(i);
		ans/=10;
		ans+= this.quantity.size()*10;
		return ans;
	}

	public void applydiscount(int i) {
		Member a = (Member) ordee;
		double b = a.getVoucher().get(i).getPercentage();
		this.total_bill -= (this.total_bill*b/100);
	}
	
	public int foodsearch(String name) {
		for(int i=0;i<orderfood.size();i++)
			if(orderfood.get(i).getName().equals(name))
				return i;
		return -1;
	}
	
	public Person getOrdee() {
		if(ordee instanceof Member)
			return new Member((Member)ordee);
		return ordee;
	}

	
	public int getTotal_bill() {
		return total_bill;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setOrdee(Person ordee) {
		Member a;
		if(ordee instanceof Member) {
			a = (Member) ordee;
			this.ordee = new Member(a);
		}
		else
		this.ordee = new Person(ordee);
	}

	public void setTotal_bill(int total_bill) {
		this.total_bill = total_bill;
	}

	public void addquantity(int a) {
		quantity.add(a);
	}
	
	public void addfood(Food a) {
		this.orderfood.add(a);
	}
	
	public void addcafe(int a) {
		this.cafe_id.add(a);
	}
	public void addprice(int a) {
		this.price.add(a);
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public ArrayList<Food> getOrderfood() {
		return orderfood;
	}

	public ArrayList<Integer> getQuantity() {
		return quantity;
	}

	public ArrayList<Integer> getPrice() {
		return price;
	}

	public ArrayList<Integer> getCafe_id() {
		return cafe_id;
	}

	public String getDate() {
		return date;
	}
	
	
}

