import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Scanner;

class rkl218 {
	
	public static void main (String[] arg) throws Exception {
		Scanner scan = new Scanner(System.in);
		boolean exit = false;
		while(!exit){
			System.out.println("Welcome to the Jog Wireless System. Function as?\n(1) An online customer\n(2) A store clerk or manager\n(3) Submitting usage records\nEnter corresponding number");
       		String choice = scan.next();
       		if(choice.equals("1")){
       			CustomerInterface inf = new CustomerInterface();
       			System.out.println("Exit System? ('yes' to exit, anything else to return to beginning)");
       			String e = scan.next();
       			if(e.equals("YES") || e.equals("Yes") || e.equals("yes") || e.equals("YEs")){
       				exit = true;
       			}
       		} else if(choice.equals("2")){
       			StoreInterface si = new StoreInterface();
       			System.out.println("Exit System? ('yes' to exit, anything else to return to beginning)");
       			String e = scan.next();
       			if(e.equals("YES") || e.equals("Yes") || e.equals("yes") || e.equals("YEs")){
       				exit = true;
       			}
       		} else if(choice.equals("3")){
       			UsageRecords ur = new UsageRecords();
       			System.out.println("Exit System? ('yes' to exit, anything else to return to beginning");
       			String e = scan.next();
       			if(e.equals("YES") || e.equals("Yes") || e.equals("yes") || e.equals("YEs")){
       				exit = true;
       			}
       		} else {
       			System.out.println("Error... Please enter a valid option");
       		}
		
		}
  	}
  	
}