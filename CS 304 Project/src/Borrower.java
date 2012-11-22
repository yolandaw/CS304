import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Borrower{

	/**
	 * 
	 */
	public Borrower() {
		// TODO Auto-generated constructor stub
	}
	
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	java.sql.Connection con = Connection.getInstance().getConnection();

	/*
	 * Search for books using keyword search on titles, authors and subjects. 
	 * The result is a list of books that match the search together with the number 
	 * of copies that are in and out
	 */
	public void search() {
		Statement stmt;
		ResultSet rs;
		try {
			System.out.println("\nKeywords: ");
			String keyword = in.readLine();
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT b.book_title,b.book_callNo,bc.bookCopy_copyNo,bc.bookCopy_status FROM book b,bookCopy bc,hasAuthor h,hasSubject hs WHERE b.book_callNo=h.book_callNo AND b.book_callNo=hs.book_callNo AND b.book_callNo=bc.book_callNo AND (b.book_title='" + keyword + "' OR h.hasAuthor_name='" + keyword + "' OR hs.hasSubject_subject='" + keyword + "' )");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();

			System.out.println(" ");
			
			for(int i=0; i < numCols; i++) {
				System.out.printf("%-15s", rsmd.getColumnName(i+1));
			}
			System.out.println(" ");
			
			while(rs.next()) {
				String book_title = rs.getString("book_title");
				System.out.printf("%-10.10s", book_title);
				int book_callNo = rs.getInt("book_callNo");
				System.out.printf("%-10.10s", book_callNo);
				int bookCopy_copyNo = rs.getInt("bookCopy_copyNo");
				System.out.printf("%-20.20s", bookCopy_copyNo);
				String bookCopy_status = rs.getString("bookCopy_status");
				System.out.printf("%-50.50s", bookCopy_status);
				System.out.println(" ");
			}
			stmt.close();
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		    ex.printStackTrace();
		}	
		catch (IOException e)
		{
			System.out.println("IOException!");
		}
	}

	/*
	 * Check his/her account. The system will display the items the borrower has 
	 * currently borrowed and not yet returned, any outstanding fines and the hold 
	 * requests that have been placed by the borrower
	 */
	public void checkAccount(int id) {
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT bc.book_callNo,bc.bookCopy_copyNo,f.fine_amount,h.holdRequest_hid FROM fine f,borrowing b,holdRequest h,bookCopy bc,borrower bo WHERE bo.borr_bid=b.borr_bid AND b.book_callNo=bc.book_callNo AND b.bookCopy_copyNo=bc.bookCopy_copyNo AND b.borrowing_borid=f.borrowing_borid AND bo.borr_bid=h.borr_bid AND bo.borr_bid=" + id);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();
			System.out.println(" ");
			
			for(int i=0; i < numCols; i++) {
				System.out.printf("%-15s", rsmd.getColumnName(i+1));
			}
			System.out.println(" ");
			
			while(rs.next()) {
				int book_callNo = rs.getInt("book_callNo");
				System.out.printf("%-10.10s", book_callNo);
				int bookCopy_copyNo = rs.getInt("bookCopy_copyNo");
				System.out.printf("%-20.20s", bookCopy_copyNo);
				int fine_amount = rs.getInt("fine_amount");
				System.out.printf("%-30.30s", fine_amount);
				int holdRequest_hid = rs.getInt("holdRequest_hid");
				System.out.printf("%-20.20s", holdRequest_hid);
			}
			stmt.close();
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}
	}
	
	/*
	 * Place a hold request for a book that is out. When the item is returned, 
	 * the system sends an email to the borrower and informs the library clerk 
	 * to keep the book out of the shelves.
	 */
	public void placeHold(int bid, int callNo, String date) {
		holdRequest hr = new holdRequest();
		hr.insertHoldRequest(bid, callNo, date);
	}
	
	/*
	 * Pay a fine
	 */
	public void payFine(int amount, int bid) {
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("UPDATE fine SET fine.fine_amount=(fine.fine_amount-?) WHERE borrower.borr_bid=? AND borrower.borr_bid=borrowing.borr_bid AND borrowing.borrowing_borid=fine.borrowing_borid");
			con.commit();
			ps.close();
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
//				borrowerTable bt = new borrowerTable();
//				bt.displayBorrower();
				//Borrowing bo = new Borrowing();
				//bo.insertBorrowing(100, 10, 234234, 1, 2012/02/12, "2012/03/12");
//				Borrower borrower = new Borrower();
//				borrower.checkAccount(10);
			}
		});
	}

}
