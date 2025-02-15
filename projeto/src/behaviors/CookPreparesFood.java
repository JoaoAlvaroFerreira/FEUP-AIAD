package behaviors;

import agents.Cook;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static sun.misc.Version.print;

public class CookPreparesFood extends CyclicBehaviour {

    /**
     *
     */
    private Cook cook;
    private static final long serialVersionUID = 1L;

    public CookPreparesFood(Cook cook) { this.cook = cook;}

    @Override
    public void action() {

        ACLMessage msg = this.cook.blockingReceive();

        if (msg != null) {

            if (msg.getSender().getLocalName().equals("receptionist")) {

                try {

                    cook.setBusy(true);

                    ArrayList<String> message = (ArrayList) msg.getContentObject();

                    if(!message.get(0).equals("REQUEST_FOOD")){
                        System.out.println("ERROR - Cook request food error");
                        return;
                    }

                    String waiterName = message.get(5);
                    int total = Integer.parseInt(message.get(6));
                    ACLMessage newMsg = new ACLMessage(ACLMessage.INFORM);
                    DFAgentDescription dfd = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("Waiter");
                    sd.setName(waiterName);
                    dfd.addServices(sd);

                    ArrayList<String> content = new ArrayList<>();
                    content.add("FOOD_READY");
                    content.add(this.cook.getSpecialization());

                    try {
                        newMsg.setContentObject(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    DFAgentDescription[] result = new DFAgentDescription[0];

                    try {
                        result = DFService.search(this.cook, dfd);
                        boolean found = false;

                        for (int j = 0; j < result.length; j++) {

                            AID dest = result[j].getName();

                            if(dest != null && dest.getLocalName().equals(waiterName)){
                                newMsg.addReceiver(dest);
                            }
                        }
                    } catch (FIPAException e) {
                        e.printStackTrace();
                    }
                    
                    

                   
                        try {
                            TimeUnit.SECONDS.sleep(total+2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                    System.out.println(this.cook.getLocalName() + " finished preparing the food for "+ waiterName + " to deliver.");

                    this.cook.send(newMsg);

                }

                catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }

        cook.setBusy(false);
        ACLMessage msgToReceptionist = new ACLMessage(ACLMessage.INFORM);
        ArrayList<String> content = new ArrayList<>();
        content.add("AVAILABLE_COOK");
        content.add(cook.getSpecialization());

        try {
            msgToReceptionist.setContentObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        msgToReceptionist.addReceiver(msg.getSender());

        this.cook.send(msgToReceptionist);
    }

}

