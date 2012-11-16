import java.io.*;
import java.sql.*;

public class borrowerType {
	
	public borrowerType() {
		
	}
	
	private Connection con;
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	// Insert a tuple into the table BorrowerType
	public void insertBorrowerType(String type, Date bookTimeLimit) {
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("INSERT INTO borrowerType VALUES (?,?)");
			
			System.out.print("\nBorrower Type: ");
			type = in.readLine();
			ps.setString(1, type);
			
			System.out.print("\nBook Time Limit: ");
			bookTimeLimit = Date.valueOf(in.readLine());
			ps.setDate(2, bookTimeLimit);
			
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
		
	// Delete a tuple from the table BorrowerType
	public void deleteBorrowerType(String type) {
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("DELETE FROM borrowerType WHERE bt_type = ?");
			System.out.print("\nBorrower Type: ");
			type = in.readLine();
			ps.setString(1, type);
			
			int rowCount = ps.executeUpdate();
			
			if (rowCount == 0)
			{
				System.out.println("\nBorrower Type " + type + " does not exist!");
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
		
	// Display all the rows of the table BorrowerType
	public void displayBorrowerType() {
		String type;
		String bookTimeLimit;
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM borrowerType");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCol = rsmd.getColumnCount();
			System.out.println(" ");
			
			for (int i = 0; i < numCol; i++)
			  {
			      // get column name and print it

			      System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			  }

			System.out.println(" ");
			
			while(rs.next()) {
				type = rs.getString("bt_type");
				System.out.printf("%-10.10s", type);
				
				bookTimeLimit = rs.getString("bt_bookTimeLimit");
				System.out.printf("%-10.10s", bookTimeLimit);
			}
			stmt.close();
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}
	}

}
