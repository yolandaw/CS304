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
			case 0: status = "in"; break;
			case 1: status = "out"; break;
			case 2: status = "on-hold"; break;
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
			rs = stmt.executeQuery("SELECT borrowing_borid, book_callNo, bookCopy_copyNo, borrowing_outDate, borrowing_inDate FROM borrowing WHERE borrowing_inDate IS NULL");
			
			List<List<String>> colArray = new ArrayList<List<String>>(1);

			List<String> columnHeadingsArray = new ArrayList<String>(1);
			columnHeadingsArray.add("Call Number");
			columnHeadingsArray.add("Copy Number");
			columnHeadingsArray.add("Out Date");
			columnHeadingsArray.add("Overdue");
			colArray.add(columnHeadingsArray);
			
			while(rs.next()) {

				borid = rs.getInt("borrowing_borid");
				if (borrowing.isOverdue(borid)) {
					overdue = "Overdue";	
				} else {
					overdue = "";
				}
				
				List<String> rowArray = new ArrayList<String>(1);

				rowArray.add(rs.getString("book_callNo"));
				rowArray.add(rs.getString("bookCopy_copyNo"));
				rowArray.add(rs.getString("borrowing_outDate"));
				rowArray.add(overdue);
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
			rs = stmt.executeQuery("SELECT * FROM ((SELECT book_callNo, COUNT(borrowing_borid) FROM borrowing WHERE EXTRACT(YEAR from borrowing_outDate) ='" + year + "' GROUP BY borrowing.book_callNo ORDER BY COUNT(borrowing_borid) DESC) borr JOIN (SELECT book_callNo, book_title from book) booktitle ON borr.book_callNo = booktitle.book_callNo)");

			List<List<String>> colArray = new ArrayList<List<String>>(1);


			List<String> columnHeadingsArray = new ArrayList<String>(1);
			columnHeadingsArray.add("Call Number");
			columnHeadingsArray.add("Book Title");
			columnHeadingsArray.add("Number of Times Borrowed");
			colArray.add(columnHeadingsArray);
			
			int j = 1;
			
			while(rs.next() && j <= n) {
				
				List<String> rowArray = new ArrayList<String>(1);

				rowArray.add(rs.getString("book_callNo"));
				rowArray.add(rs.getString("book_title"));
				rowArray.add(rs.getString("COUNT(borrowing_borid)"));
				colArray.add(rowArray);
				
			}
			
//			int numRows = colArray.size();
//			for (int i=0; i < numRows; i++) {
//				for (int k=0; k < 3; k++) {
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
			
//			librarian.generateBookReport();
//			librarian.generatePopularBooksReport(2012, 10);
			}
		});
	}

}
