import java.io.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;



public class Librarian {

	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	java.sql.Connection con = Connection.getInstance().getConnection();

	public Librarian() {
		// TODO Auto-generated constructor stub
	}

	public void addBook () {


		try {

			System.out.print("\nISBN: ");
			int isbn = Integer.parseInt(in.readLine());

			Book book = new Book();
			int existingCallNo = book.checkBook(isbn);
			if (book.checkBook(isbn) == 0) {
				System.out.print("\nNo matching book found in system. Add a new book: ");
				System.out.print("\nCall Number: ");
				int callNo = Integer.parseInt(in.readLine());
				if (book.numCopies(callNo) == 0) {
					System.out.print("\nTitle: ");
					String title = in.readLine();
					System.out.print("\nAuthor: ");
					String mainAuthor = in.readLine();
					System.out.print("\nPublisher: ");
					String publisher = in.readLine();
					System.out.print("\nYear Published: ");
					int year = Integer.parseInt(in.readLine());	
					book.insertBook(callNo, isbn, title, mainAuthor, publisher, year);			
				} else {
					System.out.print("\nCall Number exists, perhaps there was an error in entering the ISBN or Call Number.");	
					return;
				}
				
				addCopy(callNo);
			} else {
				System.out.print("\nBook exists, adding new copy:");
				addCopy(existingCallNo);
			}
		}

		catch (IOException e)
		{
			System.out.println("IOException!");
		}
	}

	public void addCopy (int callNo) {

		try {
			System.out.print("\n\nPlease choose one of the following statuses: \n");
			System.out.print("1.  In\n");
			System.out.print("2.  Checked Out\n");
			
			int choice = Integer.parseInt(in.readLine());
			String status = null;
			switch(choice)
			{
			   case 1: status = "in"; break;
			   case 2: status = "out"; break;
			}
			
			bookCopy copies = new bookCopy();


			copies.insertBookCopy(callNo, status);
		}

		catch (IOException e)
		{
			System.out.println("IOException!");
		}
	}



	public void generateBookReport() {

		int bid;
		String callNo;
		String copyNo;
		Date outDate;
		Date inDate;
		Statement stmt;
		ResultSet rs;
		
		borrowerTable borrowerTable = new borrowerTable();
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT borr_bid, book_callNo, bookCopy_copyNo, borrowing_outDate, borrowing_inDate FROM borrowing WHERE borrowing_inDate IS NULL");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();
			
			for (int i = 0; i < numCols; i++)
			{
				// get column name and print it
				System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			}
			
			System.out.printf("%-15s", "Overdue?");  

			while(rs.next()) {
				callNo = rs.getString("book_callNo");
				System.out.printf("\n%-10.10s", callNo);

				copyNo = rs.getString("bookCopy_copyNo");
				System.out.printf("%-20.20s", copyNo);

				bid = rs.getInt("borr_bid");
				System.out.printf("%-20.20s", bid);
				
				outDate = rs.getDate("borrowing_outDate");
				System.out.printf("%-20.20s", outDate);	
				
				inDate = rs.getDate("borrowing_inDate");
				System.out.printf("%-20.20s", inDate);		
				
				String type = borrowerTable.checkBorrowerType(bid);
				int timeLimit = borrowerTable.getTimeLimit(type);
				
				GregorianCalendar gregCalendar = new GregorianCalendar();
				
				if () {
					
				}
			}
			stmt.close();
		}

		catch(SQLException e){
			System.out.print("Message: " + e.getMessage());
		}
		
	}

	public void generatePopularBooksReport () {

	}
	
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Librarian librarian = new Librarian();
				//     testTable.insertBook(3, 2, "a", "b", "c", 0);

				librarian.generateBookReport();               

			}
		});
	}

}
