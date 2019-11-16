package behaviors;

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
                ArrayList<String> message = (ArrayList) msg.getContentObject();
                type = message.get(0);
            } catch (UnreadableException e) {
                e.printStackTrace();
            }

            switch (type){
                case "REQUEST_TABLE":
                    request_table(msg);
                    break;
                case "REQUEST_COOK":
                    break;
                case "AVAILABLE_COOK":
                    break;
                case "SCORE_AVAILABLE_TABLE":
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
        System.out.println("Available tables: " + this.receptionist.getTables().size());

        Table table = new Table(0, false);

        for(int i = 0; i < this.receptionist.getTables().size(); i++) {

            if(this.receptionist.getTables().get(i).getSeats() >= client_number && this.receptionist.getTables().get(i).getEmpty() &&
                    this.receptionist.getTables().get(i).isSmokers()== smoking) {

                System.out.println("Found table");

                table = this.receptionist.getTables().get(i);
                table_available = true;
                break;

            }
        }

        if(!table_available) {
            this.receptionist.getwaitingAvailableWaiterTable().add(msg);
            return;
        }

        //ASSIGN WAITER IF AVAILABLE
        Waiter waiter = new Waiter();

        for(int i = 0; i < this.receptionist.getWaiters().size(); i++){
            System.out.println("hello");

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
        query.add(waiter.getAID() + "");


        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Client");
        dfd.addServices(sd);

        try {
            msg.setContentObject(query);
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
                    msg.addReceiver(dest);
                }
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        this.receptionist.send(newMsg);
        System.out.println(msg.getSender().getLocalName() + " is assigned to table and " + waiter.getLocalName());

    }
}
