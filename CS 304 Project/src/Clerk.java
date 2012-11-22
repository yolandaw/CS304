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
