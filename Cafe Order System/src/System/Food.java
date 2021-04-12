package System;

public class Food {

	private int id;
	private String name;
	private String type;
	private int price;
	//private Cafe ordered_from;
	public Food() {
		
	}
	
	public Food(int id, String a,String b,int c) {
		this.id=id;
		name=a;
		type=b;
		price=c;
	}
	public Food(String a,String b,int c) {
		name=a;
		type=b;
		price=c;
	}
	
	public Food(Food a) {
		id=a.id;
		name=a.name;
		type=a.type;
		price=a.price;
	}
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getPrice() {
		return price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setPrice(int price) {
		this.price = price;
	}

	
	
}
