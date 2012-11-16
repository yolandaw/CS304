import java.io.*;
import java.sql.*;
 
public class Book {
	
	public Book () {
		
	}

	private Connection con;
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
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
	
}
