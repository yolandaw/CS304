import java.sql.*;

public class bookCopy {
	
	public bookCopy() {
		
	}
	java.sql.Connection con = Connection.getInstance().getConnection();
	
	//check status of book copy
	public String checkStatus(int callNo, int copyNo){
		String returnStatus;
		Statement stmt;
		ResultSet rs;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT bookcopy_status FROM bookCopy WHERE book_callNo =" + callNo + "AND bookCopy_copyNo =" + copyNo);
			while(rs.next()){
				returnStatus = rs.getString(1);
				return returnStatus;
			}
			stmt.close();
		}

		catch(SQLException e){
			System.out.print("Message: " + e.getMessage());
		}
		
		return "Unavailable";
		
	}
	public boolean noBook(int callNo, int copyNo){
		Statement stmt;
		ResultSet rs;
		int count = 0;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*) FROM bookCopy WHERE book_callNo =" + callNo + "AND bookCopy_copyNo =" + copyNo);
			while(rs.next()){
				count = rs.getInt(1);

			}
			stmt.close();
		}

		catch(SQLException e){
			System.out.print("Message: " + e.getMessage());
		}

		if (count > 0){
			return false;
		}
		else{
			return true;
		}
	}
	
	public boolean bookExist(int callNo){
		Statement stmt;
		ResultSet rs;
		int count = 0;
		
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*) FROM book WHERE book_callNo =" + callNo);
			while(rs.next()){
				count = rs.getInt(1);

			}
			stmt.close();
		}

		catch(SQLException e){
			System.out.print("Message: " + e.getMessage());
		}

		if (count > 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	//set book copy to out status
	public void setStatusOut(int callNo, int copyNo){
		PreparedStatement ps;
	
		try{
			ps = con.prepareStatement("UPDATE bookCopy SET bookCopy_status = ? WHERE book_callNo=? AND bookcopy_copyNo=?");
			ps.setString(1, "out");
			ps.setInt(2,callNo);
			ps.setInt(3,copyNo);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
		}
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		    try 
		    {
			// undo the update
			con.rollback();	
		    }
		    catch (SQLException ex2)
		    {
			System.out.println("Message: " + ex2.getMessage());
			System.exit(-1);
		    }
		}
	}
	
	//set book copy to in status
	public void setStatusIn(int callNo, int copyNo){
		PreparedStatement ps;
		
		try{
			ps = con.prepareStatement("UPDATE bookCopy SET bookCopy_status = ? WHERE book_callNo=? AND bookcopy_copyNo=?");
			ps.setString(1, "in");
			ps.setInt(2,callNo);
			ps.setInt(3,copyNo);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
		}
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		    try 
		    {
			// undo the update
			con.rollback();	
		    }
		    catch (SQLException ex2)
		    {
			System.out.println("Message: " + ex2.getMessage());
			System.exit(-1);
		    }
		}
	}
	
	public void setStatusHold(int callNo, int copyNo){
		PreparedStatement ps;
		
		try{
			ps = con.prepareStatement("UPDATE bookCopy SET bookCopy_status = ? WHERE book_callNo=? AND bookcopy_copyNo=?");
			ps.setString(1, "on hold");
			ps.setInt(2,callNo);
			ps.setInt(3,copyNo);
			
			ps.executeUpdate();
			con.commit();
			ps.close();
		}
		catch(SQLException e){
			System.out.println("Message: " + e.getMessage());
		    try 
		    {
			// undo the update
			con.rollback();	
		    }
		    catch (SQLException ex2)
		    {
			System.out.println("Message: " + ex2.getMessage());
			System.exit(-1);
		    }
		}
	}
	
	// Insert a tuple into the table BookCopy
	public void insertBookCopy(int callNo, String status) {
		
		PreparedStatement ps;
		try {
			
			
			Book book = new Book();
			int copyNo = book.numCopies(callNo) + 1;
			
			ps = con.prepareStatement("INSERT INTO bookCopy VALUES (?," + copyNo + ",?)");
			ps.setInt(1, callNo);
			ps.setString(2, status);
			
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
	
	// Delete a tuple from the table BookCopy
	public void deleteBookCopy(int callNo, int copyNo) {
		PreparedStatement ps;
		try {
			ps = con.prepareStatement("DELETE FROM bookCopy WHERE book_callNo = ? AND bookCopy_copyNo = ?");
			ps.setInt(1, callNo);
			ps.setInt(2, copyNo);
			ps.executeUpdate();
			
			con.commit();
			ps.close();
		}

		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());

		    try 
		    {
		    	con.rollback();	
		    }
		    catch (SQLException ex2)
		    {
		    	System.out.println("Message: " + ex2.getMessage());
		    	System.exit(-1);
		    }
		}

	}
		
	// Disply all the rows of the table BookCopy
	public void displayBookCopy() {
		String callNo;
		String copyNo;
		String status;
		Statement stmt;
		ResultSet rs;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM bookCopy");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();

			System.out.println(" ");

			for (int i = 0; i < numCols; i++)
			{
				// get column name and print it
				System.out.printf("%-20s", rsmd.getColumnName(i+1));    
			}

			while(rs.next()) {
				callNo = rs.getString("book_callNo");
				System.out.printf("\n%-20.20s", callNo);

				copyNo = rs.getString("bookCopy_copyNo");
				System.out.printf("%-20.20s", copyNo);

				status = rs.getString("bookCopy_status");
				System.out.printf("%-20.20s", status);
			}
			stmt.close();
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
		}
	}

	
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                bookCopy testTable = new bookCopy();
                testTable.displayBookCopy();
                //System.out.println(" ");
//                System.out.println("check Status of:");
//                System.out.println("callNo 1 copy 10:  " + testTable.checkStatus(1, 10));
//                System.out.println("callNo 1 copy 1: " + testTable.checkStatus(1, 1));
//                System.out.println("callNo 1 copy 2: " + testTable.checkStatus(1, 2));
//                System.out.println("callNo 102 copy 2: " + testTable.checkStatus(102, 2));
                //System.out.println("callNo 2 copy 1: " + testTable.checkStatus(2, 1));

                testTable.setStatusIn(98,2);
                testTable.setStatusIn(1,8);
                testTable.setStatusIn(1,5);
                testTable.setStatusIn(1,7);
                testTable.setStatusIn(1,4);
                testTable.setStatusIn(10,1);
 //              System.out.println("callNo 2 copy 1: " + testTable.checkStatus(2, 1));
                testTable.displayBookCopy();

         // System.out.println("callNo 2 copy 1: " + testTable.checkStatus(2, 1));

            
                
            }
        });
	}


	
}
