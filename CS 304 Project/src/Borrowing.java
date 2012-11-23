import java.io.*;
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Borrowing {
	
	public Borrowing() {
		
	}
	java.sql.Connection con = Connection.getInstance().getConnection();
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	

	CastDate newDate = new CastDate();
	
	// Insert a tuple into the table Borrowing
	public void insertBorrowing(int borid, int bid, int callNo, int copyNo, Date inDate) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO borrowing VALUES(?,?,?,?,?,?)");
			ps.setInt(1, borid);
			ps.setInt(2, bid);
			ps.setInt(3, callNo);
			ps.setInt(4, copyNo);
			ps.setDate(5, newDate.currentDate());
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
		
	
	public void setInDate(int borid){
		
		PreparedStatement ps; 
		
		try{
		ps = con.prepareStatement("UPDATE borrowing SET borrowing_indate = ? WHERE  borrowing_borid = " + borid);

		ps.setDate(1, newDate.currentDate());
		ps.executeUpdate();
		con.commit();
		ps.close();
		
		}
		
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
			System.exit(-1);
		}
	}
	
	// use group by function? - UNFINISHED (does this belong in this class or the borrowing class?)
	public void checkOverdues(int borid) {
		Statement stmt;
		ResultSet rs;

		try {
			stmt = con.createStatement();
			// rs =
			// stmt.executeQuery("SELECT DISTINCT b.borrowing_borid, br.borr_bid ")
		} catch (SQLException e) {

		}
	}
	
	// Display all the rows of the table Borrowing
	public void displayBorrowing() {
		//TODO
	}



}
