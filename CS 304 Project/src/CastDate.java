import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CastDate {

	public CastDate() {
		// TODO Auto-generated constructor stub
	}

	// gets current date from system locale in SQL format
	public Date currentDate() {

		Calendar cal = GregorianCalendar.getInstance();

		java.util.Date utilDate = cal.getTime();
		java.sql.Date SQLDate = new java.sql.Date(utilDate.getTime());

		return SQLDate;
	}
	
	//add number of days via 365 days a year to the sql you pass through
	//for an alternate way to add time period to date instead of this function, refer to borrowerTable insertBorrower
	public Date addToDate(java.sql.Date date, int days){
		
		Calendar cal = new GregorianCalendar();
		
		cal.setTime(date);
		
		cal.add(Calendar.DAY_OF_YEAR, days);
		
		java.util.Date utilDate = cal.getTime();
		java.sql.Date SQLDate = new java.sql.Date(utilDate.getTime());
		
		return SQLDate;
		
	}

	// takes a calendar object and returns an sql date
	public Date getSQLDate(Calendar cal) {

		java.util.Date utilDate = cal.getTime();
		java.sql.Date SQLDate = new java.sql.Date(utilDate.getTime());

		return SQLDate;
	}

	// returns a Gregorian calendar date type of the SQLDate
	public Calendar getGDate(java.sql.Date SQLDate) {

		Calendar cal = new GregorianCalendar();

		cal.setTime(SQLDate);

		return cal;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CastDate newDate = new CastDate();
				Calendar cal = Calendar.getInstance();
				java.sql.Date issueD = java.sql.Date.valueOf("2012-12-12");
				//System.out.println(newDate.getGDate(issueD));
				cal.setTime(newDate.getGDate(issueD).getTime());
				System.out.println(cal.getTime());
				cal.add(Calendar.DATE, 20);
				System.out.println(cal.getTime());

			}
		});

	}
}
