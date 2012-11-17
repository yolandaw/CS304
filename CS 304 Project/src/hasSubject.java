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
	public int displayHasSubject() {
		return 0;
			
	}
	
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                hasSubject testTable = new hasSubject();
                testTable.insertHasSubject(12345, "subject");
                testTable.insertHasSubject(12345, "subject2");
                testTable.deleteHasSubject(12345, "subject");
            }
        });
	}
}
