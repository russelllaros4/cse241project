import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Date;

class CustomerInterface {

	Connection con = null;
	String cust_name;
	String cust_id;
	String cust_address;

	public CustomerInterface() throws SQLException {
			
			Scanner scan = new Scanner(System.in);//reading in scanner to take input from the user
			
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
		try {
			Scanner scan = new Scanner(System.in);
			boolean out = false;
       		String ans = " ";
       		System.out.println("------------------------------------------------------------------------------------------");
       		System.out.println("Hello, welcome to Jog Wireless... the best moderate speed modern telecomunications company");
    		while(!out){
				System.out.println("Enter:\n(1) if you are a returning customer\nor\n(2) if you are a new customer");
   				ans = scan.next();
       			if(ans.equals("1")) {
       				menu();
       				out = true;
       			} else if (ans.equals("2")) {
       				newCustomer();
       				menu();
       				out = true;
       			} else {
       				System.out.println("Incorrect input, returning you to the beginning");
       			}
       		}
       	} catch (SQLException se) {
       		System.out.println(se.getMessage());
       	}
	}
	
	public void menu() throws SQLException {
		Scanner scan = new Scanner(System.in);
		//find out who the customer is
		String[] split;
		try{
			do{
				System.out.println("To access your account, enter your name and customer id (enter in format: name-customer_id)");
				String info = scan.nextLine();
				split = info.split("-");
			} while(!checkForCustomer(split));
		} catch (SQLException se) {
			System.out.println("Catching exception in menu");
		}
		//display menu options, i.e. getting a bill, view call history, look at account info
		
		String choice;
		boolean check = false;
		while(!check){
			System.out.println("Choose from one of the menu options below(enter the accompanied number):\n(1) View customer and account(s) information\n(2) Update customer information or delete accounts and disconnect phone numbers\n(3) Make online purchases\n(4) View Usage history on accounts\n(5) View billing specifics\n(6) View and pay bill\n(7) Exit");
			choice = scan.next();
			switch(choice) {
				case "1":
					firstChoice();
					String s;
					System.out.print("Return to beginning of menu? (Y for YES, any other key will have program exit): ");
					s = scan.next();
					if(s.equals("Y") || s.equals("y")) {
						check = false;
					} else {
						check = true;
					}
					break;
				case "2":
					secondChoice();
					
					System.out.print("Return to beginning of menu? (Y for YES, any other key will have program exit): ");
					s = scan.next();
					if(s.equals("Y") || s.equals("y")) {
						check = false;
					} else {
						check = true;
					}
					break;
				case "3":
					thirdChoice();
					System.out.print("Return to beginning of menu? (Y for YES, any other key will have program exit): ");
					s = scan.next();
					if(s.equals("Y") || s.equals("y")) {
						check = false;
					} else {
						check = true;
					}
					break;
				case "4":
					fourthChoice();
					System.out.print("Return to beginning of menu? (Y for YES, any other key will have program exit): ");
					s = scan.next();
					if(s.equals("Y") || s.equals("y")) {
						check = false;
					} else {
						check = true;
					}
					break;
				case "5":
					fifthChoice();
					System.out.print("Return to beginning of menu? (Y for YES, any other key will have program exit): ");
					s = scan.next();
					if(s.equals("Y") || s.equals("y")) {
						check = false;
					} else {
						check = true;
					}
					break;
				case "6":
					System.out.print("Return to beginning of menu? (Y for YES, any other key will have program exit): ");
					s = scan.next();
					if(s.equals("Y") || s.equals("y")) {
						check = false;
					} else {
						check = true;
					}
					break;
				case "7":
					System.out.println("You chose 7... exiting");
					System.exit(0);
					break;
				default:
					System.out.println("Invalid input");
			}
		}
			
	}
	
