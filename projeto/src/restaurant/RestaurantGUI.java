package restaurant;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Canvas;
import java.awt.Panel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RestaurantGUI {

	private JFrame frame;
	JTextField textField;
	JTextField textField_1;
	JTextField textField_2;
	JButton btnNewButton; //start Button
	JRadioButton rdbtnRandom;
	JRadioButton rdbtnTimePriority;
	static boolean start;
	private JLabel lblTableNumber;
	JTextField textField_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		start = false;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RestaurantGUI window = new RestaurantGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RestaurantGUI() {
		initialize();
	}
	
	private void startSimulation() {

	       boolean random = true;
	       if(rdbtnTimePriority.isSelected()) {
	    	   random = false;
	       }
	       Restaurant simulation = new Restaurant(random,Integer.parseInt(textField.getText()),Integer.parseInt(textField_1.getText()),
	    		   Integer.parseInt(textField_2.getText()),Integer.parseInt(textField_3.getText()));
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(650, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{114, 67, 86, 68, 86, 76, 86, 57, 0};
		gbl_panel.rowHeights = new int[]{23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		System.setOut(printStream);
		System.setErr(printStream);
		
		
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridheight = 11;
		gbc_textArea.gridwidth = 6;
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 2;
		gbc_textArea.gridy = 1;
		panel.add(textArea, gbc_textArea);
		panel.add(new JScrollPane(textArea), gbc_textArea);
		
		
		

		ButtonGroup bgroup = new ButtonGroup();
		
		btnNewButton = new JButton("Start");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startSimulation();
			}

			
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridwidth = 2;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 4;
		panel.add(btnNewButton, gbc_btnNewButton);
		
		JLabel lblClientNumber = new JLabel("Client Number");
		GridBagConstraints gbc_lblClientNumber = new GridBagConstraints();
		gbc_lblClientNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblClientNumber.gridx = 0;
		gbc_lblClientNumber.gridy = 5;
		panel.add(lblClientNumber, gbc_lblClientNumber);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 5;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		textField.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void removeUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void insertUpdate(DocumentEvent e) {
			    changed();
			  }
			});
		
		JLabel lblCookNumber = new JLabel("Cook Number:");
		GridBagConstraints gbc_lblCookNumber = new GridBagConstraints();
		gbc_lblCookNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblCookNumber.gridx = 0;
		gbc_lblCookNumber.gridy = 6;
		panel.add(lblCookNumber, gbc_lblCookNumber);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 6;
		panel.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		textField_1.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void removeUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void insertUpdate(DocumentEvent e) {
			    changed();
			  }
			});
		
		JLabel lblWaiterNumber = new JLabel("Waiter Number:");
		GridBagConstraints gbc_lblWaiterNumber = new GridBagConstraints();
		gbc_lblWaiterNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblWaiterNumber.gridx = 0;
		gbc_lblWaiterNumber.gridy = 7;
		panel.add(lblWaiterNumber, gbc_lblWaiterNumber);
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 7;
		panel.add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
		
		textField_2.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void removeUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void insertUpdate(DocumentEvent e) {
			    changed();
			  }
			});
		
		
		lblTableNumber = new JLabel("Table Number:");
		GridBagConstraints gbc_lblTableNumber = new GridBagConstraints();
		gbc_lblTableNumber.insets = new Insets(0, 0, 5, 5);
		gbc_lblTableNumber.gridx = 0;
		gbc_lblTableNumber.gridy = 8;
		panel.add(lblTableNumber, gbc_lblTableNumber);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 8;
		panel.add(textField_3, gbc_textField_3);
		
		textField_3.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void removeUpdate(DocumentEvent e) {
			    changed();
			  }
			  public void insertUpdate(DocumentEvent e) {
			    changed();
			  }
			});
		
		rdbtnRandom = new JRadioButton("Random");
		GridBagConstraints gbc_rdbtnRandom = new GridBagConstraints();
		gbc_rdbtnRandom.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnRandom.gridx = 0;
		gbc_rdbtnRandom.gridy = 10;
		panel.add(rdbtnRandom, gbc_rdbtnRandom);
		bgroup.add(rdbtnRandom);
		
		rdbtnTimePriority = new JRadioButton("Time Priority");
		GridBagConstraints gbc_rdbtnTimePriority = new GridBagConstraints();
		gbc_rdbtnTimePriority.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnTimePriority.gridx = 0;
		gbc_rdbtnTimePriority.gridy = 11;
		panel.add(rdbtnTimePriority, gbc_rdbtnTimePriority);
		bgroup.add(rdbtnTimePriority);
		
	}
	  public void changed() {
		     if (textField.getText().equals("") || textField_1.getText().equals("") || textField_2.getText().equals("") ||  textField_3.getText().equals("") ){
		       btnNewButton.setEnabled(false);
		     }
		     else {
		       btnNewButton.setEnabled(true);
		    }

		  }


}
