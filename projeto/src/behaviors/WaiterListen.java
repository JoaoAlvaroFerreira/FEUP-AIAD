package behaviors;

import agents.Waiter;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

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
            case "REQUEST_CHECK":
                request_check(message);
                break;
            case "REQUEST_FOOD":
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

            //ASSIGN TABLE IF AVAILABLE
            boolean table_found = false;
            System.out.println("Available tables: " + this.waiter.getTables().size());

            for(int i = 0; i < this.waiter.getTables().size(); i++) {

                if(this.waiter.getTables().get(i).getSeats() >= client_number && this.waiter.getTables().get(i).getEmpty() &&
                        this.waiter.getTables().get(i).isSmokers()== smoking) {
                    System.out.println("Found table");
                    this.waiter.getTables().get(i).assignClients(msg.getSender().getLocalName());

                    msgToClient.setContent("TABLE_FOUND");
                    table_found = true;

                }

                if(table_found)
                    break;
            }

            if(!table_found) {
                msgToClient.setContent("NO_TABLE_FOUND");
            }

            System.out.println(waiter.getLocalName() + " SENT " + msgToClient.getContent() + " TO " + dest.getLocalName());
            this.waiter.send(msgToClient);

        }
        catch (UnreadableException e) {
            e.printStackTrace();
        }
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