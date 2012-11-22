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
	
	//unfinished
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

	// returns an array of fine IDs that match the given bid 
/*	public int[] getFines(int bid){
		Statement stmt;
		ResultSet rs; 
		int[] fineArray = null;
		int size;

		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT fine_fid FROM fine f, borrowing b, borrower br WHERE f.borrowing_bid = b.borrowing_bid AND b.borr_bid = " + bid);
			
			size = rs.getFetchSize();
			fineArray = new int[size];
			
			while(rs.next()){
				for(int i = 0; i <= size; i++){
					fineArray[i] = rs.getInt(1);
				}
			
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		return fineArray;

		
	}
	*/
	
/*	public int getBorrowingFineID(int borid){
		Statement stmt;
		ResultSet rs;
		Statement stmt2;
		ResultSet rs2;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT f.fine_fid FROM fine f, borrowing b WHERE f.borrowing_borid = b.borrowing_borid");
			stmt2 = con.createStatement();
			rs2 = stmt2.executeQuery("SELECT ");
		}
		
		catch(SQLException ex){
			System.out.println("Message: " + ex.getMessage());
			System.exit(-1);
		}
		return borid;
		
	}
	
public boolean checkFine(int bid) {
		Statement stmt;
		Statement stmt2;
		ResultSet rs;
		ResultSet rs2;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*)  FROM fine");
			stmt2 = con.createStatement();
			rs2 = stmt2.executeQuery("SELECT COUNT(*) FROM borrowing");

			while (rs.next()) {
				if (rs == rs2) {
					System.out.println(rs);
					System.out.println(rs2);
					return true;
				}
			}
		} catch (SQLException ex) {
			System.out.println("Message:" + ex.getMessage());
		}
		return false;

	}*/

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
				//runFine.insertFine(99, 20, "2012-12-12", "2012-12-12", 21);
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
				
			}
		});

	}

}
