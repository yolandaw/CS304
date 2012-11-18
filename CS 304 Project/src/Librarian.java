import java.io.*;



public class Librarian {

	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


	public Librarian() {
		// TODO Auto-generated constructor stub
	}

	public void addBook () {


		try {

			System.out.print("\nISBN: ");
			int isbn = Integer.parseInt(in.readLine());

			Book book = new Book();
			int existingCallNo = book.checkBook(isbn);
			if (book.checkBook(isbn) == 0) {
				System.out.print("\nNo matching book found in system. Add a new book: ");
				System.out.print("\nCall Number: ");
				int callNo = Integer.parseInt(in.readLine());
				if (book.numCopies(callNo) == 0) {
					System.out.print("\nTitle: ");
					String title = in.readLine();
					System.out.print("\nAuthor: ");
					String mainAuthor = in.readLine();
					System.out.print("\nPublisher: ");
					String publisher = in.readLine();
					System.out.print("\nYear Published: ");
					int year = Integer.parseInt(in.readLine());	
					book.insertBook(callNo, isbn, title, mainAuthor, publisher, year);			
				} else {
					System.out.print("\nCall Number exists, perhaps there was an error in entering the ISBN or Call Number.");	
					return;
				}
				
				addCopy(callNo);
			} else {
				System.out.print("\nBook exists, adding new copy:");
				addCopy(existingCallNo);
			}
		}

		catch (IOException e)
		{
			System.out.println("IOException!");
		}
	}

	public void addCopy (int callNo) {

		try {
			System.out.print("\n\nPlease choose one of the following statuses: \n");
			System.out.print("1.  In\n");
			System.out.print("2.  Checked Out\n");
			
			int choice = Integer.parseInt(in.readLine());
			String status = null;
			switch(choice)
			{
			   case 1: status = "in"; break;
			   case 2: status = "out"; break;
			}
			
			bookCopy copies = new bookCopy();
			copies.insertBookCopy(callNo, status);
		}

		catch (IOException e)
		{
			System.out.println("IOException!");
		}
	}



	public void generateBookReport () {

	}

	public void generatePopularBooksReport () {

	}
	
	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Librarian librarian = new Librarian();
				//     testTable.insertBook(3, 2, "a", "b", "c", 0);

				librarian.addBook();               

			}
		});
	}

}
