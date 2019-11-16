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
	ConcurrentHashMap<String, Waiter> waiters = new ConcurrentHashMap<>();
	ArrayList<ClientGroup> clients = new ArrayList<ClientGroup>();
	ConcurrentHashMap<String, Cook> cooks = new ConcurrentHashMap<>();
	

	public Restaurant(boolean random,int clients, int cooks, int waiters, int tables) {
		
		newJade();
		newRestaurant(clients, cooks, waiters, tables);
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
	
	private void generateCooks(int cook_amount) {
		Dish dish = null;
		
		System.out.println("Amount of cooks available: "+cook_amount+"\n With the following specializations:");
		
		for(int i = 0; i < cook_amount; i++) { //CREATES #COOKS = COOK_AMOUNT
				
					switch(i%4) {  //"HIRES" COOKS IN A NORMAL DISTRIBUTION, TO ENSURE WE CAN HAVE AS MANY OPTIONS AS POSSIBLE
					case 0: //NORMAL
						dish = new Dish(false, false, false);
						System.out.println("Normal dishes.");
					break;
					case 1: //ALLERGY
						dish = new Dish(true, false, false);
						System.out.println("Dishes for those with allergies.");
					break;
					case 2: //VEGETARIAN
						dish = new Dish(false, true, false);
						System.out.println("Vegetarian dishes.");
					break;
					case 3: //KID
						dish = new Dish(false, false, true);
						System.out.println("Kid meals.");
					break;	
					
					
			} 
			Cook cook = new Cook(dish,"");
			newAgent("cook_"+Integer.toString(i),cook) ;
			cooks.put("cook_"+Integer.toString(i), cook);
		}
		System.out.println("\n");
		
	}
	
	private void generateWaiters(int waiter_amount) {
		
		System.out.println("Amount of waiters available: "+waiter_amount+"\n");
		for(int i = 0; i < waiter_amount; i++) {
			Waiter waiter =  new Waiter(tables);
			newAgent("waiter_"+Integer.toString(i),waiter);
			waiters.put("waiter_"+Integer.toString(i),waiter);
		}
		
	}
	
	private void generateTables(int table_amount) {
		Random rand = new Random();
		int table_size = 0;
		boolean smoker_table = false;
		tables = new ArrayList<Table>();

		for(int i = 0; i < table_amount; i++) {
			table_size = rand.nextInt(10)+1;
			smoker_table = rand.nextBoolean();
			tables.add(new Table(table_size,smoker_table));
			String smokers = (smoker_table ? "in the smokers section" : "in the non-smokers section");
			System.out.println("Table for "+table_size+" people available "+smokers);
		}

		System.out.println("\n");
		
	}
	public void newRestaurant(int client_amount, int cook_amount, int waiter_amount, int table_amount) {
		
				
		generateTables(table_amount);
		
		generateCooks(cook_amount);
		
		generateClients(client_amount);
		
		generateWaiters(waiter_amount);
		
		newAgent("receptionist", new Receptionist(waiters, cooks, clients));

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