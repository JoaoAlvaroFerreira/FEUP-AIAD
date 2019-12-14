package restaurant;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import agents.ClientGroup;
import extras.Client;
import extras.Table;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

public class RestaurantGUI {

	private JFrame frame;
	private JTextField txtChildren;
	private JTextField textField_2;
	JButton btnNewButton; // start Button
	JRadioButton rdbtnRandom;
	JRadioButton rdbtnTimePriority;
	static boolean start;
	private JLabel lblTableNumber;
	JTextField textField_3;
	private JCheckBox chckbxSmokers;
	private JButton btnAddTable;
	private JButton btnNewButton_1;
	private JButton btnNewButton_5;
	private JLabel lblAllergic;
	private JTextField textVegetarian;
	private JTextField textAllergic;
	private JTextArea tableArea;
	private static ArrayList<Table> tables;
	Restaurant simulation;
	private ClientGroup clientGroup;
	private static Random rand;
	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		start = false;
		tables = new ArrayList<Table>();
		rand = new Random();
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
		if (rdbtnTimePriority.isSelected()) {
			random = false;
		}

		simulation = new Restaurant(rdbtnTimePriority.isSelected(), tables, Integer.parseInt(textField_2.getText()),
				Integer.parseInt(textVegetarian.getText()), Integer.parseInt(textAllergic.getText()),
				Integer.parseInt(txtChildren.getText()));
		simulation.setGUI(this);
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setSize(1200, 1000);
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

		tableArea = new JTextArea();
		tableArea.setEditable(false);
		JScrollPane tablePane = new JScrollPane(tableArea);
		tablePane.setBounds(231, 10, 900, 150);
		panel.add(tablePane);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		System.setOut(printStream);
		System.setErr(printStream);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(231, 182, 900, 700);
		panel.add(scrollPane);

		JLabel lblCookNumber = new JLabel("Cooks:");
		lblCookNumber.setBounds(0, 84, 68, 14);
		panel.add(lblCookNumber);

		txtChildren = new JTextField();
		txtChildren.setText("0");
		txtChildren.setToolTipText("Children food specialists");
		txtChildren.setBounds(120, 106, 52, 20);
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
		textField_2.setText("0");
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
		textField_3.setText("0");
		textField_3.setBounds(120, 233, 52, 20);
		textField_3.setColumns(10);
		panel.add(textField_3);

		chckbxSmokers = new JCheckBox("Smokers");
		chckbxSmokers.setBounds(0, 257, 100, 23);
		panel.add(chckbxSmokers);

		btnAddTable = new JButton("Add Table");
		btnAddTable.setBounds(83, 280, 140, 23);
		btnAddTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				addTable(Integer.parseInt(textField_3.getText()), chckbxSmokers.isSelected());
				changed();
			}

		});
		panel.add(btnAddTable);

		rdbtnRandom = new JRadioButton("Greedy");
		rdbtnRandom.setSelected(true);
		rdbtnRandom.setBounds(0, 310, 120, 23);
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
		
		btnNewButton_5 = new JButton("Generate Random Client");
		btnNewButton_5.setEnabled(false);

		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				addClients();
			}
		});
		btnNewButton_5.setBounds(0, 380, 226, 23);
		panel.add(btnNewButton_5);

		rdbtnTimePriority = new JRadioButton("Optimized");
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
		textVegetarian.setText("0");
		textVegetarian.setToolTipText("Vegeterian food specialists");
		textVegetarian.setColumns(10);
		textVegetarian.setBounds(120, 130, 52, 20);
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
		textAllergic.setText("0");
		textAllergic.setToolTipText("Allergic food specialists");
		textAllergic.setColumns(10);
		textAllergic.setBounds(120, 153, 52, 20);
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

	public void tableContent() {

		String a = "Average User Rating: "+simulation.averageUserRating + "\n";
		
		//Descomentar para fazer display a waiting lists

	/*	a = a.concat("Clients waiting for table:");
		if (!simulation.getReceptionist().getwaitingAvailableWaiterTable().isEmpty()) {

			for (int j = 0; j < simulation.getReceptionist().getwaitingAvailableWaiterTable().size(); j++) {

				a = a.concat(" "+simulation.getReceptionist().getwaitingAvailableWaiterTable().get(j).getSender().getLocalName());

				
					a = a.concat(";");
			}
		}
		a = a.concat("\nClients waiting for food:");
		if (!simulation.getReceptionist().getWaitingAvailableCook().isEmpty()) {
			System.out.println("d");
			for (int k = 0; k < simulation.getReceptionist().getWaitingAvailableCook().size(); k++) {
				System.out.println("e");
				a = a.concat(" "+simulation.getReceptionist().getWaitingAvailableCook().get(k).getSender().getLocalName());
				
					a = a.concat(";");
			}
		}

		a = a.concat("\n"); */
		for (int i = 0; i < tables.size(); i++) {
			a = a.concat("Table #" + i + " - Size: " + tables.get(i).getSeats() + " Zone:"
					+ ((tables.get(i).isSmokers()) ? "Smokers" : "Non-Smokers"));
			if (tables.get(i).getEmpty())
				a = a.concat("\n");
			else
				a = a.concat(" - Occupied by: " + tables.get(i).getClientID() + "\n");
		}

		tableArea.setText(a);
	}

	public void changed() {
		if (textField_2.getText().equals("") || tables.size() == 0 || (txtChildren.getText().equals("")
				&& textAllergic.getText().equals("") && textVegetarian.getText().equals(""))) {
			btnNewButton.setEnabled(false);
		} else {
			btnNewButton.setEnabled(true);
		}

	}

	private void addTable(int table_size, boolean smoking) {

		tables.add(new Table(table_size, smoking));
		String smokers = (smoking ? "in the smokers section" : "in the non-smokers section");
		System.out.println("Table for " + table_size + " people available " + smokers);

	}
	
	private void addClients() {
		
			int i = rand.nextInt(3);
			int j = rand.nextInt(3);
			int k = rand.nextInt(3);
			int l = rand.nextInt(3);
						clientGroup = new ClientGroup(i,j,k,l, rand.nextBoolean());
						clientGroup.setRestaurant(simulation);
						simulation.newAgent("client_group_"+simulation.clients.size(), clientGroup);
						simulation.clients.add(clientGroup);
									
					
						
				 
	}

}
