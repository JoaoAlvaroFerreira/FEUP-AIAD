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

        System.out.println("Clients are eating"); //will sleep for as many seconds as there are members in the group

        int timeToEat = client.getClients().size();

        for(int i = 0; i < timeToEat; i++){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Waited another second to eat");
        }


        if (msg != null) {

            if (msg.getSender().getLocalName().substring(0, 6).equals("waiter")) {

                ACLMessage newMsg = new ACLMessage(ACLMessage.INFORM);
                DFAgentDescription dfd = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Receptionist");
                dfd.addServices(sd);

                ArrayList<String> content = new ArrayList<>();
                content.add("LEAVE");

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


    @Override
    public boolean done() {

        return this.finished;
    }

}
