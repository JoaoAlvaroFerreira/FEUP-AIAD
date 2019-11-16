package behaviors;

import agents.Cook;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;

import static sun.misc.Version.print;

public class CookPreparesFood extends SimpleBehaviour  {

    /**
     *
     */
    private boolean finished = false;
    private Cook cook;
    private static final long serialVersionUID = 1L;

    public CookPreparesFood(Cook cook) { this.cook = cook;}

    @Override
    public void action() {

        ACLMessage msg = this.cook.blockingReceive();

        if (msg != null) {

            if (msg.getSender().getLocalName().substring(0, 5).equals("waiter")) {

                cook.setBusy(true);

                try {

                    ACLMessage msgToWaiter = new ACLMessage(ACLMessage.INFORM);
                    AID dest = msg.getSender();
                    msgToWaiter.addReceiver(dest);

                    ArrayList<String> message = (ArrayList) msg.getContentObject();
                    boolean allergic = Boolean.parseBoolean(message.get(2));
                    boolean veggie = Boolean.parseBoolean(message.get(3));
                    boolean child = Boolean.parseBoolean(message.get(4));

                    if(allergic){
                        message.set(2,"2");
                    }

                    else if (veggie){
                        message.set(3, "2");
                    }

                    else if (child){
                        message.set(4, "2");
                    }

                    msgToWaiter.setContent("FOOD_READY");
                    System.out.println("SENT " + msgToWaiter.getContent() + " TO " + dest.getLocalName());
                    this.cook.send(msgToWaiter);

                }

                catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        }

        this.finished = true;
        cook.setBusy(false);
    }

    @Override
    public boolean done() {

        return this.finished;
    }

}

