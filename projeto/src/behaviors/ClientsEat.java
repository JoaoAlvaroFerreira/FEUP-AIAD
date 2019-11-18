package behaviors;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShowGui;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import agents.ClientGroup;
import agents.Receptionist;
import extras.Table;

public class ClientsEat extends SimpleBehaviour  {

    private boolean finished = false;
    private ClientGroup client;
    private static final long serialVersionUID = 1L;

    public ClientsEat(ClientGroup client) {
        this.client = client;
    }

    @Override
    public void action() {

        ACLMessage msg = this.client.blockingReceive();

        System.out.println(this.client.getLocalName()+ " has received their food and are now eating it."); //will sleep for as many seconds as there are members in the group

      
       
            try {
                TimeUnit.SECONDS.sleep(client.getClients().size()*2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        
        System.out.println(this.client.getLocalName()+ " has finished eating."); //will sleep for as many seconds as there are members in the group

            
    
        if (msg != null) {
       
        	 if (msg.getSender().getLocalName().substring(0, 6).equals("recept")) { 
        		//O receptionist também dá a conta aos clientes pelos vistos, não ter isto causa um bug onde alguns clientes não saem do restaurante depois de comer
        		 ACLMessage newMsg = new ACLMessage(ACLMessage.INFORM);
        		  ArrayList<String> content = new ArrayList<>();
                  content.add("AVAILABLE_TABLE");
                  
                  try {
                      newMsg.setContentObject(content);
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
                  System.out.println(this.client.getLocalName() + " is leaving");
                  
                  this.client.leave();
                  
                 bad_case(); 
        	 }
            if (msg.getSender().getLocalName().substring(0, 6).equals("waiter")) {

                ACLMessage newMsg = new ACLMessage(ACLMessage.INFORM);
                DFAgentDescription dfd = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Receptionist");
                dfd.addServices(sd);

                ArrayList<String> content = new ArrayList<>();
                content.add("AVAILABLE_TABLE");
               
                try {
                    newMsg.setContentObject(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                DFAgentDescription[] result = new DFAgentDescription[0];

                try {
                    result = DFService.search(this.client, dfd);
                   
                    for (int j = 0; j < result.length; j++) {

                        AID dest = result[j].getName();

                        if(dest != null){
                            newMsg.addReceiver(dest);
                        }
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }

                System.out.println(this.client.getLocalName() + " is leaving");
           
                this.client.leave();
                this.client.send(newMsg);
            }
        }

        this.finished = true;

    }


    private void bad_case() {
    	
    	 Table table = new Table(0, false);
    	 
    	Receptionist recep = this.client.getRestaurant().getReceptionist();
    	
    	for(int i = 0; i < recep.getTables().size(); i++){

            table = recep.getTables().get(i);

            if(!table.getEmpty()) {
            if(table.getClientID().equals(this.client.getLocalName())){
                System.out.println("Table of " + table.getSeats() + " is available now.\n");
                table.setClientID(null);
                recep.getRestaurant().getGUI().tableContent();
                break;
            }
            }
        }

        int size = table.getSeats();
        String smoker = "SMOKE";

        if(!table.isSmokers()){
            smoker = "NO_SMOKE";
        }

        
        int waiting_list_size = recep.getwaitingAvailableWaiterTable().size();

        recep.getRestaurant().getGUI().tableContent();
   
        if(waiting_list_size > 0){

            ACLMessage curr;
            ArrayList<String> curr_content;

            for(int i = 0; i < waiting_list_size; i++){
                curr = recep.getwaitingAvailableWaiterTable().get(i);
                try {
                    curr_content = (ArrayList<String>) curr.getContentObject();
                if(curr_content.get(0).equals("REQUEST_TABLE")){

                    if(size >= Integer.parseInt(curr_content.get(1)) && smoker.equals(curr_content.get(2))){
                        recep.getwaitingAvailableWaiterTable().remove(i);
                       // request_table(curr);
                        break;
                    }
                    
                } else
                    System.out.println("ERROR - Message kept on waiting table waiter wrong");


                } catch (UnreadableException e) {
                    e.printStackTrace();
                }



            }

        }
	}

	@Override
    public boolean done() {

        return this.finished;
    }

}
