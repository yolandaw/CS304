import java.io.*;
import java.sql.*;

public class borrowerType {
	
	public borrowerType() {
		
	}
	
	java.sql.Connection con = Connection.getInstance().getConnection();
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	// Insert a tuple into the table BorrowerType
	public void insertBorrowerType(String type, Date bookTimeLimit) {
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("INSERT INTO borrowerType VALUES (?,?)");
			ps.setString(1, type);
			ps.setDate(2, bookTimeLimit);			
			ps.executeUpdate();
			con.commit();
			ps.close();
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
			ps.setString(1, type);
			ps.executeUpdate();
			con.commit();
			ps.close();
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
