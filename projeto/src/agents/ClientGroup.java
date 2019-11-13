package agents;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;

import behaviors.ClientsRequestTable;
import extras.Client;

public class ClientGroup extends Agent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Client> clients;
	private DFAgentDescription dfad;
	private int state; //0 - arrived; 1 - seated; 2 - ordered; 3 - ate and await to give review + pay

	
    
    public ClientGroup(ArrayList<Client> clients) {
    	this.setClients(clients);
    }
    
 
    
    
    public void setup(){
        
        
        
        yellowPagesRegister();
        
    	SequentialBehaviour loop = new SequentialBehaviour();
    	//add behaviors
		loop.addSubBehaviour(new ClientsRequestTable(this));
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

    public boolean smokersTable() {
    	boolean smokers = false;
    	
    	for(int i = 0; i < this.clients.size(); i++)
    	{
    		if(this.clients.get(i).isSmoker())
    			smokers = true;
    		
    	}
    	return smokers;
    }



	public ArrayList<extras.Client> getClients() {
		return clients;
	}




	public void setClients(ArrayList<extras.Client> clients) {
		this.clients = clients;
	}
	
	public void sitDown() {
		state = 1;
	}


}
