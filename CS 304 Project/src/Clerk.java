import java.sql.*;



public class Clerk {

	private String student = "student";
	private String faculty = "faculty";
	private String staff = "staff";
	private String general = "general";
	java.sql.Connection con = Connection.getInstance().getConnection();

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

	// adds a new borrower to the database
	public void addBorrower(String name, String password, String address,
			int phone, String email, int sinOrStNo, int type) {

		borrowerTable newBorrower = new borrowerTable();

		// check if SIN or St. No already exists
		if (newBorrower.checkSinOrStNo(sinOrStNo) == true) {

			System.out
					.println("A borrower with the same SIN or Stu. No. already exists.");

		} else {

			newBorrower.insertBorrower(name, password, address, phone, email,
					sinOrStNo, returnType(type));
		}
	}

	// checks out a list of books - unfinished (still have to get return expiry dates)
	public void checkOut(int bid, int[] callNo, int[] copyNo) {

		Fine currFine = new Fine();
		bookCopy bookCopy = new bookCopy();
		String copyStatus = null;
		Borrowing currBorr = new Borrowing();

		if (currFine.checkHasFines(bid) == true) {
			System.out
					.println("Borrower has fines! Please pay fine before continuing.");
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

		bid = bor.findBorrowerOfBook(callNo, copyNo);
		borid = bor.findBoridOfBook(callNo, copyNo);
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

	//unfinihsed
	public ResultSet checkOverdue() {

		ResultSet rs = null;
		
		return rs;
		
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
				clerkTest.returnBook(10, 1);
				// borTest.displayBorrowing();
				borTest.displayBorrowing();
				bookC.displayBookCopy();

			}
		});
	}

}
