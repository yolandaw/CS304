import java.io.*;
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class borrowerTable {

	java.sql.Connection con = Connection.getInstance().getConnection();

	public borrowerTable() {

	}

	CastDate newDate = new CastDate();
	Calendar cal = GregorianCalendar.getInstance();

	
	// Insert a borrower into the table Borrower
	public void insertBorrower(String name, String password, String address, int phone, String email, int sinOrStNo, String borrowerType) {
		
		//TODO: generate of bid and expiryDate
		int bid = 20018;
		
		//getting current day borrower was created and then setting the expiry date to 2 years later
		cal.getTime();
		cal.add(cal.YEAR, 2); //use with newDate.getSQLDate(cal) line
		java.sql.Date issueD = java.sql.Date.valueOf("2012-12-12");

		
	
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO borrower VALUES(?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, bid);
			ps.setString(2, password);
			ps.setString(3, name);
			ps.setString(4, address);
			ps.setInt(5, phone);
			ps.setString(6, email);
			ps.setInt(7, sinOrStNo);
			ps.setDate(8, newDate.addToDate(issueD, 2000)); //newDate.getSQLDate(cal)
			ps.setString(9, borrowerType);
			ps.executeUpdate();
			con.commit();		
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
		    try 
		    {
			// undo the insert
			con.rollback();	
		    }
		    catch (SQLException ex2)
		    {
			System.out.println("Message: " + ex2.getMessage());
			System.exit(-1);
		    }

		}
	}
	
	// Delete a borrower from the table Borrower
	public void deleteBorrower(int bid) {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM borrower WHERE borr_bid = ?");
			ps.setInt(1, bid);
			ps.executeUpdate();
			con.commit();		
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
		    try 
		    {
			// undo the insert
			con.rollback();	
		    }
		    catch (SQLException ex2)
		    {
			System.out.println("Message: " + ex2.getMessage());
			System.exit(-1);
		    }

		}

	}
	
	// Display all the rows of the table Borrower
	public void displayBorrower() {
		int bid;
		String password;
		String name;
		String address;
		int phone;
		String email;
		int sinOrStNo;
		Date date;
		String type;
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM borrower");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();

			System.out.println(" ");
			
			for (int i = 0; i < numCols; i++)
			{
		      // get column name and print it
		      System.out.printf("%-20s", rsmd.getColumnName(i+1));    
			}
			
			while(rs.next()) {
				bid = rs.getInt("borr_bid");
			    System.out.printf("\n%-20.20s", bid);
			    
			    password = rs.getString("borr_password");
			    System.out.printf("%-20.20s", password);
			    
			    name = rs.getString("borr_name");
			    System.out.printf("%-20.20s", name);
			    
			    address = rs.getString("borr_address");
			    System.out.printf("%-20.20s", address);
			    
			    phone = rs.getInt("borr_phone");
			    System.out.printf("%-20.20s", phone);
			    
			    email = rs.getString("borr_emailAddress");
			    System.out.printf("%-20.20s", email);
			    
			    sinOrStNo = rs.getInt("borr_sinOrStNo");
			    System.out.printf("%-20.20s", sinOrStNo);
			    
			    date = rs.getDate("borr_expiryDate");
			    System.out.printf("%-20.20s", date);
			    
			    type = rs.getString("bt_type");
			    System.out.printf("%-20.20s", type);
			 
			}
			stmt.close();
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}
	}
	
	
	//checks if SIN or Stu. No. alreadt exists in database, if exists returns true, else false.
	public boolean checkSinOrStNo(int sinOrStNo){
		
		Statement stmt;
		ResultSet rs;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT borr_sinorstno FROM borrower WHERE borr_sinorstno = " + sinOrStNo );
			
			while(rs.next()){
				if (rs.getInt(1) == sinOrStNo){
					return true;
				}
			}
		}
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		}
		return false;
		
	}
	
	public String checkBorrowerType(int bid) {
		
		String type;
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT borrowerType.bt_type FROM borrowerType JOIN borrower ON borrower.bt_type = borrowerType.bt_type WHERE borrower.borr_bid = " + bid);
	
			while(rs.next()) {
				type = rs.getString("bt_type");
				return type;
			}
			stmt.close();
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}
		return null;
		
		
	}
	
	public int getTimeLimit(String type) {

		int timeLimit;
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT borrowerType.bt_bookTimeLimit FROM borrowerType WHERE borrowerType.bt_type = '" + type + "'");
	
			while(rs.next()) {
				timeLimit = rs.getInt("bt_bookTimeLimit");
				return timeLimit;
			}
			stmt.close();
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}
		return 0;
		
	}

	
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                borrowerTable testTable = new borrowerTable();
                testTable.insertBorrower("name", "password", "address", 1234567, "email@email.com", 11111111, "student");
                //testTable.displayBorrower();
   //             testTable.deleteBorrower(12345);
                
           //  testTable.insertBorrower("tom", "abc", "home", 333444, "ubc@email.com", 123456, "student");
               //System.out.println(testTable.checkSinOrStNo(123456));
            testTable.displayBorrower();
            testTable.checkBorrowerType(20210);
            testTable.getTimeLimit("student");
            }
        });
	}



}
