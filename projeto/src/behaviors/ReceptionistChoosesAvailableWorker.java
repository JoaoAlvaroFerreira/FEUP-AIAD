package behaviors;

import agents.ClientGroup;
import agents.Cook;
import agents.Receptionist;
import agents.Waiter;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;

public class ReceptionistChoosesAvailableWorker extends SimpleBehaviour {

    private Receptionist receptionist;

    public ReceptionistChoosesAvailableWorker(Receptionist receptionist) { this.receptionist = receptionist; }

    public void action() {

        ACLMessage msg = this.receptionist.blockingReceive();

        try {
            ArrayList<String> message = (ArrayList) msg.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        if (msg != null) {

            if (msg.getSender().getLocalName().substring(0, 12).equals("client_group")) {
                chooseWaiter(msg);
            }

            else if (msg.getSender().getLocalName().substring(0, 6).equals("waiter")) {
                chooseCook(msg);
            }
        }
    }

    public void chooseWaiter(ACLMessage message) {

    	 boolean foundWaiter = false;
    	 
    	 
    	 
        Waiter availableWaiter = new Waiter(null);
       

        for (int i = 0; i < receptionist.getWaitersState().size(); i++) {

            if (!receptionist.getWaitersState().get("waiter_"+Integer.toString(i)).getBusy()) {

                foundWaiter = true;
                availableWaiter = receptionist.getWaitersState().get("waiter_"+Integer.toString(i));
                break;

            }
        }
        

        if(foundWaiter){

        //SEND MESSAGE TO CLIENT INFORMING WHICH WAITER WILL WAITER THEM
        //hello tou? inglês?
        ACLMessage msgToClient = new ACLMessage(ACLMessage.QUERY_IF);

        ArrayList<String> query = new ArrayList();
        query.add("WAITER_" + availableWaiter.getLocalName() + "_AVAILABLE");
        query.add(availableWaiter.getAID() + "");

        System.out.println(message.getSender() + " will be served by " + availableWaiter.getAID());

        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Client");
        dfd.addServices(sd);

        try {
            msgToClient.setContentObject(query);
        } catch (IOException e) {
            e.printStackTrace();
        }

        AID dest = message.getSender();
        msgToClient.addReceiver(dest);

        this.receptionist.send(msgToClient);

        //SEND CLIENT MESSAGE TO CHOSEN WAITER
        //NOTA : COMO È QUE REMOVO UM RECEIVER?? QUERO REMOVÊ-LO?
        message.addReceiver(availableWaiter.getAID());
        this.receptionist.send(message);
        }

        else {
            //PUT CLIENT IN WAITING LIST
            //TODO
            for(int j = 0; j < receptionist.getAllClients().size(); j++){

                ClientGroup clients = receptionist.getAllClients().get(j);

                if(message.getSender().equals(clients.getAID())){
                    receptionist.getWaitingClients().add(clients.getAID()+ "");
                    break;
                }
            }
        }
    }

    public void chooseCook(ACLMessage message){

        Cook availableCook = new Cook(null, null);
        boolean foundCook = false;

        for(int i = 0; i < receptionist.getWaitersState().size(); i++){

            if(!receptionist.getWaitersState().get(i).getBusy()){

                availableCook = receptionist.getCooksState().get(i);
                foundCook = true;
                break;
            }
        }

        if(foundCook) {

            //SEND MESSAGE TO WAITER INFORMING WHICH COOK IS AVAILABLE
            ACLMessage msgToWaiter = new ACLMessage(ACLMessage.QUERY_IF);

            ArrayList<String> query = new ArrayList();
            query.add("COOK_" + availableCook.getLocalName() + "_AVAILABLE");
            query.add(availableCook.getAID() + "");

            System.out.println(availableCook.getAID() + " is available");

            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("Waiter");
            dfd.addServices(sd);

            try {
                msgToWaiter.setContentObject(query);
            } catch (IOException e) {
                e.printStackTrace();
            }

            AID dest = message.getSender();
            msgToWaiter.addReceiver(dest);

            this.receptionist.send(msgToWaiter);

            //SEND WAITER MESSAGE TO CHOSEN COOK
            message.addReceiver(availableCook.getAID());
            this.receptionist.send(message);
        }

        else {
            //IDK WHAT TO DO, SE ESTÃO TODOS OCUPADOS ...
            //guess i'll die
            //TODO
        }
    }

    @Override
    public boolean done() {
        // TODO Auto-generated method stub
        return false;
    }

}
