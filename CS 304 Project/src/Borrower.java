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
	public Object[][] search(String keyword) {
		Statement stmt;
		ResultSet rs;

		int count;

		String title;
		int callNo;
		int copyNo;
		String status;

		Object[][] searchResult = null;

		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery("SELECT book_title, book.book_callNo, bookCopy_copyNo, bookCopy_status FROM book, bookCopy, hasAuthor, hasSubject WHERE book.book_callNo=bookCopy.book_callNo AND (book_title LIKE '%" + keyword + "%' OR (hasAuthor_name LIKE '%" + keyword + "%' AND book.book_callNo=hasAuthor.book_callNo) OR (hasSubject_subject LIKE '%" + keyword + "%' AND book.book_callNo=hasSubject.book_callNo) OR book.book_mainAuthor LIKE '%" + keyword + "%' )");

			try {

				rs.last();

				count = rs.getRow();
				rs.beforeFirst();
			}
			catch(Exception ex) {
				return null;
			}

			if (count > 0) {
				searchResult = new Object[count][4];
			}

			int i=0;
			while(rs.next()) {

				title = rs.getString("book_title");
				callNo = rs.getInt("book_callNo");
				copyNo = rs.getInt("bookCopy_copyNo");
				status = rs.getString("bookCopy_status");

				searchResult[i][0] = title;
				searchResult[i][1] = callNo;
				searchResult[i][2] = copyNo;
				searchResult[i][3] = status;
				i++;

			}
			stmt.close();

			//			for (int l=0;l<count;l++) {
			//				for (int k=0;k<4;k++) {
			//					System.out.println(searchResult[l][k]);
			//				}
			//			}

			return searchResult;
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
			ex.printStackTrace();
		}	
		return null;
	}

	public Object[][] checkLoans(int id) {
		Statement stmt;
		Statement stmt2;
		ResultSet rs;
		ResultSet rs2;
		int count;
		int borid;
		int callNo;
		int copyNo;
		String title;
		Date outDate;
		Borrowing borr = new Borrowing();

		Borrowing borrowing = new Borrowing();
		String overdue;

		Object[][] currentLoans = null;
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery("SELECT borrowing_borid, book_callNo, bookCopy_copyNo, borrowing_outDate, borrowing_inDate FROM borrowing WHERE borrowing_inDate IS NULL AND borr_bid = '" + id + "'");

			try {

				rs.last();
				count = rs.getRow();
				rs.beforeFirst();
			}
			catch(Exception ex) {
				return null;
			}
			if (count > 0) {
				currentLoans = new Object[count][6];
			}

			int i=0;
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
				borid = rs.getInt("borrowing_borid");

				stmt2 = con.createStatement();
				rs2 = stmt2.executeQuery("SELECT book_title FROM book WHERE book_callno =" + callNo);
				rs2.next();
				title = rs2.getString("book_title");

				currentLoans[i][0] = callNo;
				currentLoans[i][1] = copyNo;
				currentLoans[i][2] = title;
				currentLoans[i][3] = outDate;
				currentLoans[i][4] = overdue;
				currentLoans[i][5] = borr.getDueDate(id, borid);
				i++;
			}
			stmt.close();

//			for (i=0;i<count;i++) {
//				for (int j=0;j<5;j++) {
//					System.out.println(currentLoans[i][j]);
//				}
//			}

			return currentLoans;
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
		}

		return null;

	}

	public Object[][] checkFines(int id) {
		Statement stmt;
		Statement stmt2;
		ResultSet rs;
		ResultSet rs2;
		int count;
		int borid;
		int callNo;
		int fineAmount;
		String title;
		Date issueDate;
		Borrowing borr = new Borrowing();

		Object[][] currentFines = null;
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery("SELECT fine.borrowing_borid, fine_amount, fine_issueDate, book_callNo FROM borrowing, fine WHERE fine_paidDate IS NULL AND borrowing.borrowing_borid = fine.borrowing_borid AND borr_bid = '" + id + "'");

			try {
				rs.last();
				count = rs.getRow();
				rs.beforeFirst();
			}

			catch(Exception ex) {
				return null;
			}
//			if (count > 0) {
				currentFines = new Object[count][5];
//			}

			int i=0;
			while(rs.next()) {

				callNo = rs.getInt("book_callNo");
				issueDate = rs.getDate("fine_issueDate");
				fineAmount = rs.getInt("fine_amount");
				borid = rs.getInt("borrowing_borid");

				stmt2 = con.createStatement();
				rs2 = stmt2.executeQuery("SELECT book_title FROM book WHERE book_callno =" + callNo);
				rs2.next();
				title = rs2.getString("book_title");

				currentFines[i][0] = issueDate;
				currentFines[i][1] = fineAmount;
				currentFines[i][2] = callNo;
				currentFines[i][3] = title;
				currentFines[i][4] = borr.getDueDate(id, borid); //dueDate;
				i++;
			}
			stmt.close();

			for (i=0;i<count;i++) {
				for (int j=0;j<5;j++) {
					System.out.println(currentFines[i][j]);
				}
			}

			return currentFines;
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
		}

		return null;

	}

	public Object[][] checkHolds(int id) {
		Statement stmt;
		Statement stmt2;
		ResultSet rs;
		ResultSet rs2;
		int count;
		int callNo;
		String title;
		Date issueDate;

		Borrowing borrowing = new Borrowing();

		Object[][] currentHolds = null;
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery("SELECT holdRequest_issueDate, book_callNo FROM holdRequest WHERE borr_bid = '" + id + "'");

			try {
				rs.last();
				count = rs.getRow();
				rs.beforeFirst();
			}
			catch(Exception ex) {
				return null;
			}
			if (count > 0) {
				currentHolds = new Object[count][3];
			}

			int i=0;
			while(rs.next()) {

				callNo = rs.getInt("book_callNo");
				issueDate = rs.getDate("holdRequest_issueDate");

				stmt2 = con.createStatement();
				rs2 = stmt2.executeQuery("SELECT book_title FROM book WHERE book_callno =" + callNo);
				rs2.next();
				title = rs2.getString("book_title");

				currentHolds[i][0] = callNo;
				currentHolds[i][1] = title;
				currentHolds[i][2] = issueDate;
				i++;
			}
			stmt.close();

			for (i=0;i<count;i++) {
				for (int j=0;j<3;j++) {
					System.out.println(currentHolds[i][j]);
				}
			}

			return currentHolds;
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
		Statement stmt;
		ResultSet rs;
		String status;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT bookCopy_status FROM bookCopy WHERE book_callNo = '" + callNo + "'");
			
			while (rs.next()) {
				status = rs.getString("bookCopy_status");

				if (!status.equals("out")) {
					return;
				}
			}

			hr.insertHoldRequest(bid, callNo);
		
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
		}
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
	
	//get total fines
	public float getTotalFines(int bid){
		float totalFines = 0;
		Statement stmt;
		ResultSet rs;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT DISTINCT f.fine_amount FROM fine f, borrowing b, borrower br WHERE b.borr_bid = "
					+ bid + "AND b.borrowing_borid = f.borrowing_borid AND f.fine_paiddate IS NULL" );
			
			while(rs.next()){
				totalFines = totalFines + rs.getFloat(1);
			}
		}
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		}
		return totalFines;
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
				//				borrower.search("a");
//					borrower.checkLoans(15);
				//				borrower.checkFines(10);

				//borrower.placeHold(10,2);
				//borrower.checkHolds(10);
								
				System.out.println(borrower.getTotalFines(10));
				
			}
		});
	}

}
