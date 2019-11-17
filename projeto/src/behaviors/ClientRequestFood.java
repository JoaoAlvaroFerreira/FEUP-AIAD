package behaviors;

import agents.ClientGroup;
import extras.Client;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.ArrayList;

public class ClientRequestFood extends SimpleBehaviour {

    private ClientGroup clients;
    private boolean finished = false;
    private static final long serialVersionUID = 1L;

    public ClientRequestFood(ClientGroup clients) {
        this.clients = clients;
    }

    public void action() {

        System.out.println("Action request food");

        ///PARTE 1
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

        ArrayList<String> query = new ArrayList();
        query.add("REQUEST_FOOD");

        int normalDish = 0, allergyDish = 0, veggieDish = 0, childDish = 0;

        for(int i = 0; i < this.clients.getClients().size(); i++){

            Client client = this.clients.getClients().get(i);

            if(client.hasAllergy())
                allergyDish++;

            else if(client.isChild())
                childDish++;

            else if(client.isVegetarian())
                veggieDish++;

            else normalDish++;
        }

        query.add(allergyDish + "");
        query.add(veggieDish + "");
        query.add(childDish + "");
        query.add(normalDish + "");

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
            result = DFService.search(this.clients, dfd);
            boolean found = false;

            for (int j = 0; j < result.length; j++) {

                AID dest = result[j].getName();

                if(dest != null && dest.getLocalName().equals(this.clients.getWaiter())){
                    msg.addReceiver(dest);
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        this.clients.send(msg);
        this.finished = true;


    }


    @Override
    public boolean done() {
        return this.finished;
    }
}