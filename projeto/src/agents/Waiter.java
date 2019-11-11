package agents;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;

import behaviors.WaiterListenFoodRequest;
import behaviors.WaiterListenTableRequest;

public class Waiter extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DFAgentDescription dfad;

    
    public Waiter() {}
    
    public void setup(){
        System.out.println("waiter");
        
        yellowPagesRegister();
        
    	SequentialBehaviour loop = new SequentialBehaviour();
    	//add behaviors
    	loop.addSubBehaviour(new WaiterListenTableRequest(this));
		loop.addSubBehaviour(new WaiterListenFoodRequest(this));
    	addBehaviour(loop);
      
    }

    private void yellowPagesRegister() {
    	ServiceDescription sd = new ServiceDescription();
		//sd.setType();
    	sd.setType("Waiter");
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
