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
	private static boolean strategy;

//	public Restaurant(boolean random,int clients, int cooks, int waiters, int tables) {
//		
//		newJade();
//		newRestaurant(clients, cooks, waiters, tables);
//	}
//	
	public Restaurant(boolean strat, ArrayList<Table> new_tables, int waiters, int veg_cooks, int allerg_cooks, int kid_cooks) {
		newJade();
		strategy = strat;
		tables = new_tables;
		newRestaurant(waiters, veg_cooks, allerg_cooks, kid_cooks);
	}
	
	
	public void newJade() {
		
		run = Runtime.instance();
    	profile = new ProfileImpl();
    	profile.setParameter(Profile.CONTAINER_NAME, "TestContainer");
		profile.setParameter(Profile.MAIN_HOST, "localhost");
    	mainContainer = run.createMainContainer(profile);
	}
	
	
	private void generateClients(int client_amount) {
		
		Random rand = new Random();
		ArrayList<Client> client_group;
		ClientGroup client_group_agent;
		int group_size;
		int client_unique_trait;
		for(int i = 0; i < client_amount; i++) { //CREATES #GROUPS = CLIENT_AMOUNT
			client_group = new ArrayList<Client>();
			group_size = rand.nextInt(10)+1; //MAKES GROUPS FROM 1 TO 10 PEOPLE, RANDOMLY FOR EACH GROUP
			
			System.out.println("New client group of "+group_size+" with the following clients:");
			for(int j = 0; j < group_size; j++) { //ADDS THE PEOPLE TO THE GROUP
				client_unique_trait = rand.nextInt(5); //DECIDES IF THE PERSON WILL BE VEGETARIAN, CHILD, SMOKER, ALLERGIC TO SOMETHING OR NO SPECIFIC TRAITS
				
					switch(client_unique_trait) {
					case 0: //NORMAL
						client_group.add(new Client(false,false,false,false));
						System.out.println("An average adult.");
					break;
					case 1: //ALLERGY
						client_group.add(new Client(true,false,false,false));
						System.out.println("An allergic adult.");
					break;
					case 2: //VEGETARIAN
						client_group.add(new Client(false,true,false,false));
						System.out.println("A vegetarian adult.");
					break;
					case 3: //KID
						client_group.add(new Client(false,false,true,false));
						System.out.println("A child.");
					break;
					case 4: //SMOKER
						client_group.add(new Client(false,false,false,true));
						System.out.println("A smoker.");
					break;
						
					}
					
			} 
			client_group_agent = new ClientGroup(client_group);
			System.out.println("\n");
			newAgent("client_group_"+Integer.toString(i), client_group_agent);
			clients.add(client_group_agent);
		}
		
		

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
	
	
	
	public void newRestaurant(int waiter_amount, int veg_cooks, int allerg_cooks, int kid_cooks) {
		
				
		
		
		generateCooks(veg_cooks, allerg_cooks, kid_cooks);
		
		//generateClients(client_amount);
		
		generateWaiters(waiter_amount);
		
		newAgent("receptionist", new Receptionist(waiters, cooks, tables));

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
}