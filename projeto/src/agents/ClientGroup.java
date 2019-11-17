package agents;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import behaviors.ClientRequestFood;
import behaviors.ClientsEat;
import behaviors.ClientsRequestTable;
import extras.Client;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;


public class ClientGroup extends Agent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Client> clients;
	private DFAgentDescription dfad;
	private String waiter;
	private long startTime, endTime, timeWaiting, satisfaction;

	//CONSTRUCTOR
    public ClientGroup(ArrayList<Client> clients) {
    	startTime = System.nanoTime();
    
        this.setClients(clients);
        this.waiter = null;
    }

    //GET
	public ArrayList<extras.Client> getClients() { return clients; }

    public String getWaiter() {
        return waiter;
    }

    //SET
	public void setClients(ArrayList<extras.Client> clients) { this.clients = clients; }

	public void sitDown(String waiter) { this.waiter = waiter; }

    public void setup(){

        yellowPagesRegister();
        
    	SequentialBehaviour loop = new SequentialBehaviour();
    	//add behaviors
		loop.addSubBehaviour(new ClientsRequestTable(this));
		loop.addSubBehaviour(new ClientRequestFood(this));
        loop.addSubBehaviour(new ClientsEat(this));
        //loop.addSubBehaviour(new ClientsRequestCheck(this));
        addBehaviour(loop);
    }

    private void yellowPagesRegister() {

    	ServiceDescription sd = new ServiceDescription();
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
    
    public void leave() {
    	endTime  = System.nanoTime();
    	timeWaiting = endTime - startTime;
    	satisfaction = 10 - TimeUnit.SECONDS.convert(timeWaiting, TimeUnit.NANOSECONDS);
    	System.out.println(getLocalName() + " satisfaction was: "+ satisfaction);
    }
    
}
