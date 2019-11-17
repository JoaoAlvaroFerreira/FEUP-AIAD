package behaviors;

import agents.Cook;
import agents.Receptionist;
import agents.Waiter;
import extras.Table;
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

public class ReceptionistListen extends CyclicBehaviour {

    private Receptionist receptionist;

    public ReceptionistListen(Receptionist receptionist) {
        this.receptionist = receptionist;
    }

    public void action() {

        ACLMessage msg = this.receptionist.blockingReceive();

        if (msg != null) {

            String type = null;

            try {
                ArrayList<String> message = (ArrayList<String>) msg.getContentObject();
                type = message.get(0);

            } catch (UnreadableException e) {
                e.printStackTrace();
            }

            switch (type){
                case "REQUEST_TABLE":
                    request_table(msg);
                    break;
                case "REQUEST_COOK":
                    request_cook(msg);
                    break;
                case "AVAILABLE_COOK":
                    break;
                case "AVAILABLE_TABLE":
                    //client leaves, table becomes available, gives score
                    available_table(msg);
                    break;
                default:
                    break;
            }
        }
    }

    public void request_table(ACLMessage msg){
        //vê se ha waiter
        //vê se ha mesa
        //se nao houver um dos dois vai pra fila de espera

        ArrayList<String> message = new ArrayList<>();
        boolean waiter_available = false;
        boolean table_available = false;
        boolean clientSpecificsMet = false;

        try {
            message = (ArrayList) msg.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        int client_number = Integer.parseInt(message.get(1));
        boolean smoking = false;
        if(message.get(2).equals("SMOKE"))
            smoking = true;

        //ASSIGN TABLE IF AVAILABLE
        Table table = new Table(0, false);

        for(int i = 0; i < this.receptionist.getTables().size(); i++) {

            if( this.receptionist.getTables().get(i).isSmokers() == smoking && this.receptionist.getTables().get(i).getSeats() >= client_number) {

                clientSpecificsMet = true;

                if(this.receptionist.getTables().get(i).getEmpty()) {

                    System.out.println("Found table");

                    table = this.receptionist.getTables().get(i);
                    table_available = true;
                    break;

                }
            }
        }

        if(!clientSpecificsMet){

            System.out.println("There are no tables for these clients. Please go to another restaurant.");

            ACLMessage deleteClient = new ACLMessage(ACLMessage.INFORM);
            ArrayList<String> content = new ArrayList<>();
            content.add("CLIENT_LEAVE");

            try {
                deleteClient.setContentObject(content);
            } catch (IOException e) {
                e.printStackTrace();
            }

            deleteClient.addReceiver(msg.getSender());
            this.receptionist.send(deleteClient);
            return;
        }

        if(!table_available) {
            this.receptionist.getwaitingAvailableWaiterTable().add(msg);
            return;
        }

        //ASSIGN WAITER IF AVAILABLE
        Waiter waiter = new Waiter();

        for(int i = 0; i < this.receptionist.getWaiters().size(); i++){

            waiter = this.receptionist.getWaiters().get(i);

            if(!waiter.getBusy()){
                waiter_available = true;
                break;
            }
        }

        if(!waiter_available) {
            this.receptionist.getwaitingAvailableWaiterTable().add(msg);
            return;
        }

        if(waiter_available && table_available){
            waiter.setBusy(msg.getSender().getLocalName());
            table.setClientID(msg.getSender().getLocalName());
        }

        ACLMessage newMsg = new ACLMessage(ACLMessage.INFORM);

        ArrayList<String> query = new ArrayList();
        query.add("ASSIGN_TABLE_WAITER");
        query.add(waiter.getLocalName());


        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Client");
        dfd.addServices(sd);

        try {
            newMsg.setContentObject(query);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DFAgentDescription[] result = new DFAgentDescription[0];
        try {
            result = DFService.search(this.receptionist, dfd);
            boolean found = false;

            for (int j = 0; j < result.length; j++) {

                AID dest = result[j].getName();
                if(dest != null){
                    newMsg.addReceiver(dest);
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        this.receptionist.send(newMsg);
        System.out.println(msg.getSender().getLocalName() + " is assigned to table and " + waiter.getLocalName());

    }

    public void request_cook(ACLMessage msg){

        //TODO: adicionar outras estrategias mais inteligentes de alocação

        Cook cook = new Cook(null);
        boolean cook_available = false;

        for(int i = 0; i < this.receptionist.getCooks().size(); i++){

            cook = this.receptionist.getCooks().get(i);

            if(!cook.getBusy()){
                cook_available = true;
                break;
            }
        }

        if(!cook_available) {
            this.receptionist.getWaitingAvailableCook().add(msg);
            return;
        }

        else {

            ArrayList<String> content = new ArrayList<>();

            try {
                content = (ArrayList<String>) msg.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

            content.set(0, "REQUEST_FOOD");
            content.add(msg.getSender().getLocalName());

            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

            try {
                message.setContentObject(content);
            } catch (IOException e) {
                e.printStackTrace();
            }

            message.addReceiver(cook.getAID());
            this.receptionist.send(message);

            System.out.println("Receptionist sent food request to cook");

        }
    }

    public void available_table(ACLMessage msg){

        Table table = new Table(0, false);

        for(int i = 0; i < this.receptionist.getTables().size(); i++){

            table = this.receptionist.getTables().get(i);

            if(table.getClientID().equals(msg.getSender().getLocalName())){
                System.out.println("Table of " + table.getSeats() + " is available now.\n");
                table.setClientID(null);
                break;
            }
        }
    }
}
