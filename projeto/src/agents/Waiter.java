package agents;

import behaviors.WaiterListen;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import restaurant.Restaurant;

import java.util.ArrayList;

import behaviors.WaiterListen;
import extras.Table;

public class Waiter extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DFAgentDescription dfad;
	private String client_group_id;

	//CONSTRUCTOR
    public Waiter() {
    	this.client_group_id = null;
    }

    //GETS
	public boolean getBusy() {

    	if(client_group_id == null)
            return false;

    	else
            return true;


	}

	//SETS
	public void setBusy(String client_group_id) { this.client_group_id = client_group_id;}


    public void setup(){

        System.out.println("waiter");
        
        yellowPagesRegister();

        CyclicBehaviour waiter_listen = new WaiterListen(this);
        
    	//SequentialBehaviour loop = new SequentialBehaviour();
    	//add behaviors
		//loop.addSubBehaviour(new WaiterListen(this));
    	addBehaviour(waiter_listen);
      
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
