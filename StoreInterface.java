import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Date;

class StoreInterface {

	Connection con = null;
	String store_id;
	String cust_name;
	String cust_id;

	public StoreInterface() throws SQLException {
	
		Scanner scan = new Scanner(System.in);//reading in scanner to take input from the user
			//Connection con = null;
			String qt = " ";
			while (qt != "quit"){//overlook this, essentially created an infinite loop that will keep asking the user for input until they give correct input
			try {
				//Prompt user for username and password and will attempt to establish connection to oracle
    			System.out.print("Enter username: ");
    			String username = scan.next();
    			Console console = System.console() ;
				char[] pW = console.readPassword("Enter password(hidden on purpose): ");//did the console thing so the password will intentionally not be displayed to the screen but still inputted
				if(pW.length > 0){
					String password = new String(pW);
					
					Class.forName("oracle.jdbc.driver.OracleDriver");
    				con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", "rkl218", 
       				"Eragon#4");
       				
       				getInput();
       				con.close();
       				break;
       			} else {
       				System.out.println("Bad input");//error message
       			}
       			
       		} catch (SQLException se) {//catching the exception
       			System.out.println(se.getMessage());
       			
       		} catch (java.lang.ClassNotFoundException cnfe) {
       			System.out.println("oracle.jdbc.driver.OracleDriver class not found...");
       			
       		} 
       		
       		}
		
	}
	
	public void getInput() throws SQLException {
	
		Scanner scan = new Scanner(System.in);
	
		System.out.println("Hello employee, ready to be enslaved some more?..... alright great lets get started");
		
		try {
		
			boolean check = true;
			while(check){
				System.out.println("Please enter store_id: ");
				String s_id = scan.next();
				
				if(check_store_id(s_id)) {
					store_id = s_id;
					menu();
					check = false;
				} else {
					System.out.println("Please enter proper input");			
				}
		
			}
		
		} catch (SQLException sex) {
			System.out.println(sex.getMessage());
		}
	
	
	
	}
	
	public void menu() throws SQLException {
	
		try {
	
			Scanner scan = new Scanner(System.in);
			boolean check = true;
		
			while(check) {
				System.out.println("Enter corresponding number for the function\n(1) Sale\n(2) Check Unit Inventory");
				String opt = scan.next();
				if(opt.equals("1")){
					sale();
				} else if(opt.equals("2")){
					check_unit_inventory();
				} else {
					System.out.println("Please enter proper input");
				}
			
			
			}
		
		} catch(SQLException sex) {
			System.out.println(sex.getMessage());
			
		}
	
	
	}
	
	public boolean check_store_id(String store_id) throws SQLException {
		
		boolean check = false;
		
		try{

			Statement s=con.createStatement();
      		String q;
      		ResultSet result;
      		q = "select store_id, address from store where store_id = "+store_id;
      		result = s.executeQuery(q);
      		if (!result.next()) {
      			System.out.println ("Empty result - Please enter correct information");  				
      		} else {
        		check = true;
        		System.out.println("Successful retrieval of store_id... Thank you");
        	}
        	s.close();
 
        	
      	
		} catch (SQLException se) {
			System.out.println("caught exception");
		}
	
		return check;
	}
	
	public void sale() throws SQLException {
		
		Scanner scan = new Scanner(System.in);
		
		String[] split;
		try{
			do{
				System.out.println("Please enter customer's name and customer id (enter in format: name-customer_id)");
				String info = scan.nextLine();
				split = info.split("-");
			} while(!checkForCustomer(split));
			
			
			ArrayList<String> a_ids = new ArrayList<String>();
			
			System.out.println("Would you like to purchase a phone today?('Y' or 'y' for yes, anything else for no)");
			String opt = scan.next();
			if(opt.equals("Y") || opt.equals("y")){
				boolean check = true;
				while(check){
				System.out.println("Choose from one of the options:\n(1) View smartphones for purchase\n(2) View dumb phones for purchase");
				String option = scan.next();
				ArrayList<String> phoneList;
				if(option.equals("1")) {
					phoneList = new ArrayList<String>();
					int i = 0;
					Statement s=con.createStatement();
      				String q;
      				ResultSet result;
					q = "select distinct MANUFACTURER, MODEL, type from phone where type like '%Smart Phone%'";
      				result = s.executeQuery(q);
      				if (!result.next()) {
      					System.out.println ("Empty result.");
      				} else {
         				do {
         					System.out.println("PHONE #"+i);
          					System.out.println("MANUFACTURER: "+result.getString("MANUFACTURER"));
          					String model = result.getString("MODEL");
          					System.out.println("MODEL:        "+model);
          					phoneList.add(model);
          					i++;
        				} while (result.next());
      				}
      				while(check){
      					System.out.println("Would you like to purchase one of these phones?('Y' or 'y' for yes, anything else for no)");
      					String choice = scan.next();
      					if(choice.equals("Y") || choice.equals("y")){
      						while(check){
      							System.out.println("Please enter model from list above");
      							//scan.nextLine();
      							String modelChoice = scan.nextLine();
      							if(checkForPhone(modelChoice, phoneList)){
      								while(check){
      									System.out.println("(1) Would you like to add this to an existing account\n(2) Would you like to create a new account for this line");
      									String e_or_n = scan.next();
      									if(e_or_n.equals("1")){
      								
      										System.out.println("These are your accounts...");
      										q = "select account.type, count(*), account.account_id from account, phone_number where customer_id = "+cust_id+" and account.account_id = phone_number.ACCOUNT_ID group by account.type, phone_number.ACCOUNT_ID, account.account_id";
      										result = s.executeQuery(q);
      										i=1;
      										if (!result.next()) {
      										System.out.println ("Empty result.");
      										} else {
        	 									do {
        	 										System.out.println("Account #"+i);
        	  										System.out.println("TYPE: "+result.getString("type"));
        	  										String acc_id = result.getString("account_id");
        	  										System.out.println("LINKED PHONE NUMBERS: "+result.getString("count(*)"));
        	  										System.out.println("ACCOUNT ID: "+acc_id+"\n");
        	  										a_ids.add(acc_id);
        	  										i++;
        										} while (result.next());
      										}
      								
      					
      										System.out.println("Which account would you like to add this phone to (Keep in mind the maximum amount of lines for different types of accounts)\nPersonal is 1\nFamily is 5\nBusiness is unlimited");
      										while(check){
      											System.out.println("Please enter corresponding account ID");
      											String acc_id = scan.next();
      											if(checkForA_ID(acc_id, a_ids)){
      												if(checkForMaxNums(acc_id)){
      													System.out.println("Are you sure you want to make the purchase?\n**Phone will be given a phone number and added to selected account account**\n(Enter 'Y' or 'y' to proceed, anything else to cancel the purchase)");
      													String op = scan.next();
      													if(op.equals("Y") || op.equals("y")){
      														String newNum = newNumber();
      														String MEID = genMEID(modelChoice);
      														
      														q = "insert into phone_number values ("+acc_id+","+newNum+")";
      														s.executeUpdate(q);
      								
      														
      														q = "update phone set phone_num = "+newNum+" where MEID = '"+MEID+"'";
      														s.executeUpdate(q);
      														int si = Integer.parseInt(store_id);
      														transaction(MEID, si);
      														
      														check = false;
      														
      									
      													} else {
      														check = false;
      													}
      											
      												} else {
      													System.out.println("Please enter proper input");
      												}
      											}
      										}
      									} else if (e_or_n.equals("2")) {
      										String type = "";
      										while(check){
      											System.out.println("Enter type of account('P' for Personal(1 Line) or 'F' for Family(maximum of 5 lines) or 'B' for Business(unlimited lines)): ");
      											
												type = scan.nextLine();
												type = type.toUpperCase();
												if(type.equals("P") || type.equals("B") || type.equals("F")){
													check = false;
													if(type.equals("P")){
														type = "PERSONAL";
													} else if (type.equals("B")){
														type = "BUSINESS";
													} else {
														type = "PERSONAL";
													}
												} else {
													System.out.println("improper input... you fucking douche cunt asshole");
												}
											}
											System.out.println();
											Random rand = new Random();
       										int acct_id = rand.nextInt(99999999) + 1;
											q = "insert into Account Values ("+cust_id+",'"+type+"',"+acct_id+")";
											System.out.println("Thank you, "+cust_name+". You selected a "+type+" account type, and this account number is "+acct_id+"\n");
											s.executeUpdate(q);
											
											System.out.println("Are you sure you want to make the purchase?\n**Phone will be given a phone number and added to selected account account**\n(Enter 'Y' or 'y' to proceed, anything else to cancel the purchase)");
      										String op = scan.next();
      										if(op.equals("Y") || op.equals("y")){
      											String newNum = newNumber();
      											String MEID = genMEID(modelChoice);
      														
      											q = "insert into phone_number values ("+acct_id+","+newNum+")";
      											s.executeUpdate(q);
      								
      														
      											q = "update phone set phone_num = "+newNum+" where MEID = '"+MEID+"'";
      											s.executeUpdate(q);
      											int si = Integer.parseInt(store_id);
      											transaction(MEID, si);
      														
      											check = false;
      														
      									
      										} else {
      											check = false;
      										}
      									}
      								}
      								
      							} else {
      								System.out.println("Incorrect Input... try again");
      							}
      						}
      					}
      				}
      				
				} else if(option.equals("2")) {
					phoneList = new ArrayList<String>();
					int i = 0;
					Statement s=con.createStatement();
      				String q;
      				ResultSet result;
					q = "select distinct MANUFACTURER, MODEL, type from phone where type like '%Dumb Phone%'";
      				result = s.executeQuery(q);
      				if (!result.next()) {
      					System.out.println ("Empty result.");
      				} else {
         				do {
         					System.out.println("PHONE #"+i);
          					System.out.println("MANUFACTURER: "+result.getString("MANUFACTURER"));
          					String model = result.getString("MODEL");
          					System.out.println("MODEL:        "+model);
          					phoneList.add(model);
          					i++;
        				} while (result.next());
      				}
      				while(check){
      					System.out.println("Would you like to purchase one of these phones?('Y' or 'y' for yes, anything else for no)");
      					String choice = scan.next();
      					if(choice.equals("Y") || choice.equals("y")){
      						while(check){
      							System.out.println("Please enter model from list above");
      							scan.nextLine();
      							String modelChoice = scan.nextLine();
      							if(checkForPhone(modelChoice, phoneList)){
      								while(check){
      									System.out.println("(1) Would you like to add this to an existing account\n(2) Would you like to create a new account for this line");
      									String e_or_n = scan.next();
      									if(e_or_n.equals("1")){
      								
      										System.out.println("These are your accounts...");
      										q = "select account.type, count(*), account.account_id from account, phone_number where customer_id = "+cust_id+" and account.account_id = phone_number.ACCOUNT_ID group by account.type, phone_number.ACCOUNT_ID, account.account_id";
      										result = s.executeQuery(q);
      										i=1;
      										if (!result.next()) {
      										System.out.println ("Empty result.");
      										} else {
        	 									do {
        	 										System.out.println("Account #"+i);
        	  										System.out.println("TYPE: "+result.getString("type"));
        	  										String acc_id = result.getString("account_id");
        	  										System.out.println("LINKED PHONE NUMBERS: "+result.getString("count(*)"));
        	  										System.out.println("ACCOUNT ID: "+acc_id+"\n");
        	  										a_ids.add(acc_id);
        	  										i++;
        										} while (result.next());
      										}
      								
      					
      										System.out.println("Which account would you like to add this phone to (Keep in mind the maximum amount of lines for different types of accounts)\nPersonal is 1\nFamily is 5\nBusiness is unlimited");
      										while(check){
      											System.out.println("Please enter corresponding account ID");
      											String acc_id = scan.next();
      											if(checkForA_ID(acc_id, a_ids)){
      												if(checkForMaxNums(acc_id)){
      													System.out.println("Are you sure you want to make the purchase?\n**Phone will be given a phone number and added to selected account account**\n(Enter 'Y' or 'y' to proceed, anything else to cancel the purchase)");
      													String op = scan.next();
      													if(op.equals("Y") || op.equals("y")){
      														String newNum = newNumber();
      														String MEID = genMEID(modelChoice);
      														
      														q = "insert into phone_number values ("+acc_id+","+newNum+")";
      														s.executeUpdate(q);
      								
      														
      														q = "update phone set phone_num = "+newNum+" where MEID = '"+MEID+"'";
      														s.executeUpdate(q);
      														int si = Integer.parseInt(store_id);
      														transaction(MEID, si);
      														
      														check = false;
      														
      									
      													} else {
      														check = false;
      													}
      											
      												} else {
      													System.out.println("Please enter proper input");
      												}
      											}
      										}
      									} else if (e_or_n.equals("2")) {
      										String type = "";
      										while(check){
      											System.out.println("Enter type of account('P' for Personal(1 Line) or 'F' for Family(maximum of 5 lines) or 'B' for Business(unlimited lines)): ");
      											
												type = scan.nextLine();
												type = type.toUpperCase();
												if(type.equals("P") || type.equals("B") || type.equals("F")){
													check = false;
													if(type.equals("P")){
														type = "PERSONAL";
													} else if (type.equals("B")){
														type = "BUSINESS";
													} else {
														type = "PERSONAL";
													}
												} else {
													System.out.println("improper input... you fucking douche cunt asshole");
												}
											}
											System.out.println();
											Random rand = new Random();
       										int acct_id = rand.nextInt(99999999) + 1;
											q = "insert into Account Values ("+cust_id+",'"+type+"',"+acct_id+")";
											System.out.println("Thank you, "+cust_name+". You selected a "+type+" account type, and this account number is "+acct_id+"\n");
											s.executeUpdate(q);
											
											System.out.println("Are you sure you want to make the purchase?\n**Phone will be given a phone number and added to selected account account**\n(Enter 'Y' or 'y' to proceed, anything else to cancel the purchase)");
      										String op = scan.next();
      										if(op.equals("Y") || op.equals("y")){
      											String newNum = newNumber();
      											String MEID = genMEID(modelChoice);
      														
      											q = "insert into phone_number values ("+acct_id+","+newNum+")";
      											s.executeUpdate(q);
      								
      														
      											q = "update phone set phone_num = "+newNum+" where MEID = '"+MEID+"'";
      											s.executeUpdate(q);
      											
      											int si = Integer.parseInt(store_id);
      											transaction(MEID, si);
      														
      											check = false;
      														
      									
      										} else {
      											check = false;
      										}
      									}
      								}
      								
      							} else {
      								System.out.println("Incorrect Input... try again");
      							}
      						}
      					}
      				}
				
			
				} else {
					System.out.println("Incorrect input.. try again");
				}
			}
			}
			
		} catch (SQLException se) {
			System.out.println(se.getMessage());
		}

	}
	
	public void check_unit_inventory() throws SQLException {
		Scanner scan = new Scanner(System.in);
		try {
			int i = 1;
			Statement s=con.createStatement();
      		String q;
      		ResultSet result;
			q = "select model, manufacturer, count(*) from phone, inventory where store_id = "+store_id+" and phone.MEID = inventory.MEID group by model, manufacturer";
      		result = s.executeQuery(q);
      		if (!result.next()) {
      			System.out.println ("Empty result.");
      		} else {
       	  		do {
         			System.out.println();
          			System.out.println("MANUFACTURER-- "+result.getString("MANUFACTURER"));
       	   			System.out.println("MODEL--------- "+result.getString("MODEL"));
       	   			System.out.println("STOCK--------- "+result.getString("count(*)"));
       	   			i++;
       	 		} while (result.next());
      		}
      		
      		
      		boolean check = true;
      		while(check){
      			System.out.println("Would you like to order more phones for delivery?('Y' or 'y' for yes, anything else for no)");
      			String opt = scan.next();
      			if(opt.equals("Y") || opt.equals("y")){
      				ArrayList<String> phoneList = new ArrayList<String>();
      				while(check){
      					System.out.println("which model would you like to order?");
      					scan.nextLine();
      					String model = scan.nextLine();
      					q = "select distinct model from phone";
      					result = s.executeQuery(q);
      					if (!result.next()) {
      						System.out.println ("Empty result.");
      					} else {
       	  					do {
         						phoneList.add(result.getString("model"));
       	 					} while (result.next());
      					}
      					if(checkForPhone(model,phoneList)){
      						while(check) {
      							System.out.println("How many?(Cannot be >100");
      							if(scan.hasNextInt()){
      								String amt = scan.next();
      								int amount = Integer.parseInt(amt);
      							
      								for(int j = 0; j<amount; j++){
      									String MEID = makeMEID();
      									if(model.equals("Rage Face 8")){
      										q = "insert into phone values(null,'Frat Co.','"+model+"','"+MEID+"','Smart Phone')";
      										s.executeUpdate(q);
      									} else if (model.equals("Home Phone")){
      										q = "insert into phone values(null,'Home LLC','"+model+"','"+MEID+"','Dumb Phone')";
      										s.executeUpdate(q);
      									} else if (model.equals("Nebula 10x")){
      										q = "insert into phone values(null,'BobSung','"+model+"','"+MEID+"','Smart Phone')";
      										s.executeUpdate(q);
      									} else if (model.equals("Noob Phone")){
      										q = "insert into phone values(null,'BobSung','"+model+"','"+MEID+"','Dumb Phone')";
      										s.executeUpdate(q);
      									} else if (model.equals("I Pull 6969s")){
      										q = "insert into phone values(null,'Frat Co.','"+model+"','"+MEID+"','Smart Phone')";
      										s.executeUpdate(q);
      									} else if (model.equals("Stationary Phone")){
      										q = "insert into phone values(null,'Lame Corp.','"+model+"','"+MEID+"','Dumb Phone')";
      										s.executeUpdate(q);
      									} else if (model.equals("Ophone 6")){
      										q = "insert into phone values(null,'Orange','"+model+"','"+MEID+"','Smart Phone')";
      										s.executeUpdate(q);
      									} else {
      										System.out.println("hehehehehehe");
      									}
      									
      									q = "insert into inventory values('"+MEID+"',"+store_id+")";
      									s.executeUpdate(q);
      								}
      								
      								check = false;
      							}
      						}
      						
      					}
      				
      				}
      				
      				
      			} else {
      				System.out.println("Incorrect input... try again");
      			}
      		
      		}
      		
      		
      		
      	} catch (SQLException sex) {
      		System.out.println(sex.getMessage());
      	
      	}

	}
	
	public boolean checkForCustomer(String[] x) throws SQLException {
		boolean check = false;
		try{
			if(x.length > 2 || x.length < 2){
				check = false;
				System.out.println("Improper Input... douche");
			} else {
				Statement s=con.createStatement();
      			String q;
      			ResultSet result;
      			q = "select name, customer_id from customer where name like '%"+x[0]+"%' and customer_id = "+x[1];
      			result = s.executeQuery(q);
      			if (!result.next()) {
      				System.out.println ("Empty result - Please enter correct account info");  				
      			} else {
        			check = true;
        			cust_name = x[0];
        			cust_id = x[1];
        			System.out.println("Successful retrieval of account information... Thank you, "+x[0]);
        		}
        		s.close();
 
        	}
      	
		} catch (SQLException se) {
			System.out.println("caught exception");
		}
		
		return check;
	}
	
	public String genMEID(String modelChoice) {
		String MEID = "";
		try {
			Statement s=con.createStatement();
      		String q;
      		ResultSet result;
      		q = "select MEID from phone where phone_num is null and model = '"+modelChoice+"'";
      		result = s.executeQuery(q);
      		if (!result.next()) {
      			System.out.println ("Empty result.");
      		} else {
      			
         		do {
         			
         			MEID = result.getString("MEID");
        		} while (result.next());
        			
      		}
		} catch (SQLException sex) {
			System.out.println(sex.getMessage());
		}
		return MEID;
	}//finds the MEID of the phone that the customer entered in option 3 of the menu
	
	public String newNumber() throws SQLException {
		String number = "";
		try {
			Statement s=con.createStatement();
      		String q;
      		ResultSet result;
      		boolean check = true;
      		while(check){
      			Random rand = new Random();
      			int r = rand.nextInt((99999 - 10000) + 1) + 10000;
      			int r1 = rand.nextInt((99999 - 10000) + 1) + 10000;
      			String rr = Integer.toString(r);
      			String rr1 = Integer.toString(r1);
      			number = rr + rr1;
      			
      			q = "select PHONE_NUM from phone_number where phone_num = "+number;
      			result = s.executeQuery(q);
      			if (!result.next()) {
      				check = false;
      			}
			}
	
		} catch (SQLException sex) {
			System.out.println(sex.getMessage());
		}
		
		return number;
	
	}//generates a new phone number for the person who is buying a phone in option 3
	
	public void transaction(String MEID, int store_id) throws SQLException {
		System.out.println("In transaction");
		try {
			
			Date date= new Date();
	 		Timestamp ts = new Timestamp(date.getTime());//time
			int trans_num = 0;

			Statement s=con.createStatement();
      		String q;
      		ResultSet result;
			q = "select TRANSACTION_NUM from transaction";
      		result = s.executeQuery(q);
      		if (!result.next()) {
      			trans_num = 0;
      		} else {
         		do {
          			trans_num++;
        		} while (result.next());
      		}
      		
      		q = "insert into transaction values ("+trans_num+","+store_id+","+cust_id+",'"+MEID+"','"+ts+"')";
      		s.executeUpdate(q);

		} catch (Exception sex) {
			System.out.print(sex.getMessage());
		}
		
	}
	
	public boolean checkForPhone(String choice, ArrayList<String> phoneList){
		boolean check = false;
		for(int i = 0; i < phoneList.size(); i++){
			if(choice.equals(phoneList.get(i))){
				check = true;
			}
		}
		return check;
	}//checks for phone in an arraylist of phones
	
	public boolean checkForMaxNums(String acc_id) throws SQLException {
		boolean check = false;
		try {
			
			String type = "";
			String link_nums = "";
			Statement s=con.createStatement();
      		String q;
      		ResultSet result;
			q = "select account.type, count(*) from account, phone_number where account.account_id = "+acc_id+" and account.account_id = phone_number.ACCOUNT_ID group by account.type, phone_number.ACCOUNT_ID";
      		result = s.executeQuery(q);
      		if (!result.next()) {
      			System.out.println ("Empty result.");
      		} else {
        		 do {
        		  	type = result.getString("type");
        		  	link_nums = result.getString("count(*)");
       		 	} while (result.next());
      		}
      	
      		if(type.equals("PERSONAL")){
      			if(link_nums.equals("1")){
      				System.out.println("Account already at maxed linked phone numbers");
      			} else {
      				check = true;
      			}
      		} else if (type.equals("FAMILY")){
      			if(link_nums.equals("5")){
      				System.out.println("Account already at maxed linked phone numbers");
      			} else {
      				check = true;
      			}
      		} else if (type.equals("BUSINESS")){
      			check = true;
      		} else {
      			System.out.println("idk whats going on");
      		}
      	
      	} catch (SQLException sex) {
      		System.out.println(sex.getMessage());
      	}
	
		return check;
	}
	
	public boolean checkForA_ID(String choice, ArrayList<String> a_ids){
		boolean check = false;
		for(int i = 0; i< a_ids.size(); i++){
			if(choice.equals(a_ids.get(i))){
				check = true;
			}
		}
		return check;
	}
	
	public String makeMEID() throws SQLException {
	
		String MEID = "";
		
		Random rand = new Random();
		
		int i = 0;
		//System.out.println("outside the while");
		boolean check = true;
		while(check){
		//System.out.println("inside the while");
			String h = "";
			
			for(int j = 0; j<12; j++){
				int hex = rand.nextInt((15 - 0) + 1) + 0;
				
				switch (hex) {
					case 0:
						h = h + "0";
						break;
					case 1:
						h = h + "1";
						break;
					case 2:
						h = h + "2";
						break;
					case 3:
						h = h + "3";
						break;
					case 4:
						h = h + "4";
						break;
					case 5:
						h = h + "5";
						break;
					case 6:
						h = h + "6";
						break;
					case 7:
						h = h + "7";
						break;
					case 8:
						h = h + "8";
						break;
					case 9:
						h = h + "9";
						break;
					case 10:
						h = h + "A";
						break;
					case 11:
						h = h + "B";
						break;
					case 12:
						h = h + "C";
						break;
					case 13:
						h = h + "D";
						break;
					case 14:
						h = h + "E";
						break;
					case 15:
						h = h + "F";
						break;
				
				}//switch
				
			}//for
			//System.out.println(h);
			try {
				if(!noDups(h)){
					check = false;
					MEID = h;
				
				}
			} catch (SQLException sex) {
				System.out.println(sex.getMessage());
			}
		
		}//end while
		
		return MEID;
			
	}//this is to create a new MEID int he event of there being no more phones if the specified model
	
	public boolean noDups(String MEID) throws SQLException {
		boolean check = false;
		try{
			Statement s=con.createStatement();
      		String q;
      		ResultSet result;
			q = "select MEID from phone";
      		result = s.executeQuery(q);
      		if (!result.next()) {
      			System.out.println ("Empty result.");
      		} else {
      			int i = 0;
         		do {
         			if(MEID.equals(result.getString("MEID"))){
         				check = true;
         			}
        		} while (result.next());
        		
      		}
		
		} catch (SQLException sex) {
			System.out.println(sex.getMessage());
		}
		return check;
	
	}
	
}