import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class holdRequest {

	java.sql.Connection con = Connection.getInstance().getConnection();
	CastDate newDate = new CastDate();
	
	public holdRequest() {
		
	}
	
	// Insert a tuple into the table HoldRequest
	public void insertHoldRequest(int bid, int callNo) {
		
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("INSERT INTO holdRequest VALUES (hid_counter.nextval,?,?,?)");
			//ps.setInt(1, null);
			ps.setInt(1, bid);
			ps.setInt(2, callNo);
			ps.setDate(3, newDate.currentDate());
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
			rs = stmt.executeQuery("SELECT * FROM holdRequest");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();

			System.out.println(" ");
			
			for (int i = 0; i < numCols; i++)
			{
		      // get column name and print it
		      System.out.printf("%-20s", rsmd.getColumnName(i+1));    
			}
			
			while(rs.next()) {
				hid = rs.getInt("holdRequest_hid");
			    System.out.printf("\n%-20.20s", hid);
			    
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
	
	
	//get hid from callNo
	public int getHID(int callNo){
		Statement stmt;
		ResultSet rs;
		int hid = 0;

		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT holdRequest_hid FROM holdRequest WHERE book_callno =" + callNo);
			
			while(rs.next()){
				hid = rs.getInt(1);
			}
		}

		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		}
		return hid;
	}
	
	//get bid from callNo
		public int getBID(int callNo){
			Statement stmt;
			ResultSet rs;
			int bid = 0;

			try{
				stmt = con.createStatement();
				rs = stmt.executeQuery("SELECT borr_bid FROM holdRequest WHERE book_callno =" + callNo);
				
				while(rs.next()){
					bid = rs.getInt(1);
				}
			}

			catch(SQLException e){
				System.out.println("Message: " + e.getMessage());
			}
			return bid;
		}
	
	//check if there are any hold requests for book
	public boolean hasHold(int callNo){
		Statement stmt;
		ResultSet rs;
		int count = 0;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*) FROM holdRequest WHERE book_callno =" + callNo);
					
			while(rs.next()){
				count = rs.getInt(1);
			}
			if (count > 0){
				return true;
			}
			stmt.close();			
		}

		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		}
		return false;
	}
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                holdRequest testTable = new holdRequest();
                //testTable.displayHoldRequest();
                testTable.displayHoldRequest();
                System.out.print(testTable.getBID(10));
               
                
            }
        });
	}






}
