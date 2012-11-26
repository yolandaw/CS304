import java.sql.*;
import java.util.List;
import java.util.Vector;



public class Clerk {

	private String student = "student";
	private String faculty = "faculty";
	private String staff = "staff";
	private String general = "general";
	java.sql.Connection con = Connection.getInstance().getConnection();
	private static int overdueListCount;

	// Fine currFine = new Fine();

	public Clerk() {
		// TODO Auto-generated constructor stub
	}

	private String returnType(int intType) {

		String type = null;

		switch (intType) {
		case 1:
			type = student;
			break;
		case 2:
			type = faculty;
			break;
		case 3:
			type = staff;
			break;
		case 4:
			type = general;
			break;
		}
		return type;

	}	
	
	// adds a new borrower to the database. If a borrower with the given sinOrStNo already exists, the method returns false. Otherwise, the borrower is added successfully and returns true.
	public boolean addBorrower(String name, String password, String address,
			int phone, String email, int sinOrStNo, int type) {

		borrowerTable newBorrower = new borrowerTable();

		// check if SIN or St. No already exists
		if (newBorrower.checkSinOrStNo(sinOrStNo)) {
			return false;
		} else {

			newBorrower.insertBorrower(name, password, address, phone, email,
					sinOrStNo, returnType(type));
			return true;
		}
	}

	// checks out a list of books - unfinished (still have to get return expiry dates)
	// returns false if user has fines; otherwise, checkout is completed and returns true
	public Object[][] checkOut(int bid, int[] callNo, int[] copyNo) {

		Fine currFine = new Fine();
		bookCopy bookCopy = new bookCopy();
		String copyStatus = null;
		Borrowing currBorr = new Borrowing();

		if (currFine.checkHasFines(bid) == true) {
			return null;
			// prompt user with a pop up if they want to pay fines now or later
			// and if true then return the payFine window
		} else {

			for (int i = 0; i < callNo.length; i++) {
				copyStatus = bookCopy.checkStatus(callNo[i], copyNo[i]);
				if (copyStatus.equalsIgnoreCase("in")) {
					currBorr.insertBorrowing(i + 1000, bid, callNo[i],
							copyNo[i]);
					bookCopy.setStatusOut(callNo[i], copyNo[i]);
					System.out.println("Book: " + callNo[i]
							+ "has been checked out. \n return date: ");
				} else {
					System.out.println("Sorry, the book" + callNo[i] + " is "
							+ copyStatus);

				}
			}
		}
		return null;

	}

	//how do we determine the amount we fine the borrower?
	// takes in callNo and copyNo and sets bookCopy as in if no hold requests, 
	//else on hold. If it is overdue, a fine of $1 is added
	public void returnBook(int callNo, int copyNo) {
		int bid;
		int borid;
		Borrowing bor = new Borrowing();
		Fine fine = new Fine();
		bookCopy bookCopy = new bookCopy();
		holdRequest hr = new holdRequest();

		bid = bor.getBidOfBook(callNo, copyNo);
		borid = bor.getBoridOfBook(callNo, copyNo);
		bor.setInDate(callNo, copyNo);
		
		if (bor.isOverdue(borid)) {
			fine.insertFine(callNo + copyNo, 1, borid);
		}
		if (hr.hasHold(callNo)) {
			bookCopy.setStatusHold(callNo, copyNo);
		} else {
			bookCopy.setStatusIn(callNo, copyNo);
		}

	}

	//unfinished - still have to print out book titles and info instead of callNo and copyNo
	public Object[][] checkOverdue() {
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
		Borrowing bor = new Borrowing();
		
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
			overdueList = new Object[count][5];
			while(rs.next()){
				bid = rs.getInt("borr_bid");
				borid = rs.getInt("borrowing_borid");
				callNo = rs.getInt("book_callNo");
				copyNo = rs.getInt("bookcopy_copyNo");
				outDate = rs.getDate("borrowing_outDate");

				
				stmt2 = con.createStatement();
				rs2 = stmt2.executeQuery("SELECT book_title FROM book WHERE book_callno =" + callNo);
				rs2.next();
				title = rs2.getString("book_title");

					if(bor.isOverdue(borid)){
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
	
	//checks array (testing method)
	public void printArray(){
		Object[][] overdueList = checkOverdue();
		for(int k = 0; k < overdueListCount; k++){
			for(int j = 0; j < 5 ; j++){
				System.out.print(overdueList[k][j] + " ");

			}
			System.out.println(" ");
		}
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Clerk clerkTest = new Clerk();
				Borrowing borTest = new Borrowing();
				borrowerTable borrower = new borrowerTable();
				bookCopy bookC = new bookCopy();
				int[] callNo = new int[3];
				callNo[0] = 1;
				callNo[1] = 1;
				callNo[2] = 10;

				int[] copyNo = new int[3];
				copyNo[0] = 1;
				copyNo[1] = 2;
				copyNo[2] = 1;
				// borrower.displayBorrower();
				// clerkTest.addBorrower("nam", "pass", "address", 9090,
				// "email@email.ubc.com", 12341, 1);
				// borrower.displayBorrower();
				//clerkTest.checkOut(10, callNo, copyNo);
				//clerkTest.returnBook(10, 1);
				// borTest.displayBorrowing();
				//borTest.displayBorrowing();
				//bookC.displayBookCopy();
				clerkTest.printArray();

			}
		});
	}

}
