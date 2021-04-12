package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Member extends Person{
	
	
	private int points;
	private ArrayList<Prize> prize;
	private ArrayList<Discount> voucher;
	private static ArrayList<Member> members = new ArrayList<Member>();

	private Address home;
//	public Member(String a,int b,Address c) {
//		super(a,b);
//		home = new Address(c);
//	}
	
	public Member(Address a) {
		home = new Address(a);
		prize = new ArrayList<Prize>();
		voucher = new ArrayList<Discount>();
	}
	
	public Member() {
		home = new Address();
		prize = new ArrayList<Prize>();
		voucher = new ArrayList<Discount>();
	}

	public Member(Member a) {
		id = a.id;
		name = a.name;
		phone = a.phone;
		home = new Address(a.home);
		prize = a.prize;
		voucher = a.voucher;
		points = a.points;
	}
	
	public static boolean login(String username) {
		try {
			Connection con= Runner.connection();
			//Statement stmt=con.createStatement();
			PreparedStatement stmt=con.prepareStatement("select count(person_id) as ans from "
					+ "(select person_id,name from member natural join person where name=?)");
			stmt.setString(1, username);
			ResultSet rs=stmt.executeQuery();
	    while(rs.next())
	    if(rs.getInt(1)==1) {
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
	
	//make address, person in their own class
	public boolean signup() {
		//have a address in db
		try {
			Connection con= Runner.connection();
			//Statement stmt=con.createStatement();
			PreparedStatement stmt=con.prepareStatement("insert into Member(person_id,building_number, points) values(?,?,?)");  
		    int max = this.getMaxid();
		    if(max!=-1)
			stmt.setInt(1, max); //get max id from person table
			stmt.setInt(2,home.getBuilding_number());//1 specifies the first parameter in the query  
		    stmt.setInt(3,0);
	        stmt.execute();
	    con.close();
	    return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
	}
	
	public void getMemberDetails(String username) {
		try {
			Connection con= Runner.connection();
			PreparedStatement stmt=con.prepareStatement("select * from person natural join member where name=?");
			stmt.setString(1, username);
			ResultSet rs=stmt.executeQuery();
	        while(rs.next()) {
	        id = rs.getInt(1);
	        name = rs.getString(2);
	        phone = rs.getInt(3);
	        this.home.setBuilding_number(rs.getInt(4));
	        points = rs.getInt(5);  
	        }
	    con.close();
		}catch(Exception e) {
	        System.out.println(e);
		}
		
	}
	public static void showmembers() {
		System.out.println("\tMembers");
		System.out.println("ID Name Phone Address");
		for(int i=0;i<members.size();i++)
			System.out.println((i+1)+" "+members.get(i).getName()+" "+members.get(i).getPhone()+" "
			+members.get(i).getHome().toString());
		System.out.println();
	}
	public static void getallMembers() {
		try {
			Connection con= Runner.connection();
			Statement stmt=con.createStatement();
	        ResultSet rs=stmt.executeQuery("select * from person natural join member");
	        int i=0;
	        while(rs.next()) {
	        	members.add(new Member());
	        	members.get(i).setId(rs.getInt(1));
	        	members.get(i).home.setBuilding_number(rs.getInt(4));
	        	members.get(i).setPoints(rs.getInt(5));
	        	members.get(i).setName(rs.getString(2));
	        	members.get(i).setPhone(rs.getInt(3));
	        	members.get(i).home.getaddress();
	        	i++;
	        }
	    con.close();
		}catch(Exception e) {
	        System.out.println(e);
		}
		
	}

	public static boolean uniquename(String name) {
		try {
			Connection con= Runner.connection();
			Statement stmt=con.createStatement();
	        ResultSet rs=stmt.executeQuery("select name from person natural join member");
	        while(rs.next()) {
	        	if(rs.getString(1).equalsIgnoreCase(name))
	        		return false;
	        }
	    con.close();
	    return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
		
	}
	
	public boolean loadDiscounts() {
		String query;
		try {
			Connection con= Runner.connection();
			query= "select discount_percentage,code from (select person_id from member natural join person where name=?)"
					+ " natural join (member_has_discount natural join discount)";
			PreparedStatement stmt=con.prepareStatement(query);
			stmt.setString(1, this.getName());
			ResultSet rs=stmt.executeQuery();

			while(rs.next()) {
				voucher.add(new Discount(rs.getInt(1),rs.getString(2)));
			}
			con.close();
			return true;
		}catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean hasdiscount(String code) {
		for(int i=0;i<voucher.size();i++) {
			if(voucher.get(i).getCode().equals(code))
				return true;
			}
		return false;
	}
	
	public int getdiscount(String code) {
		for(int i=0;i<voucher.size();i++) {
			if(voucher.get(i).getCode().equals(code.toUpperCase()))
				return i;
		}
		return -1;
	}
	public boolean deleteDiscount(String code) {
		String query = null;
		int i;
		try {
			Connection con= Runner.connection();
			query = "delete from member_has_discount where code = ? and person_id in (select person_id from member "
					+ "natural join person where name = ?)";
			PreparedStatement stmt=con.prepareStatement(query);  
		    stmt.setString(2,this.getName());  
		    stmt.setString(1,code);
	        stmt.execute();
	        con.close();
	        for(i=0;i<voucher.size();i++) {
	        	if(voucher.get(i).getCode()==code) {
	        		voucher.set(i, null);//or remove
					break;
	        	}
			}
	    	return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
		
	}
	
	public boolean buyDiscount(String code) {
		String query;
		int i;
		if(!Discount.discountexists(code)) {
			System.out.println("This Code doesn't exist.\n");
			return false;
		}
		if(this.hasdiscount(code)) {
			System.out.println("You already have this discount code\n");
			return false;
		}
		Discount a = new Discount(code);
		a.discountdetails();
		if(this.points<a.getPrice()) {
			System.out.println("Not enough points.\n");
			return false;
		}
		try {
			Connection con= Runner.connection();
			query = "insert into member_has_discount(person_id, code) values(?,?)";
			PreparedStatement stmt=con.prepareStatement(query);  
		    stmt.setInt(1,this.getId());  
		    stmt.setString(2,code);
	        stmt.execute();
	        con.close();
	        points-=a.getPrice();
	        this.updatepoints();
	        System.out.println(a.getPrice()+ " points have been used to buy discount\nYou have "+this.points+" points now\n");
	        voucher.add(new Discount(a));
	        return true;
		}catch(Exception e) {
			System.out.println(e);
	        return false;
		}
	}

	public boolean loadPrizes() {
		String query;
		try {
			Connection con= Runner.connection();
			query= "select prize_id,prize.name from (select person_id,name from member natural join person where name=?)"
					+ " join (member_buys_prize natural join prize) using(person_id)";
			PreparedStatement stmt=con.prepareStatement(query);
			stmt.setString(1, name);
			ResultSet rs=stmt.executeQuery();

			while(rs.next()) {
				prize.add(new Prize(rs.getString(2),rs.getInt(1)));
			}
			con.close();
			return true;
		}catch(Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public boolean deletePrize(int id) {
		
		String query = null;
		int i;
		try {
			Connection con= Runner.connection();
			query = "delete from member_buys_prize where person_id in (select person_id from person "
					+ "natural join member where name=?) and prize_id = ?";
			PreparedStatement stmt=con.prepareStatement(query);  
		    stmt.setString(1,this.getName());  
		    stmt.setInt(2,id);
	        stmt.execute();
	        con.close();
	        for(i=0;i<prize.size();i++) {
	        	if(prize.get(i).getId()==id) {
	        		prize.set(i, null); //prize.remove?
					break;
	        	}
			}
	    	return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
		
	}
	public boolean prizeexists(int id) {
		for(int i=0;i<prize.size();i++) {
			if(prize.get(i).getId() == id)
				return true;
		}
		return false;
	}
	
	public boolean buyPrize(Prize a) {
		String query;
		if(this.points<a.getPrice()) {
			System.out.println("Not enough points.\n");
			return false;
		}
		if(this.prizeexists(a.getId())) {
			System.out.println("You already have this prize.\n");
			return false;
		}
		try {
			Connection con= Runner.connection();
			query = "insert into member_buys_prize(person_id, prize_id) values(?,?)";
			PreparedStatement stmt=con.prepareStatement(query);  
		    stmt.setInt(1,this.getId());  
		    stmt.setInt(2,a.getId());
	        stmt.execute();
	        con.close();
	        points-=a.getPrice();
	        this.updatepoints();
	        System.out.println(a.getPrice()+ " points have been used to buy discount\nYou have "+this.points+" points now\n");
	        prize.add(new Prize(a));
			
	    	return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
	}
	
	public boolean updatepoints() {
		try {
			Connection con= Runner.connection();
			String query = "update member set points=? where person_id =?";
			PreparedStatement stmt=con.prepareStatement(query);
			stmt.setInt(1, this.points);
			stmt.setInt(2, this.getId());
	        stmt.execute();
	        con.close();
	        
	    	return true;
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
	}
	
	public void displayDiscount() {
		if(voucher.size()==0)
			System.out.println("You have no discounts right now. You can buy them.\n");
		else {
		System.out.println("\n   Discounts");
		System.out.println("S/N  Code Percent");
		for(int i=0; i<voucher.size();i++)
			System.out.println((i+1)+".\t"+voucher.get(i).getCode()+"\t"+voucher.get(i).getPercentage()+"%");
		}
		System.out.println();
	}
	
	public void displayPrize() {
		if(prize.size()==0)
			System.out.println("You have no prizes right now. You can buy them.\n");
		else {
		System.out.println("\n  Prizes");
		System.out.println("S/N  Prize");
		for(int i=0; i<prize.size();i++)
			System.out.println((i+1)+". "+prize.get(i).getName());
		}
		System.out.println();
	}
	
	
	public static ArrayList<Member> getMembers() {
		return members;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public Address getHome() {
		return new Address(home);
	}

	public void setHome(Address home) {
		this.home = new Address(home);
	}

	public Member(String a, int b) {
		super(a,b);
		prize = new ArrayList<Prize>();
		voucher = new ArrayList<Discount>();
	}
	public ArrayList<Prize> getPrize() {
		return prize;
	}

	public void setPrize(ArrayList<Prize> prize) {
		this.prize = prize;
	}

	public ArrayList<Discount> getVoucher() {
		return voucher;
	}

	public void setVoucher(ArrayList<Discount> voucher) {
		this.voucher = voucher;
	}

	public int getPoints() {
		return points;
	}

}
