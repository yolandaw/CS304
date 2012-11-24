
// We need to import the java.sql package to use JDBC
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

// for reading from the command line
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class GUI implements ActionListener{
    
    //components for Main Interface
	private Librarian librarian;
    private JFrame mainFrame;
    private JPanel libPanel;
    private JTextArea textArea;

	public GUI() {
		librarian = new Librarian();
		textArea = new JTextArea();
	}

	public void showLibrary() {
		mainFrame = new JFrame("Library System");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		libPanel = new JPanel (new GridLayout(1,1));
		libPanel.add(new JLabel ("Library Panel: "));
		
		initializeButtons();
		
		mainFrame.getContentPane().add(libPanel, BorderLayout.NORTH);
		
		mainFrame.pack();
		mainFrame.setSize(700, 700);
		mainFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		textArea.setText("1st Test");
	}
    
	private void initializeButtons() {
		JButton addBook = new JButton ("Add Book");
		libPanel.add(addBook);
		
		addBook.addActionListener(new ActionListener() {
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
					popUp("Book with ISBN " + isbn + " already exists!");
				}
			}
		});
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
