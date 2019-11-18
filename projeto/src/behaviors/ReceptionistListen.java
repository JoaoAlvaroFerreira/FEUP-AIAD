package behaviors;

import agents.Cook;
import agents.Receptionist;
import agents.Waiter;
import extras.Table;
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
                    available_cook(msg);
                    break;
                case "AVAILABLE_TABLE":
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
        Table table = new Table(Integer.MAX_VALUE, false);


        if(!receptionist.getStrategy()){

            //DUMB TABLE ASSIGN
            for(int i = 0; i < this.receptionist.getTables().size(); i++) {

                if( this.receptionist.getTables().get(i).isSmokers() == smoking && this.receptionist.getTables().get(i).getSeats() >= client_number) {

                    clientSpecificsMet = true;

                    if(this.receptionist.getTables().get(i).getEmpty()) {

                        

                        table = this.receptionist.getTables().get(i);
                        table_available = true;
                        break;
                    }
                }
            }
        }

        else {

            //SMART TABLE ASSIGN
            ArrayList<Table> tables_available = new ArrayList<>();

            for(int i = 0; i < this.receptionist.getTables().size(); i++) {

                if(this.receptionist.getTables().get(i).getSeats() >= client_number && this.receptionist.getTables().get(i).isSmokers() == smoking){

                    clientSpecificsMet = true;

                    if(this.receptionist.getTables().get(i).getEmpty()) {

                        tables_available.add(this.receptionist.getTables().get(i));
                        table_available = true;
                    }
                }
            }

            for(int i = 0; i < tables_available.size(); i++){

                if(tables_available.get(i).getSeats() < table.getSeats()){
                    table = tables_available.get(i);
                }

            }

        }

        if(!clientSpecificsMet){

            System.out.println(msg.getSender().getLocalName()+" specifications we couldn't meet so they were asked to go to another restaurant.");

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
            this.receptionist.getRestaurant().getGUI().tableContent();
            return;
        }

        if(!table_available) {
        	System.out.println( msg.getSender().getLocalName()+ " was asked to wait until a table is free.");
            this.receptionist.getwaitingAvailableWaiterTable().add(msg);
            this.receptionist.getRestaurant().getGUI().tableContent();
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

        ArrayList<String> content = new ArrayList<>();
        Cook cook = new Cook(null);
        boolean cook_available = false;

        try {
            content = (ArrayList) msg.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        int largest = 0;
        int j = 0;
        int total = 0;
        ArrayList<Integer> smallestIndex = new ArrayList<>();


        for(int i = 4; i > 0; i--){

        	total = total + Integer.parseInt(content.get(i));

        	if(largest != 0) {
                if(Integer.parseInt(content.get(i)) >= largest)
                    j = i;
            }

            else {
        	     if(Integer.parseInt(content.get(i)) > largest)
        	         j = i;
            }

            largest = Integer.parseInt(content.get(i));
        }


        String specialization = null;

        switch(j){
            case 1 :
                specialization = "ALLERGY";
                break;
            case 2:
                specialization = "VEGGIE";
                break;
            case 3:
                specialization = "CHILD";
                break;
            case 4:
                specialization = "";
                break;
        }

        System.out.println(j + "SPECIALIZATION " + specialization);

        //SMART COOK ASSIGN
        if(this.receptionist.getStrategy()) {

            for(int i = 0; i < this.receptionist.getCooks().size(); i++){

                cook = this.receptionist.getCooks().get(i);

                if((!cook.getBusy() && cook.getSpecialization().equals(specialization)) || (!cook.getBusy() && cook.getSpecialization().equals(""))){
                    cook_available = true;
                    break;
                }
            }
        }

        //DUMB COOK ASSIGN
        else {

            for(int i = 0; i < this.receptionist.getCooks().size(); i++){

                cook = this.receptionist.getCooks().get(i);

                if(!cook.getBusy()){
                    cook_available = true;
                    break;
                }
            }
        }

        //If specialty not available - choose first cook not busy
        if(!cook_available) {

            for(int i = 0; i < this.receptionist.getCooks().size(); i++){

                cook = this.receptionist.getCooks().get(i);

                if(!cook.getBusy())
                {
                	cook_available = true;
                	break;
                }
            }
        }


        //if all are busy - add to waiting list
        if(!cook_available) {

            content.add(specialization);
            ACLMessage messageToWait = new ACLMessage(ACLMessage.REQUEST);

            try {
                messageToWait.setContentObject(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
       
            this.receptionist.getWaitingAvailableCook().add(messageToWait);
            return;
        }

        else {
        	
            content.set(0, "REQUEST_FOOD");
           
            content.add(msg.getSender().getLocalName());
            content.add(Integer.toString(total));

            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);

            try {
                message.setContentObject(content);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Cook chosen was " + cook.getLocalName() + " specialized in " + cook.getSpecialization()+ " dishes.");
            message.addReceiver(cook.getAID());
            this.receptionist.send(message);

         
        }
    }

    public void available_table(ACLMessage msg){

        Table table = new Table(0, false);
     
        for(int i = 0; i < this.receptionist.getTables().size(); i++){

            table = this.receptionist.getTables().get(i);

            if(!table.getEmpty()) {
            if(table.getClientID().equals(msg.getSender().getLocalName())){
                System.out.println("Table of " + table.getSeats() + " is available now.\n");
                table.setClientID(null);
                this.receptionist.getRestaurant().getGUI().tableContent();
                break;
            }
            }
        }

        int size = table.getSeats();
        String smoker = "SMOKE";

        if(!table.isSmokers()){
            smoker = "NO_SMOKE";
        }

        
        int waiting_list_size = receptionist.getwaitingAvailableWaiterTable().size();

        this.receptionist.getRestaurant().getGUI().tableContent();
   
        if(waiting_list_size > 0){

            ACLMessage curr;
            ArrayList<String> curr_content;

            for(int i = 0; i < waiting_list_size; i++){
                curr = receptionist.getwaitingAvailableWaiterTable().get(i);
                try {
                    curr_content = (ArrayList<String>) curr.getContentObject();
                if(curr_content.get(0).equals("REQUEST_TABLE")){

                    if(size >= Integer.parseInt(curr_content.get(1)) && smoker.equals(curr_content.get(2))){
                    	request_table(curr);
                        receptionist.getwaitingAvailableWaiterTable().remove(i);
                        
                        break;
                    }
                    
                } else
                    System.out.println("ERROR - Message kept on waiting table waiter wrong");


                } catch (UnreadableException e) {
                    e.printStackTrace();
                }



            }

        }

    }

    public void available_cook(ACLMessage msg){

        ArrayList<String> availableMsg = new ArrayList<>();
        ArrayList<String> waitingClient = new ArrayList<>();
        ArrayList<String> waitingClient1 = new ArrayList<>();
        ArrayList<String> waitingClient2 = new ArrayList<>();
        boolean specializationFound = false;

        try {
            availableMsg = (ArrayList<String>) msg.getContentObject();

        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        if(this.receptionist.getWaitingAvailableCook().size() > 0) {

            if(this.receptionist.getWaitingAvailableCook().size() >= 3) {

                for(int i = 0; i < 3; i++){

                    try {
                        waitingClient = (ArrayList<String>) this.receptionist.getWaitingAvailableCook().get(i).getContentObject();
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }

                    if(waitingClient.get(5).equals(availableMsg.get(1))){
                    	
                        request_cook(this.receptionist.getWaitingAvailableCook().get(i));
                  
                        this.receptionist.getWaitingAvailableCook().remove(this.receptionist.getWaitingAvailableCook().get(i));
               
                        specializationFound = true;
                        break;
                    }
                }

                if(!specializationFound) {
                    request_cook(this.receptionist.getWaitingAvailableCook().get(0));
                    this.receptionist.getWaitingAvailableCook().remove(0);
                    return;
                }
            }

            else {
/* COMENTEI ISTO PORQUE ESTAVA A CRASHAR O PROGRAMA EM ALGUNS EDGE CASES (SIG FAULTS)
                try {
                	
                    waitingClient1 = (ArrayList<String>) this.receptionist.getWaitingAvailableCook().get(0).getContentObject();
              
                    waitingClient2 = (ArrayList<String>) this.receptionist.getWaitingAvailableCook().get(1).getContentObject();
              
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }

                if(waitingClient1.equals(availableMsg.get(1))){
                    request_cook(this.receptionist.getWaitingAvailableCook().get(0));
                    this.receptionist.getWaitingAvailableCook().remove(0);
                    return;
                }

                else if(waitingClient2.equals(availableMsg.get(1))){
                    request_cook(this.receptionist.getWaitingAvailableCook().get(1));
                    this.receptionist.getWaitingAvailableCook().remove(1);
                    return;
                }

                else { */
                    request_cook(this.receptionist.getWaitingAvailableCook().get(0));
                    this.receptionist.getWaitingAvailableCook().remove(0);
                    return;
                //}
            }
        }
    }
}
