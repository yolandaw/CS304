import java.sql.*;
 
public class Book {

	java.sql.Connection con = Connection.getInstance().getConnection();
	
	public Book () {
		
	}
	
	public int checkBook(int isbn) {

		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*) FROM book WHERE book_isbn = " + isbn);
			if (rs.next()) {
				return rs.getInt(1);
			}
			stmt.close();
			
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}

		
		return 0;
		
	}
	
	public int numCopies(int callNo) {
		
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*) FROM bookCopy WHERE book_callNo = " + callNo);
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println(count);
				return count;
			}
			stmt.close();
			
		}
		catch (SQLException ex)
		{
		    System.out.println("Message: " + ex.getMessage());
		}
		System.out.println("end 0");
		return 0;

		
	}
	
	// Insert a tuple into the table Book
	public void insertBook(int callNo, int isbn, String title, String mainAuthor, String publisher, int year) {

		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("INSERT INTO book VALUES (?,?,?,?,?,?)");
			ps.setInt(1, callNo);
			ps.setInt(2, isbn);
			ps.setString(3, title);
			ps.setString(4, mainAuthor);
			ps.setString(5, publisher);
			ps.setInt(6, year);			
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
		
	// Delete a tuple from the table Book
	public void deleteBook(int callNo) {
		
		PreparedStatement ps;
		
		try {
			ps = con.prepareStatement("DELETE FROM book WHERE book_callNo = ?");
			ps.setInt(1, callNo);
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
		
	// Display all the rows of the table Book
	public void displayBook() {
		String callNo;
		String isbn;
		String title;
		String mainAuthor;
		String publisher;
		String year;
		Statement stmt;
		ResultSet rs;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM book");
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
			    System.out.printf("%-10.10s", callNo);
			    
			    isbn = rs.getString("book_isbn");
			    System.out.printf("%-20.20s", isbn);
			    
			    title = rs.getString("book_title");
			    System.out.printf("%-20.20s", title);
			    
			    mainAuthor = rs.getString("book_mainAuthor");
			    System.out.printf("%-20.20s", mainAuthor);
			    
			    publisher = rs.getString("book_publisher");
			    System.out.printf("%-20.20s", publisher);
			    
			    year = rs.getString("book_year");
			    System.out.printf("%-20.20s", year);
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
                Book testTable = new Book();
           //     testTable.insertBook(3, 2, "a", "b", "c", 0);

                testTable.numCopies(1);
                testTable.numCopies(2);
                testTable.numCopies(20102);                
                
            }
        });
	}
	
}
