package agents;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;

import extras.Client;

public class ClientGroup extends Agent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<extras.Client> clients;
	private DFAgentDescription dfad;

    
 /*   public ClientGroup(ArrayList<Client> clients) {
    	this.clients = clients;
    }
    
    */
    public void setup(){
        System.out.println("client group");
        
        
        yellowPagesRegister();
        
    	SequentialBehaviour loop = new SequentialBehaviour();
    	//add behaviors
		//loop.addSubBehaviour();
    	addBehaviour(loop);
    	
      
    }

    private void yellowPagesRegister() {
    	ServiceDescription sd = new ServiceDescription();
		//sd.setType();
    	sd.setType("Client");
		sd.setName(getLocalName());

		this.dfad = new DFAgentDescription();
		dfad.setName(getAID());
		dfad.addServices(sd);
		try {
			DFService.register(this, this.dfad);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
	}


}
