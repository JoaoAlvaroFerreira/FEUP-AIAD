package behaviors;

import agents.Waiter;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.ArrayList;


public class WaiterListen extends CyclicBehaviour {

    private Waiter waiter;

    public WaiterListen(Waiter waiter) {
        this.waiter = waiter;
    }

    public void action(){

        ACLMessage msg = this.waiter.blockingReceive();

        String type = null;

        try {
            ArrayList<String> message = (ArrayList) msg.getContentObject();
            type = message.get(0);

        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        switch (type){
            case "REQUEST_FOOD":
                request_food(msg);
                break;
            case "FOOD_READY":
                food_ready(msg);
                break;
            default:
                break;
        }
    }

    public void request_food(ACLMessage msg){

        ArrayList<String> newMsg = null;
        ACLMessage newMessage = new ACLMessage(ACLMessage.REQUEST);

        try {
            newMsg = (ArrayList<String>) msg.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        newMsg.set(0, "REQUEST_COOK");

        try {
            newMessage.setContentObject(newMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Receptionist");
        dfd.addServices(sd);


        DFAgentDescription[] result = new DFAgentDescription[0];
        try {
            result = DFService.search(this.waiter, dfd);

            for (int j = 0; j < result.length; j++) {

                AID dest = result[j].getName();
                if(dest != null){
                    newMessage.addReceiver(dest);
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        this.waiter.send(newMessage);
    }

    public void food_ready(ACLMessage message) {

        if (message != null) {

            try {
                    ArrayList<String> msgContent = (ArrayList) message.getContentObject();

                    if(!msgContent.get(0).equals("FOOD_READY")){
                        System.out.println("ERROR - Cook request food error");
                        return;
                    }

                    ACLMessage newMsg = new ACLMessage(ACLMessage.INFORM);
                    DFAgentDescription dfd = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("Client");
                    dfd.addServices(sd);


                    DFAgentDescription[] result = new DFAgentDescription[0];

                    try {
                        result = DFService.search(this.waiter, dfd);

                        for (int j = 0; j < result.length; j++) {

                            AID dest = result[j].getName();

                            if(dest != null && dest.getLocalName().equals(this.waiter.getClientID())){
                                newMsg.addReceiver(dest);
                            }
                        }
                        newMsg.setContentObject(msgContent);

                    } catch (FIPAException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    this.waiter.send(newMsg);
                    this.waiter.setBusy(null);
                }

                catch (UnreadableException e) {
                    e.printStackTrace();
                }
        }
    }
}