//unfinished class - need to add if/else cases in some of the sql queries and double check -Yolanda

import java.sql.*;
import java.io.*;
import java.sql.Date;
import java.lang.Object;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Fine {

	private BufferedReader in = new BufferedReader(new InputStreamReader(
			System.in));
	java.sql.Connection con = Connection.getInstance().getConnection();
	CastDate newDate = new CastDate();

	public Fine() {

	}

	// Insert a tuple into the table Fine
	public void insertFine(int fid, float amount, String issueDate,
			String paidDate, int borid) {

		PreparedStatement ps;

		try {
			ps = con.prepareStatement("INSERT INTO fine VALUES(?,?,?,?,?)");

			ps.setInt(1, fid);
			ps.setFloat(2, amount);
			ps.setDate(3, newDate.currentDate());
			ps.setDate(4, null);
			ps.setInt(5, borid);

			ps.executeUpdate();

			con.commit();

			ps.close();
		}

		catch (SQLException ex) {

			System.out.println("Message:" + ex.getMessage());
			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message:" + ex2.getMessage());
				System.exit(-1);
			}

		}

	}

	// Delete a tuple from the table Fine
	public void deleteFine(int fid) {

		PreparedStatement ps;

		try {
			ps = con.prepareStatement("DELETE FROM fine WHERE fine_fid = ?" );
			ps.setInt(1, fid);

			int rowCount = ps.executeUpdate();

			if (rowCount == 0) {
				System.out.println("\nFine" + fid + "does not exist!");
			}

			con.commit();

			ps.close();
		}

		catch (SQLException ex) {
			System.out.println("Message:" + ex.getMessage());

			try {
				con.rollback();
			} catch (SQLException ex2) {
				System.out.println("Message:" + ex2.getMessage());
				System.exit(-1);
			}
		}

	}
	
	//make a payment that pays all fine ids belonging to the bid - unfinished
	public void payAllFines(int bid){
		PreparedStatement ps;
		
		try{
			ps = con.prepareStatement("");
			
			ps.executeUpdate();
			con.commit();
			ps.close();
		}
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		}
	}
	
	//select a single fine to pay - unfinished
	public void payAFine(int bid, int fid){
		
	}

	//sets the fine paid date to current date
	public void setPaidDate(int fid){
		
		PreparedStatement ps; 
		
		try{
		ps = con.prepareStatement("UPDATE fine SET fine_paiddate = ? WHERE fine_fid = " + fid);

		ps.setDate(1, newDate.currentDate());
		ps.executeUpdate();
		con.commit();
		ps.close();
		
		}
		
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
			System.exit(-1);
		}
		

	}
	
	
	//prints out a list of fines based on inputed BID
	public void displayBIDFines(int bid){
		Statement stmt;
		ResultSet rs;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT DISTINCT f.fine_fid, f.fine_amount, f.fine_issuedate, f.fine_paiddate, f.borrowing_borid  FROM fine f, borrowing b, borrower br WHERE b.borr_bid = " + bid + "AND b.borrowing_borid = f.borrowing_borid");
			
			ResultSetMetaData rsmd = rs.getMetaData();

			int numCols = rsmd.getColumnCount();

			System.out.println(" ");

			for (int i = 0; i < numCols; i++) {

				System.out.printf("%-20s", rsmd.getColumnName(i + 1));

			}

			System.out.println(" ");
			
			int fid;
			float amount;
			Date issueDate;
			Date paidDate;
			int borid;


			while (rs.next()) {

				fid = rs.getInt("fine_fid");
				System.out.printf("%-20.20s", fid);

				amount = rs.getFloat("fine_amount");
				System.out.printf("%-20.20s", amount);

				issueDate = rs.getDate("fine_issueDate");
				System.out.printf("%-20.20s", issueDate);

				paidDate = rs.getDate("fine_paidDate");
				System.out.printf("%-20.20s", paidDate);

				borid = rs.getInt("borrowing_borid");
				System.out.printf("%-20.20s", borid);
				System.out.println(" ");
			}
			stmt.close();
		}
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		}
		
	}
	
	//returns all the unpaid fines of the inputed bid - unfinihsed
	public void displayUnPaidFines(int bid){
		Statement stmt;
		ResultSet rs;
		java.sql.Date nullDate = null;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT DISTINCT f.fine_fid, f.fine_amount, f.fine_issuedate, f.fine_paiddate, f.borrowing_borid  FROM fine f, borrowing b, borrower br WHERE b.borr_bid = " + bid + "AND b.borrowing_borid = f.borrowing_borid");
			ResultSetMetaData rsmd = rs.getMetaData();

			int numCols = rsmd.getColumnCount();

			System.out.println(" ");

			for (int i = 0; i < numCols; i++) {

				System.out.printf("%-20s", rsmd.getColumnName(i + 1));

			}

			System.out.println(" ");
			
			int fid;
			float amount;
			Date issueDate;
			Date paidDate;
			int borid;


			while (rs.next()) {

				fid = rs.getInt("fine_fid");
				System.out.printf("%-20.20s", fid);

				amount = rs.getFloat("fine_amount");
				System.out.printf("%-20.20s", amount);

				issueDate = rs.getDate("fine_issueDate");
				System.out.printf("%-20.20s", issueDate);

				paidDate = rs.getDate("fine_paidDate");
				System.out.printf("%-20.20s", paidDate);

				borid = rs.getInt("borrowing_borid");
				System.out.printf("%-20.20s", borid);
				System.out.println(" ");
			}
			stmt.close();
		}
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		}
		
	}
	

	// Display all the rows of the table Fine
	public void displayFine() {

		int fid;
		float amount;
		Date issueDate;
		Date paidDate;
		int borid;

		Statement stmt;
		ResultSet rs;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM Fine");

			ResultSetMetaData rsmd = rs.getMetaData();

			int numCols = rsmd.getColumnCount();

			System.out.println(" ");

			for (int i = 0; i < numCols; i++) {

				System.out.printf("%-20s", rsmd.getColumnName(i + 1));

			}

			System.out.println(" ");

			while (rs.next()) {

				fid = rs.getInt("fine_fid");
				System.out.printf("%-20.20s", fid);

				amount = rs.getFloat("fine_amount");
				System.out.printf("%-20.20s", amount);

				issueDate = rs.getDate("fine_issueDate");
				System.out.printf("%-20.20s", issueDate);

				paidDate = rs.getDate("fine_paidDate");
				System.out.printf("%-20.20s", paidDate);

				borid = rs.getInt("borrowing_borid");
				System.out.printf("%-20.20s", borid);
				System.out.println(" ");
				

			}

			stmt.close();
		}

		catch (SQLException ex) {

			System.out.println("Message:" + ex.getMessage());

		}
		
		

	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Fine runFine = new Fine();
				Calendar cal = Calendar.getInstance();
				// runFine.connect("ora_v2e7","a75190090");
				runFine.insertFine(100, 20, "2012-12-12", null , 21);
				// runFine.deleteFine(11);
				//System.out.print(runFine.getBorrowingFineID(10));
				//runFine.getFines(10);
				runFine.setPaidDate(10);
				runFine.displayFine();
				java.sql.Date issueD = java.sql.Date.valueOf("2012-12-12");
				//System.out.println(runFine.getGDate(issueD));
				//cal.setTime(runFine.getGDate(issueD).getTime());
				System.out.println(cal.getTime());
				cal.add(cal.DATE, 20);
				System.out.println(cal.getTime());
				runFine.displayBIDFines(10);
				
			}
		});

	}

}
