//unfinished class - need to add if/else cases in some of the sql queries and double check -Yolanda

import java.sql.*;
import java.io.*;
import java.sql.Date;



public class Fine {
	
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	java.sql.Connection con = Connection.getInstance().getConnection();

	public Fine() {
		
	}

	
	// Insert a tuple into the table Fine
	public void insertFine(int fid, float amount, Date issueDate, Date paidDate, int borid) {
		
		PreparedStatement ps;
		
		java.sql.Date date = java.sql.Date.valueOf("2012-12-12");
		try{
			ps = con.prepareStatement("INSERT INTO fine VALUES(?,?,?,?,?,)");
			
			ps.setInt(1, fid);
			ps.setFloat(2, amount);
			ps.setDate(3, date);
			ps.setDate(4, date);
			ps.setInt(5, borid);
			
			ps.executeUpdate();
			
			con.commit();
			
			ps.close();									
		}
		
		catch(SQLException ex){
			
			System.out.println("Message:" + ex.getMessage());
			try{
				con.rollback();
			}
			catch(SQLException ex2){
				System.out.println("Message:" + ex2.getMessage());
				System.exit(-1);
			}
			
		}
		
	}
	
	// Delete a tuple from the table Fine
	public void deleteFine(int fid) {
		
		PreparedStatement ps;
		
		try{
			ps = con.prepareStatement("DELETE FROM fine WHERE fine_fid = ?");
			ps.setInt(1, fid);
			
			int rowCount = ps.executeUpdate();
			
			if(rowCount == 0){
				System.out.println("\nFine" + fid + "does not exist!");
			}
			
			con.commit();
			
			ps.close();
		}
		
		catch(SQLException ex){
			System.out.println("Message:" + ex.getMessage());
			
			try{
				con.rollback();
			}
			catch(SQLException ex2){
				System.out.println("Message:" + ex2.getMessage());
				System.exit(-1);
			}
		}
		
	}
	
	// Display all the rows of the table Fine
	public void displayFine(int fid, float amount, Date issueDate, Date paidDate, int borid) {

/*		Statement stmt;
		ResultSet rs;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM branch");
			
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int numCols = rsmd.getColumnCount();
			
			System.out.println(" ");
			
			for ( int i = 0; i<numCols; i++){
				
				System.out.printf("%-15s", rsmd.getColumnName(i+1));
				
			}
			
			System.out.println(" ");
			
			while(rs.next()){
				
				fid = rs.getInt("fine_fid");
				System.out.printf("%-10.10s", fid);
				
				amount = rs.getFloat("fine_amount");
				System.out.printf("%-20.20s", amount);
				
				issueDate = rs.getDate("fine_issueDate");
				System.out.printf("%-20.20s", issueDate);
				
				paidDate = rs.getDate("fine_paidDate");
				System.out.printf("%-20.20s", paidDate);
				
				borid = rs.getInt("borrowing_borid");
				System.out.printf("%-20.20s", borid);
				
				stmt.close();
			}
		}
				
		catch(SQLException ex){
			
			System.out.println("Message:" + ex.getMessage());
								
		}*/
			
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
		Fine runFine = new Fine();
		//runFine.connect("ora_v2e7","a75190090");
		runFine.insertFine(0, 0, null, null, 10);
	
            }});
	
		}
	
}
