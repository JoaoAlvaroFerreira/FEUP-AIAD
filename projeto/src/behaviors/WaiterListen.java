package behaviors;

import agents.Waiter;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;

public class WaiterListen extends SimpleBehaviour {

    private Waiter waiter;
    private boolean finished = false;

    public WaiterListen(Waiter waiter) {
        this.waiter = waiter;
    }

    public void action(){

        ACLMessage msg = this.waiter.blockingReceive();

        try {
            ArrayList<String> message = (ArrayList) msg.getContentObject();
            System.out.println("PANICOOOO: " + message);
        } catch (UnreadableException e) {
            e.printStackTrace();
        }



        if (msg != null){

            if (msg.getSender().getLocalName().substring(0, 12).equals("client_group")) {
                messageFromClient(msg);
            }





        }


    }

    public boolean done() {
        return this.finished;
    }

    public void messageFromClient(ACLMessage message){

            System.out.println("message from client - 1" + message);

        ArrayList<String> msg = new ArrayList<String>();
        System.out.println("message from client - 2");

        try {
            System.out.println("message from client - 3");

            msg  = (ArrayList) message.getContentObject();


            System.out.println("message from client - 4");

        } catch (UnreadableException e) {
            System.out.println("message from client - 5");

            e.printStackTrace();
        }

        System.out.println("message from client - 6");
        System.out.println("message from client - 7 " + msg);

        String messageType = msg.get(0);
        System.out.println("message from client - 8");

        System.out.println("messageType: " + messageType);

        switch(messageType){
            case "REQUEST_TABLE":
                System.out.println("entered request table case");
                request_table(message);
                break;
            case "REQUEST_CHECK":
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
            System.out.println("Available tables: "+this.waiter.getTables().size());
            for(int i = 0; i < this.waiter.getTables().size(); i++) {

                if(this.waiter.getTables().get(i).getSeats() >= client_number &&
                        this.waiter.getTables().get(i).getEmpty() &&
                        this.waiter.getTables().get(i).isSmokers()==smoking) {
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

            System.out.println(waiter.getLocalName() + " SENT:" + msgToClient.getContent() + " TO: " + dest.getLocalName());
            this.waiter.send(msgToClient);

        }
        catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}
