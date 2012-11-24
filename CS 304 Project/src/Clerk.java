import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Calendar;


public class Clerk {

	java.sql.Connection con = Connection.getInstance().getConnection();
	Calendar cal = Calendar.getInstance();
	
	private String student = "student";
	private String faculty = "faculty";
	private String staff = "staff";
	private String general = "general";
	//Fine currFine = new Fine();
	CastDate newDate = new CastDate();
 

	public Clerk() {
		// TODO Auto-generated constructor stub
	}
	
	private String returnType(int intType){
		
		String type = null;
		
		switch(intType){
		case 1:
			type = student; break;
		case 2:
			type = faculty; break;
		case 3:
			type = staff; break;
		case 4:
			type = general; break;
		}
		return type;
		
	}

	//adds a new borrower to the database
	public void addBorrower(String name, String password, String address,
			int phone, String email, int sinOrStNo, int type) {
		
		borrowerTable newBorrower = new borrowerTable();

		
		// check if SIN or St. No already exists
		if (newBorrower.checkSinOrStNo(sinOrStNo) == true) {

			System.out.println("A borrower with the same SIN or Stu. No. already exists."); 
			
		} else {

			newBorrower.insertBorrower(name, password, address, phone, email,
					sinOrStNo, returnType(type));
		}
	}


	//Unfinihsed, input must be a list of call numbers
	public void checkOut(int bid, int[] callNo, int[] copyNo) {
		// checks if borrower has any unpaid fines
		// takes in list of books and user bid
		
		Fine currFine = new Fine();
		bookCopy bookCopy = new bookCopy();
		CastDate dueDate = new CastDate();
		borrowerTable bidMeta = new borrowerTable();
		String bidType = bidMeta.checkBorrowerType(bid);
		int timeLimit = bidMeta.getTimeLimit(bidType);
		String copyStatus = null;
		Borrowing currBorr = new Borrowing();
		
		if (currFine.checkHasFines(bid) == true)
		{
			System.out.println("Borrower has fines! Please pay fine before continuing.");
			//prompt user with a pop up if they want to pay fines now or later and if true then return the payFine window
		}
		else{
			
			for(int i = 0; i < callNo.length; i++){
				copyStatus	= bookCopy.checkStatus(callNo[i], copyNo[i]);
				if (copyStatus.equalsIgnoreCase("out"))
				{
					System.out.println("Sorry, the book" + callNo[i]+ " is " + copyStatus);
				}
				else{
					currBorr.insertBorrowing(i + 1000, bid, callNo[i], copyNo[i]);
					bookCopy.setStatusOut(callNo[i], copyNo[i]);
					System.out.println("Book: " + callNo[i] + "has been checked out. \n return date: " );
				}
			}
		}

	}
	
	
	// Insert a tuple into the table Borrowing
	public void returnBook(int callNo, int copyNo) {
		int bid;
		int borid;
		Borrowing bor = new Borrowing();
		Fine fine = new Fine();
		
		bid = bor.findBorrowerOfBook(callNo, copyNo);
		borid = bor.findBoridOfBook(callNo, copyNo);
		bor.isOverdue(borid);

		
	}

	public void checkOverdue() {
		
		
		

	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Clerk clerkTest = new Clerk();
				Borrowing borTest = new Borrowing();
				int[] callNo = new int[3];
				callNo[0] = 1;
				callNo[1] = 1;
				callNo[2] = 10;
				
				int[] copyNo = new int[3];
				copyNo[0] = 1;
				copyNo[1] = 2;
				copyNo[2] = 1;
//				clerkTest.addBorrower("nam", "pass", "address", 9090, "email@email.ubc.com", 12345670, 1);
	 			clerkTest.checkOut(10, callNo, copyNo);
	 			borTest.displayBorrowing();
			}
		});
	}

}
