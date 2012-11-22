import java.sql.*;


public class Clerk {

	java.sql.Connection con = Connection.getInstance().getConnection();

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

	public void checkOut(int bid, int callNo) {
		// checks if borrower has any unpaid fines
		// takes in list of books and user bid
		
		Borrower currentBorrower = new Borrower();
		currentBorrower.checkAccount(bid);
		
		boolean checkFines= getFines(bid);
		
		if (checkFines == false)
		{
			
		}
		
		else 
		{
			System.out.println("Borrower has fines! Please pay fines before continuing"); 
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
	

	public void bookReturn() {
		
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
