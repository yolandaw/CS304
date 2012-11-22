import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Calendar;


public class Clerk {

	java.sql.Connection con = Connection.getInstance().getConnection();
	Calendar cal = Calendar.getInstance();

	public Clerk() {
		// TODO Auto-generated constructor stub
	}

	public void addBorrower(String name, String password, String address,
			int phone, String email, int sinOrStNo, String borrowerType) {

		borrowerTable newBorrower = new borrowerTable();

		
		// check if SIN or St. No already exists
		if (newBorrower.checkSinOrStNo(sinOrStNo) == true) {

			System.out.println("A borrower with the same SIN or Stu. No. already exists."); 
			
		} else {

			newBorrower.insertBorrower(name, password, address, phone, email,
					sinOrStNo, borrowerType);
		}

	}


	public void checkOut(int bid, int callNo, int copyNo) {
		// checks if borrower has any unpaid fines
		// takes in list of books and user bid
		
		Borrowing currBorr = new Borrowing();
		bookCopy bookCopy = new bookCopy();
		String copyStatus = bookCopy.checkStatus(callNo, copyNo);
		java.sql.Date pDate = (Date) cal.getTime();
		
		boolean checkFines= getFines(bid);
		
		if (checkFines == true)
		{
			System.out.println("Borrower has fines! Please pay fines before continuing");
		}
		
		else if (copyStatus != "in")
		{
			System.out.println("Sorry, the book is" + copyStatus);
		}
		
		else 
		{
			 currBorr.insertBorrowing(123, bid, callNo, copyNo, pDate, null);
			 System.out.println("Success! Return Date: ");
		}
		
	}
	
	
	public boolean getFines(int bid){
		Statement stmt;
		ResultSet rs; 
		boolean fineBool = false;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT fine_fid FROM fine f, borrowing b, " +
					"borrower br WHERE f.borrowing_bid = b.borrowing_bid AND b.borr_bid = " + bid);
			while(rs.next()){
				fineBool = true;
			
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fineBool;
	}
	

	public void bookReturn(int callNo, int copyNo) {
		
		
	}
	
	
	// Insert a tuple into the table Borrowing
	public void returnBook(int callNo, int copyNo) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO borrowing VALUES(?,?,?,?,?,?)");
			ps.setInt(1, borid);
			ps.setInt(2, bid);
			ps.setInt(3, callNo);
			ps.setInt(4, copyNo);
			ps.setDate(5, outDate);
			ps.setDate(6, inDate);
			ps.executeUpdate();
			con.commit();
			ps.close();
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
		    try 
		    {
			// undo the insert
			con.rollback();	
		    }
		    catch (SQLException ex2)
		    {
			System.out.println("Message: " + ex2.getMessage());
			System.exit(-1);
		    }

		}
	}

	public void checkOverdue() {
		
		

	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
		Clerk clerkTest = new Clerk();
		clerkTest.addBorrower("nam", "pass", "address", 9090, "email@email.ubc.com", 1234567, "borrowertype");
            }
        });
	}

}