	public void newCustomer() throws SQLException {
		//creates a new customer, i.e. assigns customer ID, takes down name, address
		//also creates a new account(s) for this customer
		Scanner scan = new Scanner(System.in);
		String option = "";
		boolean check = false;
		while(!check){
			System.out.println("Welcome, new customer. Would you like to open up an account with us?(Y/N) (if no, program will close)");
			option = scan.next();
			if(option.equals("Y")){
				check = true;
			} else if (option.equals("N")){
				String sure;
				System.out.println("Are you sure (Enter anything but 'Y' to say No): ");
				sure = scan.next();
				if(sure.equals("Y")){
					System.exit(0);
				}
			} else {
				System.out.println("please enter valid input");
			}
		}
		if(option.equals("Y")){
			try{
				System.out.print("Please enter name: ");
				scan.nextLine();
				String name = scan.nextLine();
				
				System.out.print("Please enter address: ");
				String address = scan.nextLine();
       			Random rand = new Random();
       			int cust_id = rand.nextInt(99999999) + 1;
       			
       			System.out.println("\nThank you, "+name+". We have created a customer id for you which is, "+cust_id);
       			String q = "insert into Customer Values ('"+name+"','"+address+"',"+cust_id+")";
      			Statement s=con.createStatement();
      			s.executeUpdate(q);
      			/*
      			if (!result.next()) System.out.println ("No Rows");
      			else {
      				System.out.println("Name		 Address");//used the string.format
        			do {
        	  			System.out.println(result.getString("name")+ " " +result.getString("address"));
        			} while (result.next());
      			}
      			*/
      			String type = "";
      			check = false;
      			while(check){
      				System.out.println("Enter type of account('P' for Personal(1 Line) or 'F' for Family(maximum of 5 lines) or 'B' for Business(unlimited lines)): ");
      				//scan.nextLine();
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
       			int acct_id = rand.nextInt(99999999) + 1;
				q = "insert into Account Values ("+cust_id+",'"+type+"',"+acct_id+")";
				System.out.println("Thank you, "+name+". You selected a "+type+" account type, and this account number is "+acct_id+"\n");
				s.executeUpdate(q);
      			s.close();

			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
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
	
	public void firstChoice() throws SQLException {
		Scanner scan = new Scanner(System.in);
		try {
			System.out.println("You chose 1... Retrieving account information...\n");
			Statement s=con.createStatement();
      		String q;
      		ResultSet result;
      		q = "select * from customer where name like '%"+cust_name+"%' and customer_id = "+cust_id;
      		result = s.executeQuery(q);
      		if (!result.next()) System.out.println ("Empty result.");
      		else {
         	 	cust_address = result.getString("address");
      		}
      	
			System.out.println("Name: "+cust_name+"\n");
			System.out.println("Customer ID: "+cust_id+"\n");
			System.out.println("Address: "+cust_address+"\n");
			
			String num_of_accounts = "";
			q = "select count(account.customer_id) as num_of_accounts from CUSTOMER, ACCOUNT where CUSTOMER.customer_id = "+cust_id+" and  customer.customer_id = ACCOUNT.customer_id";
      		result = s.executeQuery(q);
      		if (!result.next()) System.out.println ("Empty result.");
      		else {
         	 	num_of_accounts = result.getString("num_of_accounts");
      		}
      		
      		System.out.println("There are ["+num_of_accounts+"] linked accounts\n");
		
		
			System.out.println("Do you wish to view accounts?(Y/N): ");
			String choice = scan.next();
			ArrayList<String> a_ids = new ArrayList<String>();
			if(choice.equals("Y") || choice.equals("y")){
				q = "select account.type, count(*), account.account_id from account, phone_number where customer_id = "+cust_id+" and account.account_id = phone_number.ACCOUNT_ID group by account.type, phone_number.ACCOUNT_ID, account.account_id";
      			result = s.executeQuery(q);
      			int i=1;
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
			}
			
			System.out.println("Would you like to view linked phone numbers of one of your accounts?(Y for YES, any other key for NO): ");
			choice = scan.next();
			
			if(choice.equals("Y") || choice.equals("y")) {
				System.out.println("Which account would you like to view?(enter corresponding account id");
				String acc_n = scan.next();
				if(checkForA_ID(acc_n, a_ids)){
					q = "select phone_num from phone_number where account_id = "+acc_n;
      				result = s.executeQuery(q);
      				if (!result.next()) {
      					System.out.println ("Empty result.");
      				} else {
         				do {
          					System.out.println("PHONE NUMBER: "+makePhoneNum(result.getString("phone_num")));
        				} while (result.next());
      				}
				}
				
			
			}
			
			
		} catch (SQLException sex) {
			System.out.println(sex.getMessage());
		}
		
		
	}
	
	public void secondChoice() throws SQLException {
		System.out.println("You chose 2");
		System.out.println("Do you want to update personal information or you account(s) information (Enter 'P' for personal or 'A' for account): ");
		Scanner scan = new Scanner(System.in);
		String choice = scan.next();
		
		try{
			
			boolean check = false;
			while(!check){
				Statement s=con.createStatement();
      			String q;
      			ResultSet result;
				if(choice.equals("P") || choice.equals("p")){
					check = true;
      				q = "select * from customer where name like '%"+cust_name+"%' and customer_id = "+cust_id;
      				result = s.executeQuery(q);
      				if (!result.next()) System.out.println ("Empty result.");
      				else {
         	 			cust_address = result.getString("address");
      				}
					System.out.println("Here is your current information");

					System.out.println("Name: "+cust_name+"\n");
					System.out.println("Customer ID: "+cust_id+"\n");
					System.out.println("Address: "+cust_address+"\n");
					boolean in = false;
					while(!in){
						System.out.println("What would you like to change?\n(1) to change Name\n(2) to change address");
						String option = scan.next();
						if(option.equals("1")){
							in = true;
							changeName();
						
						} else if(option.equals("2")) {
							in = true;
							changeAddress();
						
						} else {
							System.out.println("Please enter correct input");
						}
				
					}
				} else if(choice.equals("A") || choice.equals("a")) {
					ArrayList<String> a_ids = new ArrayList<String>();
					System.out.println("Here are your accounts:");
					check = true;
					q = "select account.type, count(*), account.account_id from account, phone_number where customer_id = "+cust_id+" and account.account_id = phone_number.ACCOUNT_ID group by account.type, phone_number.ACCOUNT_ID, account.account_id";
      				result = s.executeQuery(q);
      				int i=1;
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
        			System.out.println("Which account would you like to view?(enter corresponding account id)");
					String acc_n = scan.next();
					boolean in = false;
					if(checkForA_ID(acc_n, a_ids)){
						while(!in){
							System.out.println("Choose from one of the options?\n(1) delete account\n(2) delete linked phone numbers");
							String option = scan.next();
							if(option.equals("1")){
								in = true;
								delete_account(acc_n);
						
							} else if(option.equals("2")) {
								in = true;
								delete_link_nums();
						
							} else {
								System.out.println("Please enter correct input");
							}
				
						}
						
					}
      			
					
				} else {
					System.out.println("Please enter correct input");
				}
			
			}
		} catch (SQLException sex) {
			System.out.println(sex.getMessage());
		}
		
	}
	
	public void thirdChoice() throws SQLException {
		Scanner scan = new Scanner(System.in);
		ArrayList<String> a_ids = new ArrayList<String>();
		try{
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
      														
      														transaction(MEID, 0);
      														
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
      											
      											transaction(MEID, 0);
      														
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
      														
      														transaction(MEID, 0);
      														
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
      											
      											transaction(MEID, 0);
      														
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
		} catch(SQLException sex){
			System.out.println(sex.getMessage());
		}
	}//online purchase
	
	public void fourthChoice() throws SQLException {
		Scanner scan = new Scanner(System.in);
		try{
			ArrayList<String> a_ids = new ArrayList<String>();
			Statement s=con.createStatement();
      		String q;
      		ResultSet result;
			q = "select account.type, count(*), account.account_id from account, phone_number where customer_id = "+cust_id+" and account.account_id = phone_number.ACCOUNT_ID group by account.type, phone_number.ACCOUNT_ID, account.account_id";
      		result = s.executeQuery(q);
      		int i=1;
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
      		
      		boolean check = true;
      		while(check){
      			i = 1;
      			System.out.println("Which account would you like to view?(enter corresponding account id");
				String acc_n = scan.next();
				if(checkForA_ID(acc_n, a_ids)){
					System.out.println("\n\nCall Usage\n--------------");
					q = "select call.S_TIME, call.E_TIME, PHONE_NUMBER.PHONE_NUM, call.DESTINATION_NUM_CALL from call, usage, PHONE_NUMBER where PHONE_NUMBER.account_id = "+acc_n+" and PHONE_NUMBER.PHONE_NUM = usage.PHONE_NUM and usage.DESTINATION_NUM_CALL = call.DESTINATION_NUM_CALL";
      				result = s.executeQuery(q);
      				if (!result.next()) {
      					System.out.println ("No usage has occurred");
      				} else {
         				do {
          					System.out.println("\nUsage Occurrence "+i);
          					System.out.println("Call began on "+result.getString("S_TIME"));
          					System.out.println("Call ended on "+result.getString("E_TIME"));
          					System.out.println("Call came from "+makePhoneNum(result.getString("PHONE_NUM")));
          					System.out.println("Call was received by "+makePhoneNum(result.getString("DESTINATION_NUM_CALL")));
          					i++;
        				} while (result.next());
      				}
      				i = 1;
      				System.out.println("\n\nText Usage\n--------------");
					q = "select text.TIME, PHONE_NUMBER.PHONE_NUM, text.DESTINATION_NUM_TEXT from text, usage, PHONE_NUMBER where PHONE_NUMBER.account_id = "+acc_n+" and PHONE_NUMBER.PHONE_NUM = usage.PHONE_NUM and usage.DESTINATION_NUM_TEXT = text.DESTINATION_NUM_TEXT";
      				result = s.executeQuery(q);
      				if (!result.next()) {
      					System.out.println ("No usage has occurred");
      				} else {
         				do {
          					System.out.println("\nUsage Occurrence "+i);
          					System.out.println("Text was sent on "+result.getString("TIME"));
          					System.out.println("Text came from "+makePhoneNum(result.getString("PHONE_NUM")));
          					System.out.println("Text was received by "+makePhoneNum(result.getString("DESTINATION_NUM_TEXT")));
          					i++;
        				} while (result.next());
      				}
      				i = 1;
      				System.out.println("\n\nData Usage\n-------------");
					q = "select internet.gbytes, PHONE_NUMBER.PHONE_NUM from internet, usage, PHONE_NUMBER where PHONE_NUMBER.account_id = "+acc_n+" and PHONE_NUMBER.PHONE_NUM = usage.PHONE_NUM and usage.gbytes = internet.gbytes";
      				result = s.executeQuery(q);
      				if (!result.next()) {
      					System.out.println ("No usage has occurred");
      				} else {
         				do {
          					System.out.println("\nUsage Occurrence "+i);
          					System.out.println("Amount of data used "+result.getString("GBYTES")+"GB");
          					System.out.println("Used by "+result.getString("PHONE_NUM"));
          					i++;
        				} while (result.next());
      				}
      				check = false;
				}
			}
		
		} catch (SQLException sex) {
			System.out.println(sex.getMessage());
		}
	
	}//view usage history
	
	public void fifthChoice() {
		System.out.println("Personal Account Type:\n- Billed a monthly rate of $50 for maximum of 50 calling hours and 50 texts\n - If user goes over 50 hours, will be billed $10 per hour over 50 hours");
		System.out.println(" - For every text over 50 user will be billed $10 per text");
		System.out.println();
		System.out.println("Family Account Type:\n- Billed a monthly rate of $250 for maximum of 250 calling hours and 250 texts\n - If user goes over 250 hours, will be billed $10 per hour over 250 hours");
		System.out.println(" - For every text over 250 user will be billed $10 per text");
		System.out.println();
		System.out.println("Business Account Type:\n- Billed a monthly rate of $500 for unlimited calling hours and unlimited texts\n");
	}//the fifth option
	
	public void sixthChoice() {
		System.out.println("Would computate a bill here");
	}//the sixth option
	
	public boolean checkForA_ID(String choice, ArrayList<String> a_ids){
		boolean check = false;
		for(int i = 0; i< a_ids.size(); i++){
			if(choice.equals(a_ids.get(i))){
				check = true;
			}
		}
		return check;
	}//checks for an account id in an array list of account_ids
	
	public boolean checkForPhone(String choice, ArrayList<String> phoneList){
		boolean check = false;
		for(int i = 0; i < phoneList.size(); i++){
			if(choice.equals(phoneList.get(i))){
				check = true;
			}
		}
		return check;
	}//checks for phone in an arraylist of phones
	
	public String makePhoneNum(String num) {
    	char[] theNum = new char[num.length()+4];
    	
    	theNum[0] = '(';
    	theNum[1] = num.charAt(0);
    	theNum[2] = num.charAt(1);
    	theNum[3] = num.charAt(2);
    	theNum[4] = ')';
    	theNum[5] = ' ';
    	theNum[6] = num.charAt(3);
    	theNum[7] = num.charAt(4);
    	theNum[8] = num.charAt(5);
    	theNum[9] = '-';
    	theNum[10] = num.charAt(6);
    	theNum[11] = num.charAt(7);
    	theNum[12] = num.charAt(8);
    	theNum[13] = num.charAt(9);
    	
    	String number = "";
    	for(int i = 0; i<theNum.length; i++){
    		number = number + theNum[i];
    	}
    	
    	return number;
	}//makes a nicely formatted phone number
	
	public void changeName() throws SQLException {
		try{
			Scanner scan = new Scanner(System.in);
			System.out.println("What would you like to change your name to? ");
			String name = scan.nextLine();
			String q = "update CUSTOMER set Name = '"+name+"' where NAME like '%"+cust_name+"%'";
      		Statement s=con.createStatement();
      		s.executeUpdate(q);
      	} catch(SQLException sex) {
      		System.out.println(sex.getMessage());
      	}
	
	}//changes customers name
	
	public void changeAddress() throws SQLException {
		try{
			Scanner scan = new Scanner(System.in);
			System.out.println("What would you like to change your address to? ");
			String address = scan.nextLine();
			String q = "update customer set address = '"+address+"' where address like '%"+cust_address+"%'";
      		Statement s=con.createStatement();
      		s.executeUpdate(q);
		} catch (SQLException sex) {
			System.out.println(sex.getMessage());
		}
		
	}//changes customers address
	
	public void delete_account(String acc_num) throws SQLException {
		System.out.println("were chilling for now");
		/*
		try{
			Scanner scan = new Scanner(System.in);
			System.out.println("Are you sure you want to delete this account? ('Y' or 'y' for yes, anything else for no)");
			String sure = scan.next();
			
			if(sure.equals("Y") || sure.equals("y")) {
				Statement s=con.createStatement();
      			String q = "select PHONE_NUM_COUNT, type from account where ACCOUNT_ID = "+acc_num;
      			ResultSet result;
      			result = s.executeQuery(q);
      		}
			
			
      	} catch(SQLException sex) {
      		System.out.println(sex.getMessage());
      	}
      	*/
	}//supposed to delete a customers account
	
	public void delete_link_nums() {
		System.out.println("were chilling for now");
		//try {
		
		//} catch (SQLException sex) {
		
		//}
	}//supposed to disconnect numbers from someones account
	
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
	}//when adding lines this check if the account has the max amount of lines
	
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
	
	}//makes sure generated MEID above is not a duplicate

	public void transaction(String MEID, int store_id) throws SQLException {
	
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


}