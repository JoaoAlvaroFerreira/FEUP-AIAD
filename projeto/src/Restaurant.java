
import java.util.ArrayList;

import agents.ClientGroup;
import extras.Client;
import extras.Table;
import jade.Boot;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;

public class Restaurant {

	ArrayList<Table> mesas;
	private static ContainerController mainContainer;
	private static Runtime run;
	private static Profile profile;
	
	
	public static void main(){
		
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
		
		//create her stuff to make agents, depending on our needs
	/*	ArrayList<Client> clients1 = new ArrayList<Client>();
		clients1.add(new Client());
		new ClientGroup(clients1);*/
	}
	

	public static void newAgent(String agentID, String agentName) {

		Object[] a = null;
        try {
        	
        
        	
			AgentController agentController = mainContainer.createNewAgent(agentID, "agents." + agentName,a);
            agentController.start();
        } catch (jade.wrapper.StaleProxyException e) {
            System.err.println("Error launching agent...");
        }
	}
}
