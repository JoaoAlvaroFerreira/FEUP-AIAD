package restaurant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JFrame;
import agents.ClientGroup;
import agents.Receptionist;
import agents.Waiter;
import agents.Cook;
import extras.Client;
import extras.Table;
import jade.Boot;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;

public class Restaurant {

	static ArrayList<Table> tables;
	private static ContainerController mainContainer;
	private static Runtime run;
	private static Profile profile;
	ArrayList<Waiter> waiters = new ArrayList<Waiter>();
	ArrayList<ClientGroup> clients = new ArrayList<ClientGroup>();
	ArrayList<Cook> cooks = new ArrayList<Cook>();
	public int averageUserRating, totalUserRating, totalClientsServed;
	RestaurantGUI gui;
	private static Receptionist r;

	public Restaurant(boolean strat, ArrayList<Table> new_tables, int waiters, int veg_cooks, int allerg_cooks, int kid_cooks) {
		newJade();
		r = null;
		totalUserRating = 0;
		averageUserRating = 0;
		totalClientsServed = 0;
		tables = new_tables;
		newRestaurant(strat, waiters, veg_cooks, allerg_cooks, kid_cooks);
		
	}
	
	
	public void updateUserRating(int userRating) {
		totalClientsServed++;
		totalUserRating += userRating;
		averageUserRating = totalUserRating/totalClientsServed;
		
		System.out.println("Restaurant Average User Rating:"+ averageUserRating+ "\n");
	}

	public void newJade() {
		
		run = Runtime.instance();
    	profile = new ProfileImpl();
    	profile.setParameter(Profile.CONTAINER_NAME, "TestContainer");
		profile.setParameter(Profile.MAIN_HOST, "localhost");
    	mainContainer = run.createMainContainer(profile);
	}
	
	
	private void generateCooks(int veg_cooks, int allerg_cooks, int kid_cooks) {
	
		
		System.out.println("Amount of cooks available: "+veg_cooks+allerg_cooks+kid_cooks+"\n With the following specializations:");
		System.out.println("Vegetarian food: "+ veg_cooks);
		System.out.println("Allergic food: "+ allerg_cooks);
		System.out.println("Children food: "+ kid_cooks);
		
		for(int i = 0; i < veg_cooks; i++) {					
			Cook cook = new Cook("VEGGIE");
			newAgent("cook_"+Integer.toString(i),cook) ;
			cooks.add(cook);
		}
		for(int j = 0; j < allerg_cooks; j++) {
			Cook cook = new Cook("ALLERGY");
			newAgent("cook_"+Integer.toString(veg_cooks+j),cook) ;
			cooks.add(cook);
		}
		for(int k = 0; k < kid_cooks; k++) {
			Cook cook = new Cook("CHILD");
			newAgent("cook_"+Integer.toString(veg_cooks+allerg_cooks+k),cook) ;
			cooks.add(cook);
		}
		
		System.out.println("\n");
		
	}
	
	private void generateWaiters(int waiter_amount) {
		
		System.out.println("Amount of waiters available: "+waiter_amount+"\n");
		for(int i = 0; i < waiter_amount; i++) {
			Waiter waiter = new Waiter();
			newAgent("waiter_"+Integer.toString(i),waiter);
			waiters.add(waiter);
		}
		
	}
	
	
	
	public void newRestaurant(boolean strat, int waiter_amount, int veg_cooks, int allerg_cooks, int kid_cooks) {
		
		generateCooks(veg_cooks, allerg_cooks, kid_cooks);
		
		generateWaiters(waiter_amount);
		r = new Receptionist(waiters, cooks, tables, strat);
		r.setRestaurant(this);
		newAgent("receptionist", r);

	}


	public static void newAgent(String agentID, Agent agent) {

		Object[] a = null;
        try {


			AgentController agentController = mainContainer.acceptNewAgent(agentID, agent);
            agentController.start();
        
        } catch (jade.wrapper.StaleProxyException e) {
            System.err.println("Error launching agent...");
        }
	}


	public void setGUI(RestaurantGUI restaurantGUI) {
		gui = restaurantGUI;
	}
	
	public RestaurantGUI getGUI() {
		return gui;
	}
	
	
	public Receptionist getReceptionist() {
		return r;
	}
}