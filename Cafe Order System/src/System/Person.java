package System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Person {

	protected int id;
	protected String name;
	protected int phone;
	public Person() {
	}

	public Person(int id) {
		this.id=id;
	}
	
	public Person(String a,int b) {
		name=a;
		phone=b;
	}
	
	public Person(Person a) {
		name=a.name;
		phone=a.phone;
		id = a.id;
	}
	public boolean findid() {
		try {
			Connection con= Runner.connection();
			//Statement stmt=con.createStatement();
			PreparedStatement stmt=con.prepareStatement("select name from Person where person_id=?");  
			stmt.setInt(1,this.id);
	        ResultSet rs =stmt.executeQuery();
	        while(rs.next()) {
	        	name = rs.getString(1);
	        }
	        	
	        con.close();
	        return true;
			
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}	
	}
	
	public boolean addperson() {
		try {
			Connection con= Runner.connection();
			//Statement stmt=con.createStatement();
			PreparedStatement stmt=con.prepareStatement("insert into Person(person_id,name, phone) values(?, ?, ?)");  
		    int max = this.getMaxid();
			if(max!=-1)
		    stmt.setInt(1, max+1); //get max id + 1
			this.id=max+1;
			stmt.setString(2,this.name);//1 specifies the first parameter in the query  
		    stmt.setInt(3,this.phone);
	        stmt.execute();
	        con.close();
	        return true;
			
		}catch(Exception e) {
	        System.out.println(e);
	        return false;
		}
		
	}
	
	public int getMaxid() {
		int max = 0;
		try {
			Connection con= Runner.connection();
			Statement stmt=con.createStatement();
	        ResultSet rs=stmt.executeQuery("select max(person_id) from person");
	        while(rs.next())
	        	max =  rs.getInt(1);
	        con.close();
	        return max;
		}catch(Exception e) {
	        System.out.println(e);
	        return -1;
		}
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
