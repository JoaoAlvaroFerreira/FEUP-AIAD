package agents;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;
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
import restaurant.Restaurant;





public class ClientGroup extends Agent {
    
	private static final long serialVersionUID = 1L;
	private ArrayList<Client> clients;
	private DFAgentDescription dfad;
	private String waiter;
	private long startTime, endTime, timeWaiting, satisfaction;
	Restaurant restaurant;

	//CONSTRUCTOR
    public ClientGroup(ArrayList<Client> clients) {
    	startTime = System.nanoTime();
    
        this.setClients(clients);
        this.waiter = null;
    }

    public ClientGroup(int veg_clients, int normal_clients, int kid_clients, int allergic_clients, boolean smoker) {
    	startTime = System.nanoTime();
        clients = new ArrayList<Client>();
    	for(int i = 0; i < normal_clients; i++ ) {
			clients.add(new Client(false,false,false,smoker));
		}
		for(int i = 0; i < veg_clients; i++ ) {
			clients.add(new Client(false,true,false,smoker));
		}
		for(int i = 0; i < allergic_clients; i++ ) {
			clients.add(new Client(true,false,false,smoker));
		}
		for(int i = 0; i < kid_clients; i++ ) {
			clients.add(new Client(false,false,true,false));
		}
		this.setClients(clients);
		this.waiter = null;
    }
    
    //GET
	public ArrayList<extras.Client> getClients() { return clients; }

    public String getWaiter() {
        return waiter;
    }

    public void setRestaurant(Restaurant rest) {
    	restaurant = rest;
    }
    //SET
	public void setClients(ArrayList<extras.Client> clients) { this.clients = clients; }

	public void sitDown(String waiter) { this.waiter = waiter; restaurant.getGUI().tableContent(); }

    public void setup(){

    
        yellowPagesRegister();
        
    	SequentialBehaviour loop = new SequentialBehaviour();
    	//add behaviors
		loop.addSubBehaviour(new ClientsRequestTable(this));
		loop.addSubBehaviour(new ClientRequestFood(this));
        loop.addSubBehaviour(new ClientsEat(this));
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
    
    public int hasChildren() {
    	int kids = 0;
    	

    	for(int i = 0; i < this.clients.size(); i++)
    	{
    		if(this.clients.get(i).isChild())
    			kids++;
    		
    	}
    	
    	return kids;
    }
    
    public void leaveRejected() {
    	satisfaction = 0;
    	System.out.println(getLocalName() + " satisfaction was: "+ satisfaction);
    	restaurant.updateUserRating(0);
    }
    public void leave() {
    	Random rand = new Random();
    	endTime  = System.nanoTime();
    	timeWaiting = endTime - startTime;
    	satisfaction = 10 - TimeUnit.SECONDS.convert(timeWaiting, TimeUnit.NANOSECONDS)/(clients.size()*2) - hasChildren() - rand.nextInt(3);
    	
    	if(satisfaction < 0) satisfaction = 0;
    	
    	if(satisfaction > 10) satisfaction = 10;
    	
    	System.out.println(getLocalName() + " satisfaction was: "+ satisfaction);
    	writeLog1();
    	//writeLog2();
    	restaurant.updateUserRating((int) satisfaction);
   
    }
    
    public long getSatisfaction() {
    	return satisfaction;
    }
    
    public Restaurant getRestaurant() {
    	return restaurant;
    }
    
    public void writeLog1() {
    	
    	File file1 = new File("clients_log_1.csv");
        try
        {
        
        	FileWriter fr = new FileWriter(file1, true);
        	BufferedWriter br = new BufferedWriter(fr);
        

        
        br.write(this.getLocalName());
        br.write(",");
        br.write(String.valueOf(this.satisfaction));
        br.write(",");
        br.write(String.valueOf(this.getClients().size()));
        br.write(",");
        br.write(String.valueOf(this.restaurant.getReceptionist().getWaiters().size()));
        br.write(",");
        br.write(String.valueOf(this.restaurant.getReceptionist().getCooks().size()));
        br.write("\n");
    	br.close();
    	fr.close();   

       
      
        }
        catch (IOException ioe)
        {
        System.out.println(ioe);        
        }
    
    }
    
 public void writeLog2() {
    	
    	
    	File file2 = new File("clients_log_2.csv");
        try
        {
        
        	FileWriter fr = new FileWriter(file2, true);
        	BufferedWriter br = new BufferedWriter(fr);
        

        
        br.write(this.getLocalName());
        br.write(",");
        br.write((this.satisfaction > 6) ? "1" : "0");
        br.write(",");
        br.write(String.valueOf(this.getClients().size()));
        br.write(",");
        br.write((this.smokersTable()) ? "1" : "0");
        br.write("\n");
    	br.close();
    	fr.close();   

       
      
        }
        catch (IOException ioe)
        {
        System.out.println(ioe);        
        }
    
    }
    
   
}
