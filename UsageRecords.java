import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.lang.StringBuilder;

class UsageRecords {

	ArrayList<String> parsers;
	int position;
	
	Connection con = null;
	
	String err_name;

	public UsageRecords() throws Exception {
		
		parsers = new ArrayList<String>();
		
		
		Scanner scan = new Scanner(System.in);
		String qt = " ";
		while (qt != "quit"){
			try {
				//Prompt user for username and password and will attempt to establish connection to oracle
    			System.out.println("Enter username: ");
    			String username = scan.next();
    			Console console = System.console() ;
				char[] pW = console.readPassword("Enter password(hidden on purpose): ");//did the console thing so the password will intentionally not be displayed to the screen but still inputted
				if(pW.length > 0){
					String password = new String(pW);
					
					Class.forName("oracle.jdbc.driver.OracleDriver");
    				con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241", "rkl218", "Eragon#4");
       			
       				
       					
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
		System.out.println("Ready to submit usage records?? Great!");
		
		boolean check = true;
		while(check){
			System.out.println("Please enter the name of the file that the program will be reading in (format 'example.txt')");
			String file = scan.next();
			if(readFile(file)){
				System.out.println("file name entered incorrectly or does not exist in directory");
			} else {
				System.out.println("Please enter the name of the file that the program will be writing error reports to (format 'example.txt')");
				err_name = scan.next();
				readFile(file);
				check = false;
			}
		
		}
		
	
		
	}
	
	public boolean readFile(String file) throws Exception {
		boolean check = true;
		position = 1;
		String line = "";
		
		try {
            
            FileReader fileReader = new FileReader(file);

            
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
				insertFile(line);
				
            }   
            
            bufferedReader.close(); 
            check = false;        
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + file + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" + file + "'");                  
        
        }
		
		return check;
	}
	
	public void insertFile(String line) throws Exception {
		
		String phone_num = "";
		String destination_num = "";
		String time_text = "";
		String s_time = "";
		String e_time = "";
		String gbytes = "";
		String u_or_d = "";
		
		String[] inputs = line.split(",");
		
		if(!find_errors(inputs)){
			
			
			if(line.length() > 0){
				
				
				phone_num = inputs[0];
			
				if(inputs[1].equals(" UPLOAD")) {
					u_or_d = "UPLOAD";
					gbytes = inputs[2];
				} else if (inputs[1].equals(" DOWNLOAD")) {
					u_or_d = "DOWNLOAD";
					gbytes = inputs[2];
				} else {
					destination_num = inputs[1];
				}
				
				if(inputs.length > 3) {
					if(isTimeStampValid(inputs[3])){
						
						s_time = inputs[2];
						e_time = inputs[3];
						
					} else {
						time_text = inputs[2];
					}
				}
				
			
			}
			
			System.out.println("Phone_num = "+phone_num+" | destination_num = "+destination_num+" | text_time = "+time_text+" | start_time = "+s_time+" | end_time = "+e_time+" | gbytes = "+gbytes+" | up or down = "+u_or_d);
			
			
			
			try {
				Statement s=con.createStatement();
      			String q;
      			ResultSet result;
      			
      			if(inputs.length == 3){
      			
      				q = "insert into internet values ("+gbytes+")";
      				s.executeUpdate(q);
      			
      				q = "insert into usage values ("+gbytes+",null,null,"+phone_num+")";
      				s.executeUpdate(q);
      			}
      			
      			if(inputs.length == 4){
      				if(isTimeStampValid(inputs[3])){
      				
      					q = "insert into text values ("+time_text+","+destination_num+")";
      					s.executeUpdate(q);
      			
      					q = "insert into usage values (null,"+destination_num+",null,"+phone_num+")";
      					s.executeUpdate(q);
      					
      				} else {
      					q = "insert into usage values (null,null,"+destination_num+","+phone_num+")";
      				}
      			}
      			
      			
      		} catch (SQLException sex){
      			System.out.println(sex.getMessage());
      		}
      		
      	}
		
	}
	
	public void printErrors(ArrayList<String> errors) throws Exception {
  		
  	
  		try{
  			PrintWriter out = new PrintWriter(new FileWriter(err_name, true), true);
  			for(int i = 0; i < errors.size(); i++){
  				out.println(errors.get(i));
  			}
  			
		} catch(Exception er) {
			System.out.println("Unable to write errors to specified file name");
		}  		
  			
  		//System.out.println(nums+" ---"+account_num);
  	}
	
	public boolean find_errors(String[] inputs) throws Exception {

		ArrayList<String> errors = new ArrayList<String>();
		
		
		
		boolean check = false;
		String phone_num = "";
		String destination_num = "";
		String time_text = "";
		String text_bytes = "";
		String s_time = "";
		String e_time = "";
		String gbytes = "";
		String u_or_d = "";
		
		try {
		
			if(inputs.length == 3 || inputs.length == 4){
				StringBuilder sb;
				
				if(inputs[0].indexOf(' ') > -1){
					sb = new StringBuilder(inputs[0]);
					sb.deleteCharAt(0);
					inputs[0] = sb.toString();
				}
	
				if(inputs[1].indexOf(' ') > -1){
					sb = new StringBuilder(inputs[1]);
					sb.deleteCharAt(0);
					inputs[1] = sb.toString();
				
				}
				
				if(inputs[2].indexOf(' ') > -1){
					sb = new StringBuilder(inputs[2]);
					sb.deleteCharAt(0);
					inputs[2] = sb.toString();
				
				}
				
				if(inputs.length == 4 && inputs[3].indexOf(' ') > -1){
					sb = new StringBuilder(inputs[3]);
					sb.deleteCharAt(0);
					inputs[3] = sb.toString();
				
				}
				
				phone_num = inputs[0];
				
				if ((phone_num.matches("[0-9]+") && phone_num.length() == 10) ) {
					
					if(check_check_for_dups(phone_num)){
						errors.add(phone_num+" --- error in source phone number at line "+position);
						check = true;
					}
					errors.add(phone_num+" --- error in source phone number at line "+position);
				}
				
				if (!(phone_num.matches("[0-9]+") && phone_num.length() == 10) ) {
					
					check = true;
					errors.add(phone_num+" --- error in source phone number at line "+position);
				}
			
				if(inputs.length == 3) {
					u_or_d = inputs[1];
					gbytes = inputs[2];
					if(!inputs[1].equals("DOWNLOAD") || !inputs[1].equals("UPLOAD")){
						check = true;
						errors.add(u_or_d+" --- error in type of usage at line "+position);
					} else if(!gbytes.matches("[0-9]+")){
						check = true;
						errors.add(gbytes+" --- error in bytes of usage at line "+position);
					}
				
				}
			
			
				if (inputs.length == 4) {
					destination_num = inputs[2];
					if(!(destination_num.matches("[0-9]+") && destination_num.length() == 10)) {
						check = true;
						errors.add(destination_num+" --- error in destination phone number at line "+position);
					}
				
					if(isTimeStampValid(inputs[3])){
						
						s_time = inputs[2];
						e_time = inputs[3];
					
						if(!isTimeStampValid(s_time)){
							check = true;
							errors.add(s_time+" --- error in starting time for phone call at line "+position);
					
						} 
					
						if(!isTimeStampValid(e_time)){
							check = true;
							errors.add(e_time+" --- error in ending time phone call at line "+position);
					
						}
						
					} else {
						time_text = inputs[2];
					
						if(!isTimeStampValid(time_text)){
							check = true;
							errors.add(time_text+" --- error in time of text message at line "+position);
					
						}
					
						text_bytes = inputs[3];
						if(!text_bytes.matches("[0-9]+")){
							check = true;
							errors.add(time_text+" --- error in time of text message at line "+position);
						}
					}	
				}
			} else {
		
				check = true;
				errors.add("error in length of the line at position "+position);	
		
			
			}
		
			if(errors.size() > 0){
				for(int i = 0; i < errors.size(); i++){
					printErrors(errors);
				}
		
			}
		
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		position++;
		
		return check;
	}
	
	public boolean isTimeStampValid(String timeStamp) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            format.parse(timeStamp);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    public boolean check_check_for_dups(String phone_num) throws SQLException {
    	boolean check = false;
    	try {
			Statement s=con.createStatement();
      		String q;
      		ResultSet result;
    		q = "select phone_num from PHONE_NUMBER";
      		result = s.executeQuery(q);
      		if (!result.next()) {
      			System.out.println ("Empty result.");
      		} else {
       			do {
        			if(result.getString("phone_num").equals(phone_num)){
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