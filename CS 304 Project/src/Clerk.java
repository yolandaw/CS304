import java.sql.*;

public class Clerk {

	private String student = "student";
	private String faculty = "faculty";
	private String staff = "staff";
	java.sql.Connection con = Connection.getInstance().getConnection();
	private static int overdueListCount;
	private static int checkOutCount;
	private  static Object[][] receipt = new Object[30][5];

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
		}
		return type;

	}	
	
	// adds a new borrower to the database. If a borrower with the given sinOrStNo already exists, the method returns false. Otherwise, the borrower is added successfully and returns true.
	public boolean addBorrower(String name, String password, String address,
			int phone, String email, int sinOrStNo, int type) {

		borrowerTable newBorrower = new borrowerTable();

		// check if SIN or St. No already exists
		System.out.println(newBorrower.checkSID(sinOrStNo));
		if (type == 1 && newBorrower.checkSID(sinOrStNo)) {
			System.out.println("SID exists.");
			return false;
		}
		System.out.println(newBorrower.checkSin(sinOrStNo));
		System.out.println((type == 2 || type == 3 || type == 4));
		if ((type == 2 || type == 3) && newBorrower.checkSin(sinOrStNo)){
			System.out.println("SIN exists.");
			return false;
		}
		else {

			newBorrower.insertBorrower(name, password, address, phone, email,
					sinOrStNo, returnType(type));
			return true;
		}
	}

	// checks out a list of book
	// returns false if user has fines; otherwise, checkout is completed and returns true
	public boolean checkOut(int bid, int[] input) {

		bookCopy bookCopy = new bookCopy();
		Book book = new Book();
		String copyStatus = null;
		Borrowing currBorr = new Borrowing();
//			receipt = new Object[callNo.length][5];
			System.out.println("checkoutcount before borrowing is " + checkOutCount);
				copyStatus = bookCopy.checkStatus(input[0], input[1]);
				System.out.println("status is" + copyStatus);
				if (copyStatus.equalsIgnoreCase("in")) {
					currBorr.insertBorrowing(bid, input[0], input[1]);
					bookCopy.setStatusOut(input[0], input[1]);
					receipt[checkOutCount][0] = currBorr.getBoridOfBook(input[0], input[1]);
//					System.out.println(receipt[checkOutCount][0]);
					receipt[checkOutCount][1] = input[0];
//					System.out.println(receipt[checkOutCount][1]);
					receipt[checkOutCount][2] = input[1];
//					System.out.println(receipt[checkOutCount][2]);
					receipt[checkOutCount][3] = book.getTitle(input[0]);
//					System.out.println(receipt[checkOutCount][3]);
					receipt[checkOutCount][4] = currBorr.getDueDate(bid, currBorr.getBoridOfBook(input[0], input[1]));
//					System.out.println(receipt[checkOutCount][4]);
					checkOutCount++;
					System.out.println("checkoutcount after borrowing is " + checkOutCount);
					return true;
				} else {
					return false;

				
			}
		}

	public Object[][] getReceipt(){
		return receipt;
	}
	
	public int getCount(){
		return checkOutCount;
	}
	
//	public void clearReceipt(){
//		receipt = new Object[30][5];
//		checkOutCount = 0;
//	}
	

	
	// takes in callNo and copyNo and sets bookCopy as in if no hold requests, 
	//else on hold. If it is overdue, a fine of 50 cents per day is added
	public String returnBook(int callNo, int copyNo) {
		int bid;
		int borid;
		Borrowing bor = new Borrowing();
		Fine fine = new Fine();
		bookCopy bookCopy = new bookCopy();
		holdRequest hr = new holdRequest();
		String msg = null;

		bid = bor.getBidOfBook(callNo, copyNo);
		borid = bor.getBoridOfBook(callNo, copyNo);
		bor.setInDate(callNo, copyNo);
		if(bookCopy.checkStatus(callNo, copyNo).equals("in")){
			msg = "Book Copy has already been returned previously.";
			return msg;
		}
		if (bor.isOverdue(borid)) {
			fine.insertFine(callNo + copyNo, fine.calculateFine(bor.getDueDate(bid, borid)), borid);
		}
		if (hr.hasHold(callNo)) {
			bookCopy.setStatusHold(callNo, copyNo);
		} else {
			bookCopy.setStatusIn(callNo, copyNo);
			msg = "Book is returned.";
		}
		return msg;

	}

	//returns list of all overdue books and bids of borrowers who took them out 
	public Object[][] checkOverdue() {
		Statement stmt;
		ResultSet rs = null;
		Object[][] overdueList = null;
		int bid;
		int borid;
		int callNo;
		int copyNo;
		Date outDate;
		String title = null;
		int count = 0;
		Borrowing bor = new Borrowing();
		Book book = new Book();
		
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
			if(count > 0){
			overdueList = new Object[count][5];
			}
			while(rs.next()){
				bid = rs.getInt("borr_bid");
				borid = rs.getInt("borrowing_borid");
				callNo = rs.getInt("book_callNo");
				copyNo = rs.getInt("bookcopy_copyNo");
				outDate = rs.getDate("borrowing_outDate");
				title = book.getTitle(callNo);
				

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

		System.out.println("checkoutcount in printarray is " + checkOutCount);
		for(int k = 0; k < checkOutCount; k++){
			for(int j = 0; j < 5 ; j++){
				
				System.out.println(receipt[k][j] + " ");

			}
			System.out.println(" ");
		}
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Clerk clerkTest = new Clerk();
//				Borrowing borTest = new Borrowing();
//				borrowerTable borrower = new borrowerTable();
//				bookCopy bookC = new bookCopy();
//				int[] callNo = new int[3];
//				callNo[0] = 1;
//				callNo[1] = 1;
//				callNo[2] = 10;
//
//				int[] copyNo = new int[3];
//				copyNo[0] = 1;
//				copyNo[1] = 2;
//				copyNo[2] = 1;
//				 borrower.displayBorrower();
				// clerkTest.addBorrower("VI", "vip", "korea", 705, "yb@yg.com", 12345698, 1);
//				 "email@email.ubc.com", 12341, 1);
//				 borrower.displayBorrower();
//				clerkTest.checkOut(10, callNo, copyNo);
//				clerkTest.returnBook(10, 1);
//				 borTest.displayBorrowing();
//				borTest.displayBorrowing();
//				bookC.displayBookCopy();
//				clerkTest.printArray();
//				 System.out.println(clerkTest.returnBook(1, 7));
				int[] input = new int[2];
				input[0] = 1;
				input[1] = 3;
				int[] input2 = new int[2];
				input2[0] = 10;
				input2[1] = 1;
				int[] input3 = new int[2];
				input3[0] = 1;
				input3[1] = 10;
//				System.out.println(clerkTest.checkOut(10, input2));
				System.out.println(clerkTest.checkOut(10, input2));
				System.out.println(clerkTest.checkOut(10, input3));
				clerkTest.printArray();

			}
		});
	}

}
