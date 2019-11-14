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

import java.util.concurrent.TimeUnit;

import agents.ClientGroup;

public class ClientsEat extends SimpleBehaviour  {

    private boolean finished = false;
    private ClientGroup client;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ClientsEat(ClientGroup client) {
        this.client = client;
    }

    @Override
    public void action() {

        System.out.println("Action eat"); //will sleep for as many seconds as there are members in the group

        int timeToEat = client.getClients().size();

        for(int i = 0; i < timeToEat; i++){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("waited another second to eat");
        }
        /*try {
            TimeUnit.SECONDS.sleep(timeToEat);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        this.finished = true;



    }


    @Override
    public boolean done() {

        return this.finished;
    }

}
