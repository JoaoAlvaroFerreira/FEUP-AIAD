
import java.util.ArrayList;

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
	

	public static void newAgent(String agentID, String agentName) {

		Object[] a = null;
        try {
        	
        	run = Runtime.instance();
        	profile = new ProfileImpl();
        	mainContainer = run.createMainContainer(profile);
        	
			AgentController agentController = mainContainer.createNewAgent(agentID, "agents." + agentName,a);
            agentController.start();
        } catch (jade.wrapper.StaleProxyException e) {
            System.err.println("Error launching agent...");
        }
	}
}
