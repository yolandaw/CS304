import java.sql.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.UIManager.*;

import java.awt.*;
import java.awt.event.*;

public class GUI implements ActionListener{
    
    //components for Main Interface
	private Librarian librarian;
	private Borrower borrower;
	private Clerk clerk;
	private borrowerTable bt;
	private Book book;
	private Fine fine;
	private bookCopy bc;
    private JFrame mainFrame;
    private JPanel dataPanel;
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
		fine = new Fine();
		book = new Book();
		bc = new bookCopy();
	}

	public void showLibrary() {

		mainFrame = new JFrame("Library System");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initializeMenu();
		dataPanel = new JPanel (new GridLayout(4,1));
		mainFrame.getContentPane().add(dataPanel, BorderLayout.NORTH);
		
		mainFrame.pack();
		mainFrame.setSize(500, 500);
		mainFrame.setVisible(true);
		dataPanel.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		System.exit(0);
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
		
		//Borrower - Search Books
		menuItem1a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String keyword = inputPopUp("Enter keywords: ");
				dataPanel.removeAll();
				try {
					String columnNames[] = {"Title", "Call Number", "Copy Number", "Status"};
					Object dataValues[][] = borrower.search(keyword);
					if (dataValues == null) {
						popUp("No results found for given keywords.");
					}
					else {
						JTable bookTable = new JTable (dataValues, columnNames);
						bookTable.setEnabled(false);
						JScrollPane sp = new JScrollPane(bookTable);
						sp.setPreferredSize(new Dimension (200,150));
						sp.setCorner(JScrollPane.UPPER_LEFT_CORNER, bookTable.getTableHeader());
						dataPanel.add(sp);
						JButton placeHold = new JButton ("Place Hold Request");
						placeHold.setSize(50, 50);
						dataPanel.add(placeHold, BorderLayout.SOUTH);
						mainFrame.revalidate();
						placeHold.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String id = inputPopUp("Enter Borrower ID:");
								try {
									if (bt.checkBid(Integer.parseInt(id))) {
										String callNo = inputPopUp("Enter Call Number:");
										if (bc.bookExist(Integer.parseInt(callNo))) {
											borrower.placeHold(Integer.parseInt(id), Integer.parseInt(callNo));
											popUp("Hold placed successfully!");
										}
										else {
											popUp("Error: Call Number does not exist.");
										}

									}
									else {
										popUp("Invalid Borrower ID " + id + ".");
									}
								}
								catch (NumberFormatException nm8) {
									
								}
							}
						});
						mainFrame.validate();
					}
				}
				catch (NumberFormatException nm) {
						
				}
			}
		});
		
		//Borrower - Check Account
		menuItem1b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = inputPopUp("Enter Borrower ID:");
				try {
					dataPanel.removeAll();
					if (bt.checkBid(Integer.parseInt(id))) {
						String columnName[] = {"Call Number", "Copy Number", "Title", "Out Date", "Due Date"
						, "Overdue Status"	};
						String columnName2[] = {"Call Number", "Title", "Issued Date"};
						String columnName3[] = {"Outstanding Fines"};
						Object dataValues[][] = borrower.checkLoans(Integer.parseInt(id));
						Object dataValues2[][] = borrower.checkHolds(Integer.parseInt(id));
						Object dataValues3[][] = borrower.checkFines(Integer.parseInt(id));

						if (dataValues == null && dataValues2 == null && dataValues3 == null) {
							popUp("No records associated with Borrower ID " + id + ".");
						}
						else {
							dataPanel.removeAll();
							JLabel table1Label = new JLabel ("Books Out");
							JLabel table2Label = new JLabel ("Hold Requests");
							JTable table1 = new JTable(dataValues,columnName);
							JTable table2 = new JTable(dataValues2,columnName2);
							JTable table3 = new JTable(dataValues3,columnName3);
							table1.setEnabled(false);
							table2.setEnabled(false);
							table3.setEnabled(false);
					
							JScrollPane sp1 = new JScrollPane(table1);
							JPanel table1Panel = new JPanel(new BorderLayout());
							table1Panel.add(table1Label, BorderLayout.NORTH);
							table1Panel.add(sp1);
					
							JScrollPane sp2 = new JScrollPane(table2);
							JPanel table2Panel = new JPanel(new BorderLayout());
							table2Panel.add(table2Label, BorderLayout.NORTH);
							table2Panel.add(sp2);
					
							JScrollPane sp3 = new JScrollPane(table3);
							sp1.setPreferredSize(new Dimension(200,150));
							sp2.setPreferredSize(new Dimension(200,150));
							sp3.setPreferredSize(new Dimension(200,150));

							dataPanel.add(table1Panel);
							dataPanel.add(table2Panel);
							dataPanel.add(sp3);
							mainFrame.revalidate();	
						}
					}
					else {
						popUp("Invalid Borrower ID" + id + ".");
					}
				}
				catch (NumberFormatException nm) {
				
				}
			}
		});
		
		//Borrower - Pay Fines
		menuItem1c.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = inputPopUp("Enter Borrower ID:");
				try {
					if (bt.checkBid(Integer.parseInt(id))) {
						if(borrower.payFine(Integer.parseInt(id))) {
							popUp("Fines successfully paid!");
						}
						else {
							popUp("No fines associated with Borrower ID " + id);
						}
					}
					else {
						popUp("Invalid Borrower ID " + id +".");
					}
				}
				catch (NumberFormatException nm) {
					
				}
			}
		});
		
		//Clerk - Add Borrower
		menuItem2a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JTextField name = new JTextField();
					JTextField password = new JTextField();
					JTextField address = new JTextField();
					JTextField phone = new JTextField();
					JTextField email = new JTextField();
					JTextField sinOrStNo = new JTextField();
					JRadioButton type1 = new JRadioButton("Student");
					JRadioButton type2 = new JRadioButton("Faculty");
					JRadioButton type3 = new JRadioButton("Staff");
					final JComponent[] inputs = new JComponent[] {
						new JLabel("Name"),name,
						new JLabel("Password"),password,
						//new JLabel("Verify Password"),passwordVerify,
						new JLabel("Address"),address,
						new JLabel("Phone"),phone,
						new JLabel("E-mail"),email,
						new JLabel("SIN or Student No."),sinOrStNo,
						new JLabel("Type"),type1,type2,type3
					};
					JOptionPane.showMessageDialog(null, inputs, "Add Borrower", JOptionPane.PLAIN_MESSAGE);

					int index = 0;
	
					if (type1.isSelected()) {
						index = 1;
					}
				
					else if (type2.isSelected()) {
						index = 2;
					}
				
					else if (type3.isSelected()) {
						index = 3;
					}
				
					if (clerk.addBorrower(name.getText(), password.getText(), address.getText(), Integer.parseInt(phone.getText()), email.getText(), Integer.parseInt(sinOrStNo.getText()), index)) {
						popUp("Borrower added successfully!");
					}
					else {
						popUp("SIN or Student Number " + sinOrStNo + " already exists!");
					}
				}
				catch (NumberFormatException nm2) {
					
				}
			}
		});
		
		//Clerk - Check Out Items
		menuItem2b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String bid = inputPopUp("Enter Borrower ID:");
				try {
					if (bt.checkBid(Integer.parseInt(bid))) {
						if (fine.checkHasFines(Integer.parseInt(bid))) {
							popUp("This borrower has outstanding fines. Please pay fines before continuing.");
						}
						else {
							int response = 0;
							while (response == 0) {
								int[] info = checkOutHelper();
								if (clerk.checkOut(Integer.parseInt(bid), info)) {
									response = JOptionPane.showConfirmDialog(null, "Check out more books?", "Select an Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								}
								else {
									popUp("Error: This book is already checked out by another user.");
									response = 0;
								}
							}
							String columnNames[] = {"Borrowing ID", "Call Number", "Copy Number", "Book Title", "Due Date"};
							Object[][] receipt = new Object[clerk.getCount()][5];
							
							if (clerk.getCount() > 0) {
								receipt = clerk.getReceipt();
								JTable receiptTable = new JTable(receipt, columnNames);
								receiptTable.setEnabled(false);
								JScrollPane sp = new JScrollPane(receiptTable);
								sp.setPreferredSize(new Dimension (200,150));
								sp.setCorner(JScrollPane.UPPER_LEFT_CORNER, receiptTable.getTableHeader());
								dataPanel.add(sp);
								mainFrame.revalidate();
							}
							else {
								popUp("No books were checked out.");
							}						
						}
					}
					else {
					
						popUp("Invalid Borrower ID " + bid + ".");
					}
				}
				catch (NumberFormatException nm) {
					
				}
			}
		});
		
		//Clerk - Process Returns
		menuItem2c.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JTextField callNo = new JTextField();
					JTextField copyNo = new JTextField();
					final JComponent[] inputs = new JComponent[] {
							new JLabel ("Call Number"),callNo,
							new JLabel ("Copy Number"),copyNo
					};
					JOptionPane.showMessageDialog(null, inputs, "Process Returns", JOptionPane.PLAIN_MESSAGE);
					String result = clerk.returnBook(Integer.parseInt(callNo.getText()), Integer.parseInt(copyNo.getText()));
					popUp(result);
				}
				catch (NumberFormatException nm3) {
					
				}
			}
		});
		
		//Clerk - Check Overdue Items
		menuItem2d.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String columnNames[] = {"Borrower ID", "Call Number", "Copy Number", "Title", "Out Date"};
				Object[][] results = clerk.checkOverdue();
				dataPanel.removeAll();
				JTable overdueTable = new JTable(results, columnNames);
				overdueTable.setEnabled(false);
				JScrollPane sp = new JScrollPane(overdueTable);
				sp.setCorner(JScrollPane.UPPER_LEFT_CORNER, overdueTable.getTableHeader());
				dataPanel.add(sp);
				mainFrame.revalidate();
				
			}
		});
		
		//Librarian - Add Book
		menuItem3a.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
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
						if (book.checkCallNo(Integer.parseInt(callNo.getText()))) {
							popUp("The Call Number " + callNo.getText() + " already exists. Please try a different Call Number.");
						}
						else {
							librarian.addBook(Integer.parseInt(callNo.getText()), isbn, title.getText(), mainAuthor.getText(), publisher.getText(), Integer.parseInt(year.getText()));
							int resp = addCopyHelper();
							librarian.addCopy(Integer.parseInt(callNo.getText()), resp);
							popUp("Book successfully added!");
						}
					}
					else {
						int response = JOptionPane.showConfirmDialog(null, "Book already exists! Add a copy instead?", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (response == 0 ) {
							int resp = addCopyHelper();
							int callNo = book.checkBook(isbn);
							librarian.addCopy(callNo, resp);
							popUp("Copy successfully added!");
						}
						else if (response == 1) {
						}
					}
				}
				catch (NumberFormatException nm2) {
					
				}
			}
		});
		
		//Librarian - Generate Book report
		menuItem3b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataPanel.removeAll();
				String columnNames[] = { "Call Number", "Copy Number", "Title", "Out Date", "Overdue" };
				Object dataValues[][] = librarian.generateBookReport();
				JTable bookTable = new JTable(dataValues,columnNames);
				dataPanel.add(new JScrollPane(bookTable));
				mainFrame.validate();
			}
		});
		
		//Librarian - Generate popular books
		menuItem3c.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dataPanel.removeAll();
					JTextField year = new JTextField();
					JTextField numBooks = new JTextField();
					final JComponent[] inputs = new JComponent[] {
							new JLabel("Year"),year,
							new JLabel("Number of Books to Display"),numBooks
					};
					JOptionPane.showMessageDialog(null, inputs, "Generate Popular Books", JOptionPane.PLAIN_MESSAGE);
					String columnNames[] = {"Call Number", "Title", "# of Times Borrowed" };
					Object dataValues[][] = librarian.generatePopularBooksReport(Integer.parseInt(year.getText()), Integer.parseInt(numBooks.getText()));
					if (dataValues == null) {
						popUp("No results for given year");
					}
					else {
						JTable bookTable = new JTable(dataValues,columnNames);
						dataPanel.add(new JScrollPane(bookTable));
						mainFrame.validate();
					}
				}
				catch (NumberFormatException nm2) {
					
				}
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
	
	private int[] checkOutHelper() {
		JTextField callNo = new JTextField();
		JTextField copyNo = new JTextField();
		final JComponent[] inputs = new JComponent[] {
			new JLabel("Call Number"), callNo,
			new JLabel("Copy Number"), copyNo
		};
		
		JOptionPane.showMessageDialog(null, inputs, "Check Out Items", JOptionPane.PLAIN_MESSAGE);
		int[] result = new int[2];
		result[0] = Integer.parseInt(callNo.getText());
		result[1] = Integer.parseInt(copyNo.getText());
		return result;
	}
	
	private int addCopyHelper() {

		JRadioButton status1 = new JRadioButton("In");
		JRadioButton status2 = new JRadioButton("Out");
		JRadioButton status3 = new JRadioButton("On Hold");
		final JComponent[] inputs = new JComponent[] {
				new JLabel ("Select Status of Book Copy"),status1,status2,status3};
		JOptionPane.showMessageDialog(null, inputs, "Add Copy", JOptionPane.PLAIN_MESSAGE);
		int status = 0;
		if (status1.isSelected()) {
				status = 0;
		}
		else if (status2.isSelected()) {
			status = 1;
		}
		else if (status3.isSelected()) {
			status = 2;
		}	
		return status;
	}
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI gui = new GUI();
                gui.showLibrary();
            }
        });
	}
}