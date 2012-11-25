import java.sql.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

public class GUI implements ActionListener{
    
    //components for Main Interface
	private Librarian librarian;
	private Borrower borrower;
	private Clerk clerk;
	private borrowerTable bt;
    private JFrame mainFrame;
    private JPanel dataPanel;
    private JTextArea textArea;
    private JMenu menu1;
    private JMenu menu2;
    private JMenu menu3;
    private JMenuItem menuItem1a;
    private JMenuItem menuItem1b;
    private JMenuItem menuItem1c;
    private JMenuItem menuItem2a;
    private JMenuItem menuItem2b;
    private JMenuItem menuItem2c;
    private JMenuItem menuItem2d;
    private JMenuItem menuItem3a;
    private JMenuItem menuItem3b;
    private JMenuItem menuItem3c;
    private JMenuBar menuBar;

	public GUI() {
		librarian = new Librarian();
		borrower = new Borrower();
		clerk = new Clerk();
		bt = new borrowerTable();
		textArea = new JTextArea();
	}

	public void showLibrary() {

		mainFrame = new JFrame("Library System");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initializeMenu();
		dataPanel = new JPanel ();

		mainFrame.getContentPane().add(dataPanel, BorderLayout.NORTH);
		
		mainFrame.pack();
		mainFrame.setSize(500, 500);
		mainFrame.setVisible(true);
		dataPanel.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
    
	private void initializeMenu() {
		menu1 = new JMenu ("Borrower");
		menu2 = new JMenu ("Clerk");
		menu3 = new JMenu ("Librarian");
		
		menuItem1a = new JMenuItem("Search Books");
		menuItem1b = new JMenuItem("Check Account");
		menuItem1c = new JMenuItem("Pay Fine");
		menuItem2a = new JMenuItem("Add Borrower");
		menuItem2b = new JMenuItem("Check Out Items");
		menuItem2c = new JMenuItem("Process Returns");
		menuItem2d = new JMenuItem("Check Overdue Items");
		menuItem3a = new JMenuItem("Add Book");
		menuItem3b = new JMenuItem("All Loans");
		menuItem3c = new JMenuItem("Popular Books");
		textArea.setEditable(false);
		
		//Borrower - Search Books
		menuItem1a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String keyword = inputPopUp("Enter keywords: ");
				List<List<String>> searchedBook = new ArrayList<List<String>>(1);
				searchedBook = borrower.search(keyword);
				StringBuilder builder = new StringBuilder();
				for (List<String> searched : searchedBook) {
					builder.append(searched).append("\n");
				}
				textArea.setText(builder.toString());
				JButton placeHold = new JButton ("Place Hold Request");
				textArea.add(placeHold);
			}
		});
		
//		
//		JButton placeHold = new JButton ("Place Hold Request");
//		borrowerPanel.add(placeHold);
		
//		placeHold.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String id = inputPopUp("Enter Borrower ID:");
//			}
//		});
		
		//Borrower - Check Account
		menuItem1b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = inputPopUp("Enter Borrower ID:");
				if (!bt.checkBid(Integer.parseInt(id))) {
					popUp("Invalid Borrower ID! Please try again.");
				}
				else {
					List<List<String>> searchedID = new ArrayList<List<String>>(1);
					searchedID = borrower.checkAccount(Integer.parseInt(id));
					StringBuilder builder = new StringBuilder();
					for (List<String> searchID : searchedID) {
						builder.append(searchID).append("\n");
					}
					textArea.setText(builder.toString());
				}
			}
		});
		
		//Borrower - Pay Fines
		menuItem1c.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = inputPopUp("Enter Borrower ID:");
				if(borrower.payFine(Integer.parseInt(id))) {
					popUp("Fines successfully paid!");
				}
				else {
					popUp("No fines associated with Borrower ID" + id);
				}
			}
		});
		
		//Clerk - Add Borrower
		menuItem2a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		//Clerk - Check Out Items
		menuItem2b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		//Clerk - Process Returns
		menuItem2c.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		//Clerk - Check Overdue Items
		menuItem2d.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		//Librarian - Add Book
		menuItem3a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int isbn = Integer.parseInt(GUI.inputPopUp("ISBN: "));
				if (!librarian.checkISBN(isbn)) {
					JTextField callNo = new JTextField();
					JTextField title = new JTextField();
					JTextField mainAuthor = new JTextField();
					JTextField publisher = new JTextField();
					JTextField year = new JTextField();
					final JComponent[] inputs = new JComponent[] {
							new JLabel ("Call Number"),callNo,
							new JLabel ("Title"),title,
							new JLabel ("Main Author"),mainAuthor,
							new JLabel ("Publisher"),publisher,
							new JLabel ("Year"),year};
					JOptionPane.showMessageDialog(null, inputs, "Add Book", JOptionPane.PLAIN_MESSAGE);
					librarian.addBook(Integer.parseInt(callNo.getText()), isbn, title.getText(), mainAuthor.getText(), publisher.getText(), Integer.parseInt(year.getText()));
					popUp("Book added successfully!");
				}
				else {
					popUp("Book with ISBN " + isbn + " already exists! Try to add a copy instead.");
				}
			}
		});
		
