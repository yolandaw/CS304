import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Borrower{

	/**
	 * 
	 */
	public Borrower() {
		// TODO Auto-generated constructor stub
	}

	java.sql.Connection con = Connection.getInstance().getConnection();

	/*
	 * Search for books using keyword search on titles, authors and subjects. 
	 * The result is a list of books that match the search together with the number 
	 * of copies that are in and out
	 */
	public List<List<String>> search(String keyword) {
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT book_title, book.book_callNo, bookCopy_copyNo, bookCopy_status FROM book, bookCopy, hasAuthor, hasSubject WHERE book.book_callNo=bookCopy.book_callNo AND (book_title LIKE '%" + keyword + "%' OR (hasAuthor_name LIKE '%" + keyword + "%' AND book.book_callNo=hasAuthor.book_callNo) OR (hasSubject_subject LIKE '%" + keyword + "%' AND book.book_callNo=hasSubject.book_callNo) )");
		
			List<List<String>> colArray = new ArrayList<List<String>>(1);

			
			while(rs.next()) {
				List<String> rowArray = new ArrayList<String>(1);
				for (int i=0; i < 4; i++) {
					rowArray.add(i, rs.getString(i+1));
				}

				colArray.add(rowArray);
			}
			stmt.close();
			
//			int numRows = colArray.size();
//			for (int i=0; i < numRows; i++) {
//				for (int k=0; k < 4; k++) {
//					System.out.println(colArray.get(i).get(k));
//				}
//			}
						
			return colArray;
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		    ex.printStackTrace();
		}	
		return null;
	}

	/*
	 * Check his/her account. The system will display the items the borrower has 
	 * currently borrowed and not yet returned, any outstanding fines and the hold 
	 * requests that have been placed by the borrower
	 */
	public List<List<String>> checkAccount(int id) {
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT bc.book_callNo,bc.bookCopy_copyNo,f.fine_amount,h.holdRequest_hid FROM fine f,borrowing b,holdRequest h,bookCopy bc,borrower bo WHERE (bo.borr_bid=b.borr_bid AND b.book_callNo=bc.book_callNo AND b.bookCopy_copyNo=bc.bookCopy_copyNo AND b.borrowing_borid=f.borrowing_borid AND bo.borr_bid=h.borr_bid) AND bo.borr_bid=" + id + " ");
				
			List<List<String>> colArray = new ArrayList<List<String>>(1);

			
			while(rs.next()) {
				List<String> rowArray = new ArrayList<String>(1);
				for (int i=0; i < 4; i++) {
					rowArray.add(i, rs.getString(i+1));
				}

				colArray.add(rowArray);
			}
			stmt.close();
			
			return colArray;
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}
		
		return null;
	}
	
	/*
	 * Place a hold request for a book that is out. When the item is returned, 
	 * the system sends an email to the borrower and informs the library clerk 
	 * to keep the book out of the shelves.
	 */
	public void placeHold(int bid, int callNo) {
		holdRequest hr = new holdRequest();
		hr.insertHoldRequest(bid, callNo);
	}
	
	/*
	 * Pay a fine
	 */
	public boolean payFine(int bid) {
		Fine f = new Fine();
		if ( f.checkHasFines(bid) ) {
			f.payAllFines(bid);
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//hr.displayHoldRequest();
				Borrower borrower = new Borrower();
				//borrower.payFine(10);
//				Fine f = new Fine();
//				f.displayBIDFines(10);
				borrower.search("a");
			
			}
		});
	}

}
