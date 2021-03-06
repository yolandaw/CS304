import java.sql.*;


public class hasSubject {

	java.sql.Connection con = Connection.getInstance().getConnection();
	
	public hasSubject() {

	}
	
	// Insert a tuple into the table HasSubject
	public void insertHasSubject(int bookCallNo, String subject) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO hasSubject VALUES(?,?)");
			ps.setInt(1, bookCallNo);
			ps.setString(2, subject);
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
	
	// Delete a tuple from the table HasSubject
	public void deleteHasSubject(int bookCallNo, String subject) {
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM hasSubject WHERE book_callNo = ? AND hasSubject_subject = ?");
			ps.setInt(1, bookCallNo);
			ps.setString(2, subject);
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
	
	// Display all the rows of the table HasSubject
	public void displayHasSubject() {
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM hasSubject");
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();
			System.out.println(" ");
			
			for (int i = 0; i < numCols; i++)
			{
			    // get column name and print it
				System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			}
			
			System.out.println(" ");
			
			while (rs.next()) {
				int book_callNo = rs.getInt("book_callNo");
				System.out.printf("%-10.10s", book_callNo);
				
				String hasSubject_subject = rs.getString("hasSubject_subject");
				System.out.printf("%-10.10s", hasSubject_subject);
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
                hasSubject testTable = new hasSubject();
//                testTable.insertHasSubject(12345, "subject");
//                testTable.insertHasSubject(12345, "subject2");
//                testTable.deleteHasSubject(12345, "subject");
                testTable.displayHasSubject();
            }
        });
	}
}