//		JButton addCopy = new JButton ("Add Copy");
//		libPanel.add(addCopy);
//		
//		addCopy.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String[] status = {"In", "Out", "On Hold"};
//				final JTextField callNo = new JTextField();
//				final JComboBox box = new JComboBox();				
//				for (int i = 0; i < status.length; i++) {
//					box.addItem(status[i]);
//	
//				}
//				box.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						int count = box.getSelectedIndex();
//						librarian.addCopy(Integer.parseInt(callNo.getText()), count);
//					}
//				});
//				final JComponent[] inputs = new JComponent[] {
//						new JLabel ("Call Number"),callNo,
//						new JLabel ("Status"),box};
//				JOptionPane.showMessageDialog(null, inputs, "Add Copy", JOptionPane.PLAIN_MESSAGE);
//				popUp("Copy added successfully!");
//			}
//		});
		
		//Librarian - Generate Book report
		menuItem3b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				List<List<String>> libArray = new ArrayList<List<String>>(1);
//				libArray = librarian.generateBookReport();
//				StringBuilder builder = new StringBuilder();
//				for (List<String> lib : libArray) {
//					builder.append(lib).append("\n");
//				}
//				textArea.setText(builder.toString());
				
				String columnNames[] = { "Call Number", "Copy Number", "Out Date", "Overdue" };
				String dataValues[][] =
					{
						{ "12", "234", "67", "" },
						{ "-123", "43", "853", "" },
						{ "93", "89.2", "109", "" },
						{ "279", "9033", "3092", "" },
						{ "279", "9033", "3092", "" },

					};
				JTable bookTable = new JTable(dataValues,columnNames);
				dataPanel.add(new JScrollPane(bookTable));
				mainFrame.validate();
			}
		});
		
		//Librarian - Generate popular books
		menuItem3c.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//dataPanel.removeAll();
				JTextField year = new JTextField();
				JTextField numBooks = new JTextField();
				final JComponent[] inputs = new JComponent[] {
					new JLabel("Year"),year,
					new JLabel("Number of Books to Display"),numBooks
				};
				JOptionPane.showMessageDialog(null, inputs, "Generate Popular Books", JOptionPane.PLAIN_MESSAGE);
				List<List<String>> popularArray = new ArrayList<List<String>>(1);
				popularArray = librarian.generatePopularBooksReport(Integer.parseInt(year.getText()), Integer.parseInt(numBooks.getText()));
				StringBuilder builder = new StringBuilder();
				for (List<String> pop : popularArray) {
					builder.append(pop).append("\n");
				}
				textArea.setText(builder.toString());
			}
		});

		menu1.add(menuItem1a);
		menu1.add(menuItem1b);
		menu1.add(menuItem1c);
		menu2.add(menuItem2a);
		menu2.add(menuItem2b);
		menu2.add(menuItem2c);
		menu2.add(menuItem2d);
		menu3.add(menuItem3a);
		menu3.add(menuItem3b);
		menu3.add(menuItem3c);
		menuBar = new JMenuBar();
		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu3);
		mainFrame.setJMenuBar(menuBar);
		
	}
	
	private static String inputPopUp(String message)
	{
		return JOptionPane.showInputDialog(null, message);
	}
	
	private static void popUp(String message)
	{
		JOptionPane.showMessageDialog(null, message);
	}
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI gui = new GUI();
                gui.showLibrary();
            }
        });
	}
}
