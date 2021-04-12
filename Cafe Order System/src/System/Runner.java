package System;
import java.sql.*;
import java.util.Scanner;


public class Runner {
	static int cho,id,quantity,phone;
	static String code,yes,name=null;
	static String[] address= new String[3];
	static Scanner in= new Scanner(System.in);
	
	// donations remove [pk], fix prizes
	// database make, check every functionality
	public static void main(String[] args) {		
	
		do {
	do {
		System.out.println("\n\t\tMain Menu");
		System.out.println("1.Make order\t2.Member Menu\t3.Admin Menu");
		System.out.println("0.Exit");
		cho=in.nextInt();
	}while(cho<0 || cho>3);
	
		switch(cho) {
		case 1:
			ordermenu();
			break;
		case 2:
			membermenu();
			break;
		case 3:
			adminmenu();
			break;
		case 0:
			System.out.println("Exiting... ");
			break;
		default:
		}
	}while(cho!=0);
	
	in.close();
	}
	
	public static void ordermenu() {
		//a person orders
		Order order = new Order();
		// show cafes available
		int c_id;
		do {
			System.out.println("\nChoose a Cafe");
			Cafe.showcafes();
			System.out.println("0. Complete Order");
			System.out.print("\nEnter its ID: ");
			id = in.nextInt(); //chose cafe by id
			c_id=id;
			if(c_id!=0) {
			Cafe cafe = new Cafe(c_id);
			System.out.println();
		
			//show menu from selected cafe
			cafe.getfood();
			do{
				id = orderfood(cafe,order);
				if(id!=0)
					order.addcafe(c_id);
			}while(id!=0);
			}
		}while(c_id!=0);
		if(order.getCafe_id().size()==0)
			return;
		order.itemprice();
		order.totalbill();
		//after ordering complete, ask if member or customer
		//member, applying discount code
		System.out.println("Are you a\n1.Customer\t2.Member ");
		id = in.nextInt();
		//make appropriate object for member or customer
		if(id==1) {
			//customer
			//give name, phone is optional
			//Customer b = new Customer();
			System.out.print("\nEnter your name: ");
			name = in.next();
			System.out.print("Enter your Phone no. in 9-digits: ");
			phone = in.nextInt();
			Person ordee = new Person();
			order.setOrdee(new Person(ordee));
			
			order.getOrdee().setName(name); //make max  id for person, give id to person obj
			//ordee.setName(name);
			order.getOrdee().setPhone(phone);
			//ordee.setPhone(phone);
			order.getOrdee().addperson();
		}
		else if(id==2) {
			memberoptions(order);
			}
		else
			System.out.println("Invalid Option. ");//invalid option, take input again
		System.out.println();
		order.receipt();
		order.addordertodb();
		
	}
	
	public static int orderfood(Cafe cafe, Order order) {
		cafe.showmenu();
		
		//person chooses an item, get name, price, type, quantity,add it to array lists
		System.out.print("Enter ID of Food you want to buy: ");
		id = in.nextInt(); //chose food item by a number
		
		if(id!=0) {
			order.addfood(new Food(cafe.getFood().get(id))); //add details to the food by adding a present food object in cafe to food list in order
			
			System.out.print("How many "+cafe.getFood().get(id).getName() +"? "); //order.getfood.get(id).getname()
			quantity = in.nextInt(); //choose quantity of it
			order.addquantity(quantity);
		}
		System.out.println();
		//resume from menu from cafe, add option to select a different cafe
//		System.out.println("Do you want to Buy from the same cafe (y/n): ");
//		yes = in.next();
		return id;
	}
	
