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
import jade.lang.acl.StringACLCodec;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import agents.ClientGroup;

public class ClientsRequestTable extends SimpleBehaviour  {

	private boolean finished = false;
	private ClientGroup client;
	private static final long serialVersionUID = 1L;
	
	public ClientsRequestTable(ClientGroup client) {
		this.client = client;
	}

	@Override
	public void action() {
		

		
		///PARTE 1
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

		ArrayList<String> query = new ArrayList();
		query.add("REQUEST_TABLE");
		query.add(this.client.getClients().size()+"");

		if(client.smokersTable())
			query.add("SMOKE");
		else
			query.add("NO_SMOKE");

        try {
            msg.setContentObject(query);
        } catch (IOException e) {
            e.printStackTrace();
        }

		DFAgentDescription dfd = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Receptionist");
		dfd.addServices(sd);



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

        System.out.println(this.client.getLocalName()+" has requested a table.");

		ACLMessage new_msg = this.client.blockingReceive();

        if (new_msg != null) {

			if (new_msg.getSender().getLocalName().equals("receptionist")) {

				ArrayList<String> message = null;
				try {
					message = (ArrayList<String>) new_msg.getContentObject();
				} catch (UnreadableException e) {
					e.printStackTrace();
				}

				if(message.get(0).equals("ASSIGN_TABLE_WAITER")){
					client.sitDown(message.get(1));
                    System.out.println(client.getLocalName() + " sat down at a table.");
                }

				else if(message.get(0).equals("CLIENT_LEAVE")){

					try {
						DFService.deregister(this.client);
					} catch (FIPAException e) {
						e.printStackTrace();
					}
					client.doDelete();
					System.out.println(this.client.getLocalName() + " has left without being attented.\n");
				}

				else System.out.println("ERROR - Wrong Assign Table Message");
			}
		}

		this.finished = true;
	}


	@Override
	public boolean done() {
		
		return this.finished;
	}
}
