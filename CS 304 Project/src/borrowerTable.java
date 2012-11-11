import java.io.*;
import java.sql.*;

public class borrowerTable {

	public borrowerTable() {
		
	}
	private Connection con;
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	// Insert a borrower into the table Borrower
	public void insertBorrower() {
		int bid;
		String password;
		String name;
		String address;
		int phone;
		String emailAddress;
		int sinOrStNo;
		Date expiryDate;
		String type;
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("INSERT INTO borrower Values(?,?,?,?,?,?,?,?,?)");
			System.out.print("\nBorrower bid: ");
			bid = Integer.parseInt(in.readLine());
			ps.setInt(1, bid);
			
			System.out.print("\nBorrower password: ");
			password = in.readLine();
			ps.setString(2, password);
			
			System.out.print("\nBorrower name: ");
			name = in.readLine();
			ps.setString(3, name);
			
			System.out.print("\nBorrower address: ");
			address = in.readLine();
			if (address.length() == 0)
			{
				ps.setString(4, null);
			}
			else {
				ps.setString(4, address);
			}
			
			System.out.print("\nBorrower phone: ");
			String phoneTemp = in.readLine();
			if (phoneTemp.length() == 0) {
				ps.setNull(5, java.sql.Types.INTEGER);
			}
			else {
				phone = Integer.parseInt(phoneTemp);
				ps.setInt(5, phone);
			}
			
			System.out.print("\nBorrower e-mail address: ");
			emailAddress = in.readLine();
			if (emailAddress.length() == 0) {
				ps.setString(6, null);
			}
			else {
				ps.setString(6, emailAddress);
			}
			
			System.out.print("\nBorrower SIN or St. No: ");
			sinOrStNo = Integer.parseInt(in.readLine());
			ps.setInt(7, sinOrStNo);
			
			System.out.print("\nBorrower expiry date: ");
			expiryDate = Date.valueOf(in.readLine());
			ps.setDate(8, expiryDate);
			
			System.out.print("\nBorrower type: ");
			type = in.readLine();
			ps.setString(9, type);
			
			ps.executeUpdate();
			con.commit();
			ps.close();			
		}
		catch (IOException e)
		{
			System.out.println("IOException!");
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
	public void deleteBorrower() {
		int bid;
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("DELETE FROM borrower WHERE borr_bid = ?");
			
			System.out.print("\nBorrower ID: ");
			bid = Integer.parseInt(in.readLine());
			ps.setInt(1, bid);
			
			int rowCount = ps.executeUpdate();
			
			if (rowCount == 0) {
				System.out.println("\nBorrower " + bid + " does not exist!");
			}
			
			con.commit();
			ps.close();
		}
		catch (IOException e)
		{
			System.out.println("IOException!");
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());

	        try 
		    {
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
		String bid;
		String password;
		String name;
		String address;
		String phone;
		String emailAddress;
		String sinOrStNo;
		String expiryDate;
		String type;
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM borrower");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCol = rsmd.getColumnCount();
			
			System.out.println(" ");
			// display column names;
			for (int i = 0; i < numCol; i++)
			{
			  // get column name and print it
		      System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			}

			System.out.println(" ");

			while(rs.next()) {
				bid = rs.getString("borr_bid");
			    System.out.printf("%-10.10s", bid);

			    password = rs.getString("borr_password");
			    System.out.printf("%-20.20s", password);
			    
			    name = rs.getString("borr_name");
			    System.out.printf("%-20.20s", name);
			    
			    address = rs.getString("borr_address");
			    if (rs.wasNull())
			    {
			    	System.out.printf("%-20.20s", " ");
			    }
			    else
			    {
			    	System.out.printf("%-20.20s", address);
			    }
			    
			    phone = rs.getString("borr_phone");
			    if (rs.wasNull())
			    {
			    	System.out.printf("%-20.20s", " ");
			    }
			    else
			    {
			    	System.out.printf("%-20.20s", phone);
			    }
			    
			    emailAddress = rs.getString("borr_emailAddress");
			    if (rs.wasNull())
			    {
			    	System.out.printf("%-20.20s", " ");
			    }
			    else
			    {
			    	System.out.printf("%-20.20s", emailAddress);
			    }
			    
			    sinOrStNo = rs.getString("borr_sinOrStNo");
			    System.out.printf("%-20.20s", sinOrStNo);
			    
			    expiryDate = rs.getString("borr_expiryDate");
			    System.out.printf("%-20.20s", expiryDate);
			    
			    type = rs.getString("borr_type");
			    System.out.printf("%-20.20s", type);

			}
			stmt.close();
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}	
	}
	
}