	public static void memberoptions(Order order) {
		//ask name, login
		System.out.print("Enter your name: ");
		name = in.next();
		
		//if member then
		if(Member.login(name)) { //input username
			System.out.println("Logged in.");
			Member a= new Member(); 
			a.getMemberDetails(name);
			order.setOrdee(a);
//			order.getOrdee().setName(a.getName());
//			order.getOrdee().setPhone(a.getPhone());
			
			System.out.print("Do you want to use a prize (y/n): ");
			yes = in.next();
			if(yes.equalsIgnoreCase("y")) {
				System.out.println();
				a.loadPrizes();
				a.displayPrize();
				System.out.println();
				System.out.print("Enter id for the prize to use: ");
				
				id=in.nextInt();
				order.useprize(id-1);
				System.out.println("Prize Cashed in.");
			}
			//ask if apply discount or use a prize
			System.out.print("\nDo you want to use a discount code (y/n): ");
			yes = in.next();
			//if yes, enter code
			if(yes.equalsIgnoreCase("y")) {
				System.out.println();
				a.loadDiscounts();
				a.displayDiscount();
				System.out.print("Enter code: ");
				code = in.next();
			int ind = a.getdiscount(code.toUpperCase());
			if(ind!=-1){
				//get discount percent, apply it to whole bill
				//get discount percent, * with bill
				System.out.println("Bill: "+order.getTotal_bill());
				order.applydiscount(ind);
				System.out.println("Discounted Bill: "+order.getTotal_bill());
				System.out.println("Discount has been applied");
				a.deleteDiscount(code.toUpperCase());
			}
			else
				System.out.println("You don't have this discount code");
			}
			
			
			a.setPoints(a.getPoints()+order.calc_points());
			System.out.println("\nPoints have been updated to "+a.getPoints());	
			a.updatepoints();
		
		}
		else
			System.out.println("You don't have membership. ");
	}
	
	public static void membermenu() {
		System.out.println("1.Log-in");
		System.out.println("2.Sign up");
		id = in.nextInt();
		if(id==1) {
			System.out.print("Enter your name: ");
			name = in.next();
			if(Member.login(name)) { //input username
				System.out.println("Logged in.\n");
				while(login()!=0)
					;
				}
			else
				System.out.println("Not a member.");
				//else ask for signup or reenter name
			}
		else if(id==2){
			signup();
		}
		
	}

	public static void signup() {
		do{
			System.out.print("Enter a unique username: ");
			name= in.next();
		}while(!Member.uniquename(name));
		System.out.println();
		System.out.print("Enter your phone in 9-digits: ");
		phone = in.nextInt();
		System.out.println();
		System.out.println("Input your Address e.g \nHouse no. Street City\n101,St30,G6/2, Islamabad\n");
		System.out.print("Enter your House No.: ");
		address[0] = in.next();
		System.out.println();
		System.out.print("Enter your Street: ");
		address[1] = in.next();
		System.out.println();
		System.out.print("Enter your City: ");
		address[2] = in.next();
		System.out.println();
		Member member = new Member(name,phone);
		Address home = new  Address(Integer.parseInt(address[0]),address[1],address[2]);
		member.setHome(new Address(home));
		
		home.newaddress(); //add address to db 
		member.addperson(); //add person to db 
		member.signup(); //add member to db
		System.out.println("Successfully signed up.");
	}
	
	public static int login() {
		Member member= new Member();
		member.getMemberDetails(name);
		member.getHome().getaddress(); //get address members from db then do this
		
		System.out.println("1.My Discounts");
		System.out.println("2.My Prizes");
		System.out.println("3.My Points");
		System.out.println("4.Buy Discounts");
		System.out.println("5.Buy Prizes");
		System.out.print("0.Go Back ");
		id = in.nextInt();
		switch(id) {
		case 1:
			if(member.loadDiscounts())
				member.displayDiscount();
			else
				System.out.println("Discounts can't be loaded");
			break;
		case 2:
			if(member.loadPrizes())
				member.displayPrize();
			else
				System.out.println("Prizes can't be loaded");
			break;
		case 3:
			System.out.println("\nYour Points: "+member.getPoints()+"\n");
			break;
		case 4:
			Discount.showalldiscounts();
			member.loadDiscounts();
			System.out.print("Enter code of Discount to buy: ");
			code = in.next();
			member.buyDiscount(code.toUpperCase());
//			System.out.println("You already have this discount code");
			break;
		case 5:
			Prize.showallprizes();
			member.loadPrizes();
			do {
			System.out.print("Enter ID of Prize to buy: ");
			id = in.nextInt();
			}while(id<1 || id>7);
			Prize prize = new Prize(); // if id is in the possible values
			prize.setId(id);
			prize.getdetails(id);
			member.buyPrize(prize);
			break;
		case 0:
			break;
		default:
			System.out.println("Invalid Option. ");
		}
		return id;
	}
	
