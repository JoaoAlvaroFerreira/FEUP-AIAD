package restaurant;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RestaurantGUI extends JFrame{
	 JButton btnStart;
	 JPanel pnlHolder;
	RestaurantGUI(){
	   pnlHolder = new JPanel();
       btnStart = new JButton("Start");
       
       pnlHolder.add(btnStart);
       this.add(pnlHolder);
       
	}
}
