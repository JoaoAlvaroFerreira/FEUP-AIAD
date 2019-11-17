package behaviors;

import agents.ClientGroup;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;

public class ClientsRequestCheck extends SimpleBehaviour  {

	private boolean finished = false;
	private ClientGroup client;
	private static final long serialVersionUID = 1L;

	public ClientsRequestCheck(ClientGroup client) {
		this.client = client;
	}

	@Override
	public void action() {
		
		System.out.println("Action request check");
		
		ACLMessage msg = new ACLMessage(ACLMessage.QUERY_IF);

		ArrayList<String> query = new ArrayList();
		query.add("REQUEST_CHECK");

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
				
				if(message.contains("CHECK_REQUEST_REPLY")) {
					System.out.println(client.getLocalName() + " have left the restaurant");
					
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
