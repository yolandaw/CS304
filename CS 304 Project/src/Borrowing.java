import java.io.*;
import java.sql.*;

public class Borrowing {
	
	public Borrowing() {
		
	}
	java.sql.Connection con = Connection.getInstance().getConnection();
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	// Insert a tuple into the table Borrowing
	public void insertBorrowing(int borid, int bid, int callNo, int copyNo, Date outDate, Date inDate) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO borrowing VALUES(?,?,?,?,?,?)");
			ps.setInt(1, borid);
			ps.setInt(2, bid);
			ps.setInt(3, callNo);
			ps.setInt(4, copyNo);
			ps.setDate(5, outDate);
			ps.setDate(6, inDate);
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
	
	// Delete a tuple from the table Borrowing
	public void deleteBorrowing(int borid) {
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("DELETE FROM borrowing WHERE borrowing_borid = ?");
			ps.setInt(1, borid);
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
	
	// Display all the rows of the table Borrowing
	public void displayBorrowing() {
		//TODO
	}

}
