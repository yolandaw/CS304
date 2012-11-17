import java.io.*;



public class Librarian {

	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


	public Librarian() {
		// TODO Auto-generated constructor stub
	}

	public void addBook () {


		try {
			System.out.print("\nCall Number: ");
			int callNo = Integer.parseInt(in.readLine());
			System.out.print("\nISBN: ");
			int isbn = Integer.parseInt(in.readLine());
			System.out.print("\nTitle: ");
			String title = in.readLine();
			System.out.print("\nAuthor: ");
			String mainAuthor = in.readLine();
			System.out.print("\nPublisher: ");
			String publisher = in.readLine();
			System.out.print("\nYear Published: ");
			int year = Integer.parseInt(in.readLine());	

			Book book = new Book();
			if (book.checkBook(isbn) == 1) {
				addCopy(callNo);
			} else {
				book.insertBook(callNo, isbn, title, mainAuthor, publisher, year);
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
			   case 1:  status = "in"; break;
			   case 2:  status = "out"; break;
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

}