	public static void adminmenu() {
		
		System.out.print("Enter username (admin): ");
		String user = in.next();
		if(user.equalsIgnoreCase("admin")) {
			System.out.println("\nHello Admin\n");
			do {
			System.out.println("1. Calculate Cafe's profit"); 
			System.out.println("2. Give Donation to charity");
			System.out.println("3. Cafes Order list");
			System.out.println("4. Members list");
			System.out.println("5. Cafe profits");
			System.out.println("6. Charity donations"); 
			System.out.println("0. Go Back");
			id=in.nextInt();
			switch(id) {
			case 1:
				System.out.println("Choose a Cafe");
				Cafe.showcafes();
				System.out.print("Enter an ID: ");
				id=in.nextInt();
				Cafe cafe = new Cafe(id);
				cafe.cafedetails();
				cafe.getorders();
				System.out.print("\nChoose a Month-Year (MM-YY): ");
				yes = in.next();
				System.out.println();
				int amount = cafe.profit_calc(yes);
				System.out.println("Profit: "+amount);
				System.out.println();
				Profit ans = new Profit(amount,yes,cafe.getID());
				ans.addtodb();
				break;
			case 2:
				Charity.showcharity();
				System.out.print("Choose one: ");
				id=in.nextInt();
				System.out.println();
				Charity charity= new Charity(id);
				charity.getdetails();
				System.out.println("\nChoose Donating Cafe");
				Cafe.showcafes();
				System.out.print("Choose one: ");
				id=in.nextInt();
				Cafe cafe1= new Cafe(id);
				cafe1.cafedetails();
				cafe1.getorders();
				if(cafe1.getBalance()==0)
					cafe1.setbalance();
				System.out.println("Balance: "+cafe1.getBalance());
				System.out.print("Choose amount: ");
				id=in.nextInt();
				if(cafe1.donate_amount(id, charity.getId()))
					System.out.println("Donated this amount");//donate.addtodb(id);
				else
					System.out.println("Can't donate this amount, it exceeds the cafe profits. ");
				break;
			case 3:
				Cafe.showcafes();
				System.out.print("Choose one: ");
				id=in.nextInt();
				Cafe cafe2= new Cafe(id);
				cafe2.cafedetails();
				cafe2.getorders();
				cafe2.showorders();
				break;
			case 4:
				Member.getallMembers();
				Member.showmembers();
				break;
			case 5:
				Cafe.showcafes();
				System.out.print("Choose one: ");
				id=in.nextInt();
				System.out.println();
				Cafe cafe3= new Cafe(id);
				cafe3.cafedetails();
				Profit.getprofits(id);
				Profit.showprofits(cafe3);
				System.out.println();
				break;
			case 6:
				Charity.showcharity();
				System.out.print("Choose one: ");
				id=in.nextInt();
				System.out.println();
				Charity charity1= new Charity(id);
				charity1.getdetails();
				Donations.charitydonated(id);
				System.out.println();
				break;
			default:
			}
			}while(id!=0);
		}
		else
			System.out.println("Not Admin. Returning to main menu.....");
	}

	public static Connection connection() {
		try {
	       return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","system","oracle");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void sample() {
		try {
			Connection con= connection();
			Statement stmt=con.createStatement();
	        ResultSet rs=stmt.executeQuery("select * from cafe");
	    while(rs.next())
	        System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getInt(3));
	    con.close();
		}catch(Exception e) {
	        System.out.println(e);
		}
	}
}
