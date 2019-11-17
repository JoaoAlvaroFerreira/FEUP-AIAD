package restaurant;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import extras.Table;

import java.awt.Canvas;
import java.awt.Panel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;
import javax.swing.JDesktopPane;

public class RestaurantGUI {

	private JFrame frame;
	private JTextField txtChildren;
	private JTextField textField_2;
	JButton btnNewButton; //start Button
	JRadioButton rdbtnRandom;
	JRadioButton rdbtnTimePriority;
	static boolean start;
	private JLabel lblTableNumber;
	JTextField textField_3;
	private JCheckBox chckbxSmokers;
	private JButton btnAddTable;
	private JButton btnNewButton_1;
	private JLabel lblAllergic;
	private JTextField textVegetarian;
	private JTextField textAllergic;
	private static ArrayList<Table> tables;
	Restaurant simulation;
	
	/**
	 * Launch the application.
	 */
	
	
	public static void main(String[] args) {
		start = false;
		tables = new ArrayList<Table>();
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
	//	Restaurant simulation = new Restaurant(random,Integer.parseInt(textField.getText()),Integer.parseInt(txtChildren.getText()),
	  //  		   Integer.parseInt(textField_2.getText()),Integer.parseInt(textField_3.getText()));
		 simulation = new Restaurant(rdbtnTimePriority.isSelected(), tables, Integer.parseInt(textField_2.getText()), 
				Integer.parseInt(textVegetarian.getText()), Integer.parseInt(textAllergic.getText()), Integer.parseInt(txtChildren.getText()));
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setSize(650, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
	
		
		
		

		ButtonGroup bgroup = new ButtonGroup();
		
		btnNewButton = new JButton("Start");
		btnNewButton.setBounds(0, 11, 226, 23);
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startSimulation();
				btnNewButton_1.setEnabled(true);
			}

			
		});
		panel.setLayout(null);
		panel.add(btnNewButton);
		
		
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		System.setOut(printStream);
		System.setErr(printStream);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(231, 182, 403, 216);
		panel.add(scrollPane);	
		
		JLabel lblCookNumber = new JLabel("Cooks:");
		lblCookNumber.setBounds(0, 84, 68, 14);
		panel.add(lblCookNumber);
		
		txtChildren = new JTextField();
		txtChildren.setToolTipText("Children food specialists");
		txtChildren.setBounds(83, 106, 52, 20);
		panel.add(txtChildren);
		txtChildren.setColumns(10);
		txtChildren.getDocument().addDocumentListener(new DocumentListener() {
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
		lblWaiterNumber.setBounds(0, 188, 100, 14);
		panel.add(lblWaiterNumber);
		
		textField_2 = new JTextField();
		textField_2.setBounds(120, 185, 52, 20);
		panel.add(textField_2);
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
		
		
		lblTableNumber = new JLabel("Table Size:");
		lblTableNumber.setBounds(0, 236, 100, 14);
		panel.add(lblTableNumber);
		
		textField_3 = new JTextField();
		textField_3.setBounds(120, 233, 52, 20);
		textField_3.setColumns(10);
		panel.add(textField_3);
		
		
		chckbxSmokers = new JCheckBox("Smokers");
		chckbxSmokers.setBounds(0, 257, 80, 23);
		panel.add(chckbxSmokers);
		
		btnAddTable = new JButton("Add Table");
		btnAddTable.setBounds(83, 264, 140, 23);
		btnAddTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				addTable(Integer.parseInt(textField_3.getText()),chckbxSmokers.isSelected());
				changed();
			}

			
		});
		panel.add(btnAddTable);
		
		rdbtnRandom = new JRadioButton("Greedy");
		rdbtnRandom.setSelected(true);
		rdbtnRandom.setBounds(0, 310, 65, 23);
		panel.add(rdbtnRandom);
		bgroup.add(rdbtnRandom);
		
		btnNewButton_1 = new JButton("Add Client");
		btnNewButton_1.setEnabled(false);
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClientCreatorGUI client_maker = new ClientCreatorGUI(simulation);
				client_maker.setVisible(true);
			}
		});
		btnNewButton_1.setBounds(0, 47, 226, 23);
		panel.add(btnNewButton_1);
		
		rdbtnTimePriority = new JRadioButton("Time Priority");
		rdbtnTimePriority.setBounds(0, 330, 130, 23);
		panel.add(rdbtnTimePriority);
		bgroup.add(rdbtnTimePriority);
		
		JLabel lblNewLabel = new JLabel("Child:");
		lblNewLabel.setBounds(0, 109, 46, 14);
		panel.add(lblNewLabel);
		
		JLabel lblVegetarian = new JLabel("Vegetarian:");
		lblVegetarian.setBounds(0, 131, 80, 14);
		panel.add(lblVegetarian);
		
		lblAllergic = new JLabel("Allergic:");
		lblAllergic.setBounds(0, 156, 80, 14);
		panel.add(lblAllergic);
		
		textVegetarian = new JTextField();
		textVegetarian.setToolTipText("Vegeterian food specialists");
		textVegetarian.setColumns(10);
		textVegetarian.setBounds(83, 130, 52, 20);
		panel.add(textVegetarian);
		textVegetarian.getDocument().addDocumentListener(new DocumentListener() {
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
		
		textAllergic = new JTextField();
		textAllergic.setToolTipText("Allergic food specialists");
		textAllergic.setColumns(10);
		textAllergic.setBounds(83, 153, 52, 20);
		panel.add(textAllergic);
		textAllergic.getDocument().addDocumentListener(new DocumentListener() {
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
		
	}
	

	  public void changed() {
		     if (textField_2.getText().equals("") || tables.size()==0 || (txtChildren.getText().equals("")&&textAllergic.getText().equals("")&&textVegetarian.getText().equals(""))){
		       btnNewButton.setEnabled(false);
		     }
		     else {
		       btnNewButton.setEnabled(true);
		    }

		  }
	  
	  private void addTable(int table_size, boolean smoking) {
		
				tables.add(new Table(table_size,smoking));
				String smokers = (smoking ? "in the smokers section" : "in the non-smokers section");
				System.out.println("Table for "+table_size+" people available "+smokers);

	}
	
}
