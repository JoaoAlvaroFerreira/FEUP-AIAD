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
		

		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);
		try {
			ArrayList<String> query = new ArrayList();
			query.add("Table for ");
			query.add(this.client.getClients().size()+"");
			query.add("please.");
			if(client.smokersTable())
			query.add("For smokers, if possible.");
			
			else query.add("For son-smokers, if possible.");
			
			msg.setContentObject(query);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Waiter");
		dfd.addServices(sd);
		
		try {
			DFAgentDescription[] result = DFService.search(this.client, dfd);
		
			for (int j = 0; j < result.length; j++) {
				AID dest = result[j].getName();
				msg.addReceiver(dest);
				this.client.send(msg);
				System.out.println("SENT:      " + msg.getContentObject() + " TO: " + dest.getLocalName());
			}
		} catch (FIPAException e) {
			e.printStackTrace();
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ACLMessage new_msg = this.client.blockingReceive();
	
		if (new_msg != null) {
			if (new_msg.getSender().getLocalName().substring(0, 6).equals("waiter")) {
				

				String message =new_msg.getContent();
				
				
				if(message.contains("We found a table"))
					client.sitDown();
				else if(message.contains("We did not find a table")) {
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
