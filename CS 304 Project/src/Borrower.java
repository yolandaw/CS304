import java.sql.*;

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
	public void search(String keyword) {
		Statement stmt;
		ResultSet rs;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT book_title,bookCopy_copyNo,bookCopy_status FROM book,bookCopy WHERE book.book_callNo=hasAuthor.book_callNo AND book.book_callNo=hasSubject.book_callNo AND book.book_callNo=bookCopy.book_callNo AND (book.book_title=? OR hasAuthor.hasAuthor_name=? OR hasSubject.hasSubject_subject=?)");
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
				int bookCopy_copyNo = rs.getInt("bookCopy_copyNo");
				System.out.printf("%-20.20s", bookCopy_copyNo);
				int bookCopy_status = rs.getInt("bookCopy_status");
				System.out.printf("%-20.20s", bookCopy_status);
			}
			stmt.close();
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}	
	}

	/*
	 * Check his/her account. The system will display the items the borrower has 
	 * currently borrowed and not yet returned, any outstanding fines and the hold 
	 * requests that have been placed by the borrower
	 */
	public void checkAccount() {
		
	}
	
	/*
	 * Place a hold request for a book that is out. When the item is returned, 
	 * the system sends an email to the borrower and informs the library clerk 
	 * to keep the book out of the shelves.
	 */
	public void placeHold() {
		
	}
	
	/*
	 * Pay a fine
	 */
	public void payFine(int amount) {
		
	}

}
