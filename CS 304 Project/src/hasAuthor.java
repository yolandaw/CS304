import java.io.*;
import java.sql.*;

public class hasAuthor {

	public hasAuthor() {
		
	}
	
	private Connection con;
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	// Insert a tuple into the table HasAuthor
	public void insertHasAuthor(int callNo, String name) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("INSERT INTO hasAuthor VALUES (?,?)");
			ps.setInt(1, callNo);
			ps.setString(2, name);
			
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
		
	// Delete a tuple from the table HasAuthor
	public void deleteHasAuthor(int callNo, String name) {
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("DELETE FROM hasAuthor WHERE book_callNo = ? AND hasAuthor_name = ?");
			ps.setInt(1, callNo);
			ps.setString(2, name);
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
		
	// Display all the rows of the table HasAuthor
	public void displayHasAuthor() {
		int callNo;
		String name;
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM hasAuthor");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();
			System.out.println(" ");
			
			for (int i = 0; i < numCols; i++)
			  {
			      // get column name and print it

			      System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			  }

			System.out.println(" ");
			while (rs.next())
			{
				callNo = rs.getInt("book_callNo");
				System.out.printf("%-10.10s", callNo);
				name = rs.getString("hasAuthor_name");
				System.out.printf("%-10.10s", name);
			}
			stmt.close();			  
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}	
	}
}
