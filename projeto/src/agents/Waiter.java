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

import behaviors.WaiterListenFoodRequest;
import behaviors.WaiterListenTableRequest;
import behaviors.WaiterListen;
import extras.Table;

public class Waiter extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DFAgentDescription dfad;
	private ArrayList<Table> tables;
	private boolean busy;

	//CONSTRUCTOR
    public Waiter(ArrayList<Table> tables) {
    	this.busy = false;
    	this.setTables(tables);
    }

    //GETS
	public ArrayList<Table> getTables() {
		return tables;
	}
	public boolean getBusy() { return this.busy; }

	//SETS
	public void setBusy(boolean newState) { this.busy = newState;}
	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}


    public void setup(){

        System.out.println("waiter");
        
        yellowPagesRegister();

        CyclicBehaviour waiter_listen = new WaiterListen(this);
        
    	//SequentialBehaviour loop = new SequentialBehaviour();
    	//add behaviors
    	//loop.addSubBehaviour(new WaiterListenTableRequest(this));
		//loop.addSubBehaviour(new WaiterListenFoodRequest(this));
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
