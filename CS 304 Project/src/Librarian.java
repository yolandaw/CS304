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



	public Object[][] generateBookReport() {

		int borid;
		int callNo;
		int copyNo;
		int count;
		Date outDate;
		String overdue;
		Statement stmt;

		Statement stmt2;
		ResultSet rs;
		ResultSet rs2;
		String title;
		Borrowing borrowing = new Borrowing();

		Object[][] bookList;

		try{
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery("SELECT borrowing_borid, book_callNo, bookCopy_copyNo, borrowing_outDate, borrowing_inDate FROM borrowing WHERE borrowing_inDate IS NULL");


			try {

				rs.last();

				count = rs.getRow();
				rs.beforeFirst();
			}
			catch(Exception ex) {
				return null;
			}

			bookList = new Object[count][5];
			int i = 0;
			while(rs.next()) {

				borid = rs.getInt("borrowing_borid");
				if (borrowing.isOverdue(borid)) {
					overdue = "Overdue";	
				} else {
					overdue = "";
				}

				callNo = rs.getInt("book_callNo");
				copyNo = rs.getInt("bookCopy_copyNo");
				outDate = rs.getDate("borrowing_outDate");

				stmt2 = con.createStatement();
				rs2 = stmt2.executeQuery("SELECT book_title FROM book WHERE book_callno =" + callNo);
				rs2.next();
				title = rs2.getString("book_title");

				bookList[i][0] = callNo;
				bookList[i][1] = copyNo;
				bookList[i][2] = title;
				bookList[i][3] = outDate;
				bookList[i][4] = overdue;
				i++;
			}

			//			for (i=0;i<count;i++) {
			//				for (int j=0;j<5;j++) {
			//					System.out.println(bookList[i][j]);
			//				}
			//			}

			stmt.close();

			return bookList;
		}

		catch(SQLException e){
			System.out.print("Message: " + e.getMessage());
		}
		return null;

	}

	public Object[][] generatePopularBooksReport(int year, int n) {

		Statement stmt;
		ResultSet rs;

		int count;
		int callNo;
		String title;
		int borrowCount;

		Object[][] bookList = null;


		try{
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery("SELECT * FROM ((SELECT book_callNo, COUNT(borrowing_borid) FROM borrowing WHERE EXTRACT(YEAR from borrowing_outDate) ='" + year + "' GROUP BY borrowing.book_callNo ORDER BY COUNT(borrowing_borid) DESC) borr JOIN (SELECT book_callNo, book_title from book) booktitle ON borr.book_callNo = booktitle.book_callNo)");

			try {

				rs.last();

				count = rs.getRow();
				rs.beforeFirst();

			}
			catch(Exception ex) {
				return null;
			}

			int i = 0;
			int j = 1;
			
			if (count > 0) {

				bookList = new Object[count][3];
			}

			while(rs.next() && j <= n) {

				callNo = rs.getInt("book_callNo");
				title = rs.getString("book_title");
				borrowCount = rs.getInt("COUNT(borrowing_borid)");

				bookList[i][0] = callNo;
				bookList[i][1] = title;
				bookList[i][2] = borrowCount;
				i++;

			}

			stmt.close();

//			for (int l=0;l<count;l++) {
//				for (int k=0;k<3;k++) {
//					System.out.println(bookList[l][k]);
//				}
//			}
			
			return bookList;
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

			//	librarian.generateBookReport();
//							librarian.generatePopularBooksReport(2012, 10);
			}
		});
	}

}
