import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

public class Borrowing {
	
	public Borrowing() {
		
	}
	java.sql.Connection con = Connection.getInstance().getConnection();
	private static int overdueListCount = 0;

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
	public int getBoridOfBook(int callNo, int copyNo){
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
	public int getBidOfBook(int callNo, int copyNo){
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
	

	//returns a list of overdue books
	public Object[][] getOverdueList(){
		Statement stmt;
		ResultSet rs = null;
		Statement stmt2;
		ResultSet rs2;
		Object[][] overdueList = null;
		int bid;
		int borid;
		int callNo;
		int copyNo;
		Date outDate;
		String title = null;
		int count = 0;
		
		try{
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery("SELECT * FROM borrowing WHERE borrowing_inDate IS NULL");
			
			try {
			  rs.last();
			   count = rs.getRow();
			    rs.beforeFirst();
			}
			catch(Exception ex) {
				return null;
			}
			
			while(rs.next()){
				bid = rs.getInt("borr_bid");
				borid = rs.getInt("borrowing_borid");
				callNo = rs.getInt("book_callNo");
				copyNo = rs.getInt("bookcopy_copyNo");
				outDate = rs.getDate("borrowing_outDate");
				overdueList = new Object[count][5];
				
				stmt2 = con.createStatement();
				rs2 = stmt2.executeQuery("SELECT book_title FROM book WHERE book_callno =" + callNo);
				rs2.next();
				title = rs2.getString("book_title");

					if(isOverdue(borid)){
						overdueList[overdueListCount][0] = bid;
						overdueList[overdueListCount][1] = outDate;
						overdueList[overdueListCount][2] = callNo;	
						overdueList[overdueListCount][3] = copyNo;
						overdueList[overdueListCount][4] = title;
						overdueListCount++;				
					}				
					return overdueList;		
			}
		}
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		}
		
		return null;
	
	}
	// have to add returning book titles
	//return the number for rows of the 2d array for getOverdueList method
	public int getOverdueListCount(){
		return overdueListCount;
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
                testTable.getOverdueList();
            }
        });
	}

}
