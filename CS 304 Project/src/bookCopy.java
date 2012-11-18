import java.sql.*;

public class bookCopy {
	
	public bookCopy() {
		
	}
	java.sql.Connection con = Connection.getInstance().getConnection();
	
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
		
	// Display all the rows of the table BookCopy
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
				System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			}

			while(rs.next()) {
				callNo = rs.getString("book_callNo");
				System.out.printf("\n%-10.10s", callNo);

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
            }
        });
	}
	
}
