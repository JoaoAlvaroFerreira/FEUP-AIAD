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

import agents.ClientGroup;

public class ClientsRequestTable extends SimpleBehaviour  {

	private boolean finished = false;
	private ClientGroup client;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ClientsRequestTable(ClientGroup client) {
		this.client = client;
	}

	@Override
	public void action() {
		
		System.out.println("Action request table");
		
///PARTE 1
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);


		ArrayList<String> query = new ArrayList();
		query.add("REQUEST_TABLE");
		query.add(this.client.getClients().size()+"");
		if(client.smokersTable())
			query.add("SMOKE");
		else
			query.add("NO_SMOKE");

		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Waiter");
		dfd.addServices(sd);

        try {
            msg.setContentObject(query);
        } catch (IOException e) {
            e.printStackTrace();
        }


        DFAgentDescription[] result = new DFAgentDescription[0];
        try {
            result = DFService.search(this.client, dfd);
            boolean found = false;

            for (int j = 0; j < result.length; j++) {

                AID dest = result[j].getName();
                if(dest != null){
                    msg.addReceiver(dest);
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }


			


        this.client.send(msg);

		ACLMessage new_msg = this.client.blockingReceive();
	
		if (new_msg != null) {
			if (new_msg.getSender().getLocalName().substring(0, 6).equals("waiter")) {
				

				String message =new_msg.getContent();
				
				
				if(message.contains("TABLE_FOUND"))
					client.sitDown();
				else if(message.contains("NO_TABLE_FOUND")) {
					//set up waiting list
					System.out.println("Let's wait...");
				}
				else System.out.println("FAILURE");
				
						
								
							
							}
			}

		this.finished = true;

		
	
	}


	@Override
	public boolean done() {
		
		return this.finished;
	}

}
