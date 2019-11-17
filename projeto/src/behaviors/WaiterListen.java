package behaviors;

import agents.Waiter;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WaiterListen extends CyclicBehaviour {

    private Waiter waiter;
    private boolean finished = false;

    public WaiterListen(Waiter waiter) {
        this.waiter = waiter;
    }

    public void action(){


        ACLMessage msg = this.waiter.blockingReceive();

        try {
            ArrayList<String> message = (ArrayList) msg.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        if (msg != null){

            if (msg.getSender().getLocalName().substring(0, 12).equals("client_group")) {
                messageFromClient(msg);
            }
        }
    }

    public void messageFromClient(ACLMessage message){

        ArrayList<String> msg = new ArrayList<String>();

        try {

            msg  = (ArrayList) message.getContentObject();


        } catch (UnreadableException e) {

            e.printStackTrace();
        }

        String messageType = msg.get(0);

        switch(messageType){
            case "REQUEST_TABLE":
                request_table(message);
                break;
            case "REQUEST_FOOD":
                request_food(message);
                break;
            case "REQUEST_CHECK":
                request_check(message);
                break;

            default:
                break;

        }
    }

    public void request_table(ACLMessage msg){

        System.out.println( waiter.getLocalName() + " RECEIVED RESQUEST_TABLE MESSAGE from " + msg.getSender().getLocalName());

        try {
            ACLMessage msgToClient = new ACLMessage(ACLMessage.INFORM);
            AID dest = msg.getSender();
            msgToClient.addReceiver(dest);
            ArrayList<String> message = (ArrayList) msg.getContentObject();

            int client_number = Integer.parseInt(message.get(1));
            boolean smoking = false;

            if(message.get(2).equals("SMOKE"))
                smoking = true;



            System.out.println(waiter.getLocalName() + " SENT " + msgToClient.getContent() + " TO " + dest.getLocalName());
            this.waiter.send(msgToClient);

        }
        catch (UnreadableException e) {
            e.printStackTrace();
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
        newMsg.add(msg.getSender().getLocalName());

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
            boolean found = false;

            for (int j = 0; j < result.length; j++) {

                AID dest = result[j].getName();
                if(dest != null){
                    newMessage.addReceiver(dest);
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        System.out.println("Request cook sent to receptionist: " + newMsg);

        this.waiter.send(newMessage);


    }


    public void request_check(ACLMessage msg){

        System.out.println("check requested");

        Random rand = new Random();

        int time_to_get_check = rand.nextInt(5);

        try {
            TimeUnit.SECONDS.sleep(time_to_get_check);

            ACLMessage msgToClient = new ACLMessage(ACLMessage.INFORM);
            AID dest = msg.getSender();
            msgToClient.addReceiver(dest);
            ArrayList<String> message = (ArrayList) msg.getContentObject();
            msgToClient.setContent("CHECK_REQUEST_REPLY");

            System.out.println("CHECK_REQUEST_REPLY sent to client");

            System.out.println(waiter.getLocalName() + " SENT " + msgToClient.getContent() + " TO " + dest.getLocalName());
            this.waiter.send(msgToClient);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}