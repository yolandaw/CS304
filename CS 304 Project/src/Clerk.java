import java.sql.*;

//only added some trivial stuff, i'll continue after friday if nobody has done anything - Yolanda

public class Clerk{
	
	java.sql.Connection con = Connection.getInstance().getConnection();

	public Clerk() {
		// TODO Auto-generated constructor stub
	}
	
	public void addBorrower() throws SQLException{
		
		
	}
	
	public void checkOut(int bid, int callNo ){
		
		PreparedStatement ps;	
	}
	
	public void bookReturn(){
		
	}

	public void checkOverdue(){
		
	}
	
	public boolean checkStatus(int bid){
		Statement stmt;
		ResultSet rs;
		try{
		stmt = con.createStatement();
		rs = stmt.executeQuery("SELECT bt_type FROM borrower WHERE borr_bid = "+ bid );
		
		}
		catch (SQLException ex)
		{
			System.out.println("Message:"+ex.getMessage());
		}
		return false;
		
	}
}
