import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Borrowing {
	
	public Borrowing() {
		
	}
	java.sql.Connection con = Connection.getInstance().getConnection();
	

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
	
	public boolean isOverdue(int borid) {
		
		int bid;
		Date outDate;
		Date inDate;
		Statement stmt;
		ResultSet rs;
		CastDate castDate;
		Calendar calDueDate = new GregorianCalendar();
		Calendar calCurrDate = new GregorianCalendar();
		
		borrowerTable borrowerTable = new borrowerTable();
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT borr_bid, borrowing_outDate, borrowing_inDate FROM borrowing WHERE borrowing_borid = " + borid);

			while(rs.next()) {
				bid = rs.getInt("borr_bid");
				outDate = rs.getDate("borrowing_outDate");
				inDate = rs.getDate("borrowing_inDate");	

				castDate = new CastDate();
				
				String type = borrowerTable.checkBorrowerType(bid);
				int timeLimit = borrowerTable.getTimeLimit(type);
				Date dueDate = castDate.addToDate(outDate, timeLimit);
				calDueDate = castDate.getGDate(dueDate);
				calCurrDate = castDate.getGDate(castDate.currentDate());
				
				if (inDate == null) {
					if (calCurrDate.compareTo(calDueDate) > 0) {

						return true;	
					} else {

						return false;
					}
				} 
				return false;
			}
			stmt.close();
		}

		catch(SQLException e){
			System.out.print("Message: " + e.getMessage());
		}
		
		return false;
	}
	
	// Display all the rows of the table Borrowing
	public void displayBorrowing() {
		int borid;
		int bid;
		int callNo;
		int copyNo;
		Date outDate;
		Date inDate;
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM borrowing");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();

			System.out.println(" ");
			
			for (int i = 0; i < numCols; i++)
			{
		      // get column name and print it
		      System.out.printf("%-20s", rsmd.getColumnName(i+1));    
			}
			
			while(rs.next()) {
				borid = rs.getInt("borrowing_borid");
			    System.out.printf("\n%-20.20s", borid);
			    
			    bid = rs.getInt("borr_bid");
			    System.out.printf("%-20.20s", bid);
			    
			    callNo = rs.getInt("book_callNo");
			    System.out.printf("%-20.20s", callNo);
			    
			    copyNo = rs.getInt("bookCopy_copyNo");
			    System.out.printf("%-20.20s", copyNo);
			    
			    outDate = rs.getDate("borrowing_outDate");
			    System.out.printf("%-20.20s", outDate);
			    
			    inDate = rs.getDate("borrowing_inDate");
			    System.out.printf("%-20.20s", inDate);
			 
			}
			stmt.close();
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}
	}


	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Borrowing testTable = new Borrowing();
                testTable.displayBorrowing();
            }
        });
	}

}
