package restaurant;
import java.util.ArrayList;
import java.util.Random;

import agents.ClientGroup;
import agents.Waiter;
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
	
	
	public static void main(String[] args){
		

		
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
		for(int i = 0; i < tables2; i++) {
			tables.add(new Table(2, rand.nextBoolean()));
		}
		System.out.println(tables2 + " tables for 2 created.");
		for(int j = 0; j < tables4; j++) {
			tables.add(new Table(4, rand.nextBoolean()));
		}
		System.out.println(tables4 + " tables for 4 created.");
		
		//create her stuff to make agents, depending on our needs
		
		//THIS MAKES A CLIENT GROUP OF TWO MEAT-EATING NON-ALLERGIC ADULTS, ONE SMOKER, EVENTUALLY MAKE IT SERIALIZED
		ArrayList<Client> clients1 = new ArrayList<Client>();
		clients1.add(new Client(false,false,false,false, true));
		clients1.add(new Client(false,false,false,false, false));
		ClientGroup clientgroup1 = new ClientGroup(clients1);
		newAgent("client_group_01", clientgroup1);
		
		//THIS MAKES A WAITER, EVENTUALLY MAKE IT SERIALIZED
		newAgent("waiter_01", new Waiter(tables));
		
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
