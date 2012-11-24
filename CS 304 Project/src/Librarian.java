import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class Librarian {
	java.sql.Connection con = Connection.getInstance().getConnection();

	public Librarian() {
		// TODO Auto-generated constructor stub
	}

	public boolean checkISBN(int isbn) {

		Book book = new Book();
		int callNo = book.checkBook(isbn);

		if (callNo == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void addBook (int callNo, int isbn, String title, String mainAuthor, String publisher, int year) {

		Book book = new Book();
		//			int existingCallNo = book.checkBook(isbn);
		//			if (book.checkBook(isbn) == 0) {
		//				if (book.numCopies(callNo) == 0) {	
		book.insertBook(callNo, isbn, title, mainAuthor, publisher, year);			
		//				} else {
		//					return;
		//				}
		//				
		//				addCopy(callNo, choice);
		//			} else {
		//				addCopy(existingCallNo, choice);
		//			}

	}

	public void addCopy (int callNo, int choice) {

		try {

			String status = null;
			switch(choice)
			{
			case 1: status = "in"; break;
			case 2: status = "out"; break;
			case 3: status = "on-hold"; break;
			}

			bookCopy copies = new bookCopy();


			copies.insertBookCopy(callNo, status);
		}
		catch(IllegalArgumentException e){
		}
	}



	public List<List<String>> generateBookReport() {

		int borid;
		String overdue;
		Statement stmt;
		ResultSet rs;
		Borrowing borrowing = new Borrowing();

		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT borrowing_borid, borr_bid, book_callNo, bookCopy_copyNo, borrowing_outDate, borrowing_inDate FROM borrowing WHERE borrowing_inDate IS NULL");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();

			List<List<String>> colArray = new ArrayList<List<String>>(1);

			while(rs.next()) {

				borid = rs.getInt("borrowing_borid");
				if (borrowing.isOverdue(borid)) {
					overdue = "Overdue";	
				} else {
					overdue = "";
				}
				
				List<String> rowArray = new ArrayList<String>(1);
				for (int i=0; i < numCols; i++) {
					rowArray.add(i, rs.getString(i+1));
				}
				rowArray.add(overdue);
				colArray.add(rowArray);
				return colArray;
			}
						
			stmt.close();
		}

		catch(SQLException e){
			System.out.print("Message: " + e.getMessage());
		}
		return null;

	}

	public List<List<String>> generatePopularBooksReport(int year, int n) {

		Statement stmt;
		ResultSet rs;

		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT book_callNo, COUNT(*) FROM borrowing WHERE EXTRACT(YEAR from borrowing_outDate) ='" + year + "' GROUP BY book_callNo ORDER BY COUNT(borrowing_borid) DESC");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();

			List<List<String>> colArray = new ArrayList<List<String>>(1);
			int j = 1;
			
			while(rs.next() && j <= n) {
				
				List<String> rowArray = new ArrayList<String>(1);
				for (int i=0; i < numCols; i++) {
					rowArray.add(i, rs.getString(i+1));
				}
				colArray.add(rowArray);
				
			}
			
//			int numRows = colArray.size();
//			for (int i=0; i < numRows; i++) {
//				for (int k=0; k < numCols; k++) {
//					System.out.println(colArray.get(i).get(k));
//				}
//			}
			stmt.close();

			return colArray;
		}

		catch(SQLException e){
			System.out.print("Message: " + e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Librarian librarian = new Librarian();
				//     testTable.insertBook(3, 2, "a", "b", "c", 0);

				//			librarian.generateBookReport();    

				//		librarian.generatePopularBooksReport(2012, 1);

			//	librarian.addBook(48, 0, "title", "mainAuthor", "publisher", 1939);
			
			librarian.generatePopularBooksReport(2012, 10);
			}
		});
	}

}
