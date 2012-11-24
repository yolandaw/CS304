import java.sql.DriverManager;
import java.sql.SQLException;


public class Connection {
	private static Connection connectionClass;
	private static java.sql.Connection con;
	Connection()
	{
		connectionClass = this;
		
		try 
		{
			// Load the Oracle JDBC driver
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
			System.exit(-1);
		}

		String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug"; 

		try 
		{
			con = DriverManager.getConnection(connectURL,"ora_v2e7","a75190090");

			System.out.println("\nConnected to Oracle!");

		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());

		}
	}

	public static Connection getInstance()
	{
		if ( connectionClass == null )
		{
			connectionClass = new Connection();
		}
		return connectionClass;
	}

	public java.sql.Connection getConnection() {
		return con;
	}

}
