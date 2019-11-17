package restaurant;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import agents.ClientGroup;
import extras.Client;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ClientCreatorGUI extends JFrame {

	Restaurant restaurant;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	JCheckBox chckbxSmokers;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public ClientCreatorGUI(Restaurant rest) {
		restaurant = rest;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnCreateClientGroup = new JButton("Create Client Group");
		btnCreateClientGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addClientGroup();
			}

		});
		btnCreateClientGroup.setBounds(230, 77, 152, 23);
		contentPane.add(btnCreateClientGroup);
		
		chckbxSmokers = new JCheckBox("Smokers");
		chckbxSmokers.setBounds(27, 77, 97, 23);
		contentPane.add(chckbxSmokers);
		
		JLabel lblNormal = new JLabel("Allergic");
		lblNormal.setBounds(27, 11, 46, 14);
		contentPane.add(lblNormal);
		
		JLabel lblVegetarians = new JLabel("Vegetarians:");
		lblVegetarians.setBounds(27, 34, 68, 14);
		contentPane.add(lblVegetarians);
		
		JLabel lblAllergic = new JLabel("Kids:");
		lblAllergic.setBounds(230, 11, 46, 14);
		contentPane.add(lblAllergic);
		
		JLabel lblKids = new JLabel("Normal:");
		lblKids.setBounds(230, 34, 46, 14);
		contentPane.add(lblKids);
		
		textField = new JTextField();
		textField.setText("0");
		textField.setBounds(118, 8, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setText("0");
		textField_1.setBounds(118, 31, 86, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setText("0");
		textField_2.setBounds(338, 8, 86, 20);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setText("0");
		textField_3.setBounds(338, 31, 86, 20);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
	}
	
	
	private void addClientGroup() {
		ClientGroup clientGroup;
		ArrayList<Client> clients = new ArrayList<Client>();
		boolean smoker = chckbxSmokers.isSelected();
		int normal_clients = Integer.parseInt(textField_3.getText());
		int veg_clients = Integer.parseInt(textField_1.getText());
		int allergic_clients = Integer.parseInt(textField.getText());
		int kid_clients = Integer.parseInt(textField_2.getText());
		
		for(int i = 0; i < normal_clients; i++ ) {
			clients.add(new Client(false,false,false,smoker));
		}
		for(int i = 0; i < veg_clients; i++ ) {
			clients.add(new Client(false,true,false,smoker));
		}
		for(int i = 0; i < allergic_clients; i++ ) {
			clients.add(new Client(true,false,false,smoker));
		}
		for(int i = 0; i < kid_clients; i++ ) {
			clients.add(new Client(false,false,true,false));
		}
		
		clientGroup = new ClientGroup(clients);
		restaurant.newAgent("client_group_"+restaurant.clients.size(), clientGroup);
		restaurant.clients.add(clientGroup);

	}

	
}
