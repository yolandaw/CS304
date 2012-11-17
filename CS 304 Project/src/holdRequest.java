import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class holdRequest {

	java.sql.Connection con = Connection.getInstance().getConnection();
	
	public holdRequest() {
		
	}
	
	// Insert a tuple into the table HoldRequest
	public void insertHoldRequest(int bid, int callNo, String stringdate) {

		java.sql.Date date = java.sql.Date.valueOf(stringdate);
		
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("INSERT INTO holdRequest VALUES (hid_counter.nextval,?,?,?)");
			//ps.setInt(1, "hid_counter.nextval");
			ps.setInt(2, bid);
			ps.setInt(3, callNo);
			ps.setDate(4, date);
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
	
	// Delete a tuple from the table HoldRequest
	public void deleteHoldRequest(int hid) {
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("DELETE FROM holdRequest WHERE hid = ?");
			ps.setInt(1, hid);
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
	
	// Display all the rows of the table HoldRequest
	public void displayHoldRequest() {
		int hid;
		int bid;
		int callNo;
		Date date;
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM book");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();

			System.out.println(" ");
			
			for (int i = 0; i < numCols; i++)
			{
		      // get column name and print it
		      System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			}
			
			while(rs.next()) {
				hid = rs.getInt("holdRequest_hid");
			    System.out.printf("%-10.10s", hid);
			    
			    bid = rs.getInt("borr_bid");
			    System.out.printf("%-20.20s", bid);
			    
			    callNo = rs.getInt("book_callNo");
			    System.out.printf("%-20.20s", callNo);
			    
			    date = rs.getDate("holdRequest_issueDate");
			    System.out.printf("%-20.20s", date);
			 
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
                holdRequest testTable = new holdRequest();
                //testTable.insertHoldRequest(0, 0, 0, "2012-11-16");
                testTable.displayHoldRequest();
   //             testTable.deleteBorrower(12345);
            }
        });
	}
	
}
