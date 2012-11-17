import java.io.*;
import java.sql.*;

public class bookCopy {
	
	public bookCopy() {
		
	}
	java.sql.Connection con = Connection.getInstance().getConnection();
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	// Insert a tuple into the table BookCopy
	public void insertBookCopy(int callNo, int copyNo, int status) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("INSERT INTO bookCopy VALUES (?,?,?)");
			ps.setInt(1, callNo);
			ps.setInt(2, copyNo);
			ps.setInt(3, status);
			
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
	
	// Delete a tuple from the table BookCopy
	public void deleteBookCopy(int callNo, int copyNo) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("DELETE FROM bookCopy WHERE book_callNo = ? AND bookCopy_copyNo = ?");
			ps.setInt(1, callNo);
			ps.setInt(2, copyNo);
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
		
	// Display all the rows of the table BookCopy
	public void displayBookCopy() {
		int callNo;
		int copyNo;
		int status;
		Statement stmt;
		ResultSet rs;
		
//		try {
//			stmt = con.createStatement();
//			rs = stmt.executeQuery("SELECT * FROM bookCopy");
//			ResultSetMetaData rsmd = rs.getMetaData();
//			int numCols = rsmd.getColumnCount();
//			System.out.println("");
//			for (int i = 0; i < numCols; i++)
//			{
//				// get column name and print it
//
//				System.out.printf("%-15s", rsmd.getColumnName(i+1));  
//			}
//			System.out.println("");
//			
//			while (rs.next()) {
//				callNo = rs.getInt("book_callNo");
//				System.out.printf("%-10.10s", callNo);
//				
//				copyNo = rs.getInt("bookCopy_copyNo");
//				
//			}
//
//		}
	}

}
