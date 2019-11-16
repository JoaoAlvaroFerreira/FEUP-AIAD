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
import extras.Dish;
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
	static Dish dish;
	private static ContainerController mainContainer;
	private static Runtime run;
	private static Profile profile;

	public static void main(String[] args){

        RestaurantGUI GUI = new RestaurantGUI();

        GUI.setTitle("Graphical User Interface");
        GUI.setSize(600, 400);
        GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GUI.setVisible(true);
    
		newJade();
		newRestaurant();
		//INSERT LOOP HERE
	}
	
	public static void newJade() {
		
		run = Runtime.instance();
    	profile = new ProfileImpl();
    	profile.setParameter(Profile.CONTAINER_NAME, "TestContainer");
		profile.setParameter(Profile.MAIN_HOST, "localhost");
    	mainContainer = run.createMainContainer(profile);
	}
	
	public static void newRestaurant() {
		
		Random rand = new Random();
		int tables2 = rand.nextInt(15);
		int tables4 = rand.nextInt(10);
		tables = new ArrayList<Table>();
		dish = new Dish(false, false, false);

		for(int i = 0; i < tables2; i++) {
			tables.add(new Table(2, rand.nextBoolean()));
		}

		System.out.println(tables2 + " tables for 2 created.");
		for(int j = 0; j < tables4; j++) {
			tables.add(new Table(4, rand.nextBoolean()));
		}

		System.out.println(tables4 + " tables for 4 created.");
		
		//create here stuff to make agents, depending on our needs
		
		//THIS MAKES A CLIENT GROUP OF TWO MEAT-EATING NON-ALLERGIC ADULTS, ONE SMOKER, EVENTUALLY MAKE IT SERIALIZED
		ArrayList<ClientGroup> clients = new ArrayList<>();
		ArrayList<Client> clients1 = new ArrayList<Client>();
		clients1.add(new Client(false,false,false, true));
		clients1.add(new Client(false,false,false, false));
		ClientGroup clientgroup1 = new ClientGroup(clients1);
		clients.add(clientgroup1);
		newAgent("client_group_01", clientgroup1);

		//THIS MAKES A WAITER, EVENTUALLY MAKE IT SERIALIZED
		ConcurrentHashMap<String, Waiter> waiters = new ConcurrentHashMap<>();
		Waiter waiter_01 = new Waiter(tables);
		newAgent("waiter_01", waiter_01);
		waiters.put("waiter_01", waiter_01);


		//THIS MAKES A COOK, EVENTUALLY WE NEED A TEAM :)
		ConcurrentHashMap<String, Cook> cooks = new ConcurrentHashMap<>();
		Cook cook_01 = new Cook(dish, "");
		newAgent("cook_01", cook_01);
		cooks.put("cook_01", cook_01);

		//CREATES RECEPTIONIST WHICH MANAGES AVAILABLE COOKS AND WAITERS
		newAgent("receptionist", new Receptionist(waiters, cooks, clients));

	}


	public static void newAgent(String agentID, Agent agent) {

		Object[] a = null;
        try {

			AgentController agentController = mainContainer.acceptNewAgent(agentID, agent);
            agentController.start();
            System.out.println("client agent");

        } catch (jade.wrapper.StaleProxyException e) {
            System.err.println("Error launching agent...");
        }
	}
}
