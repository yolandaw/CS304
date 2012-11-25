import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

public class Borrowing {
	
	public Borrowing() {
		
	}
	java.sql.Connection con = Connection.getInstance().getConnection();
	

	CastDate newDate = new CastDate();
	
	// Insert a tuple into the table Borrowing
	public void insertBorrowing(int borid, int bid, int callNo, int copyNo) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO borrowing VALUES(?,?,?,?,?,NULL)");
			ps.setInt(1, borid);
			ps.setInt(2, bid);
			ps.setInt(3, callNo);
			ps.setInt(4, copyNo);
			ps.setDate(5, newDate.currentDate());
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
	
	//find borrowing id of given book that has not been returned
	public int findBoridOfBook(int callNo, int copyNo){
		Statement stmt;
		ResultSet rs;
		int borid = 0;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT borrowing_borid FROM borrowing WHERE book_callno =" + callNo + "AND bookcopy_copyno =" + copyNo + "AND borrowing_indate IS NULL");
			while(rs.next()){
				borid = rs.getInt(1);
			}
			stmt.close();	
		}

		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		}
		
		return borid;
	}
	
	//find borrower of the retunred book
	public int findBorrowerOfBook(int callNo, int copyNo){
		Statement stmt;
		ResultSet rs;
		int bid = 0;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT borr_bid FROM borrowing WHERE book_callno =" + callNo + "AND bookcopy_copyno =" + copyNo + "AND borrowing_indate IS NULL");
			while(rs.next()){
				bid = rs.getInt(1);
			}
			stmt.close();	
		}

		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		}
		
		return bid;
	}
	
	//sets the return date of book if the return date is NULL initially
	public void setInDate(int callNo, int copyNo){
		
		PreparedStatement ps; 
		
		try{
		ps = con.prepareStatement("UPDATE borrowing SET borrowing_indate = ? WHERE book_callno =? AND bookcopy_copyno =? AND borrowing_indate IS NULL");

		ps.setDate(1, newDate.currentDate());
		ps.setInt(2, callNo);
		ps.setInt(3,copyNo);
		ps.executeUpdate();
		con.commit();
		ps.close();
		
		}
		
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
			System.exit(-1);
		}
	}
	

	public String[][] overdueBooksAndBor(){
		Statement stmt;
		ResultSet rs = null;
		//Vector<Vector<String>> overdueList = new Vector<Vector<String>>(10);
		String[][] overdueList;
		int bid;
		int borid;
		int callNo;
		int copyNo;
		Date outDate;
		Date inDate;
		int count = 0;
		int i = 0;
		
		try{
			stmt = con.createStatement(rs.TYPE_SCROLL_INSENSITIVE,rs.CONCUR_READ_ONLY);
			rs = stmt.executeQuery("SELECT * FROM borrowing WHERE borrowing_inDate IS NULL");
			
			try {
			  rs.last();
			   count = rs.getRow();
			    rs.beforeFirst();
			}
			catch(Exception ex) {
				return null;
			}
			
			System.out.println("Count" + count);
			
			
			while(rs.next()){
				bid = rs.getInt("borr_bid");
				String sBid = Integer.toString(bid);
				borid = rs.getInt("borrowing_borid");
				callNo = rs.getInt("book_callNo");
				String sCallNo = Integer.toString(callNo);
				copyNo = rs.getInt("bookcopy_copyNo");
				String sCopyNo = Integer.toString(copyNo);
				outDate = rs.getDate("borrowing_outDate");
				inDate = rs.getDate("borrowing_inDate");
				overdueList = new String[count][3];
				

				
				//for(int i = 0; i < count; i++) {	
					if(isOverdue(borid)){
						System.out.println("Borid" + borid);
						System.out.println(sBid);
						overdueList[i][0] = sBid;
						System.out.println(overdueList[i][0]);
						System.out.println(sCallNo);
						overdueList[i][1] = sCallNo;
						System.out.println(overdueList[i][1]);
						System.out.println(sCopyNo);
						overdueList[i][2] = sCopyNo;
						System.out.println(overdueList[i][2]);	
						i++;
						
				//}				
				
			}
		
//					for(int k = 0; k < 3; k++){
//						for(int j = 0; j < 3 ; j++){
//							System.out.println(overdueList[k][j]);
//
//						}
//						System.out.println(" ");
//					}
			
			}
		}
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		}
		
		return null;
		
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
                //testTable.setInDate(1, 2);
                testTable.displayBorrowing();
                //testTable.insertBorrowing(1005, 15, 1, 10);
//                testTable.displayBorrowing();
//                System.out.println(" ");
//                System.out.println(testTable.findBorrowerOfBook(1, 10));
//                System.out.println(testTable.findBoridOfBook(1, 10));
                testTable.overdueBooksAndBor();
            }
        });
	}

}
