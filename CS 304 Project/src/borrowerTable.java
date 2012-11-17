import java.io.*;
import java.sql.*;

public class borrowerTable {

	java.sql.Connection con = Connection.getInstance().getConnection();

	public borrowerTable() {

	}

	
	// Insert a borrower into the table Borrower
	public void insertBorrower(String name, String password, String address, int phone, String email, int sinOrStNo, String borrowerType) {
		
		//TODO: generate of bid and expiryDate
		int bid = 12345;
		java.sql.Date date = java.sql.Date.valueOf("2012-12-12");
		
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO borrower VALUES(?,?,?,?,?,?,?,?,?)");
			ps.setInt(1, bid);
			ps.setString(2, password);
			ps.setString(3, name);
			ps.setString(4, address);
			ps.setInt(5, phone);
			ps.setString(6, email);
			ps.setInt(7, sinOrStNo);
			ps.setDate(8, date);
			ps.setString(9, borrowerType);
			ps.executeUpdate();
			con.commit();		
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
	
	// Delete a borrower from the table Borrower
	public void deleteBorrower(int bid) {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM borrower WHERE borr_bid = ?");
			ps.setInt(1, bid);
			ps.executeUpdate();
			con.commit();		
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
	
	// Display all the rows of the table Borrower
	public void displayBorrower() {
	//TODO
	}
	
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                borrowerTable testTable = new borrowerTable();
                testTable.insertBorrower("name", "password", "address", 1234567, "email@email.com", 11111111, "borrowertype");
   //             testTable.deleteBorrower(12345);
            }
        });
	}
}
