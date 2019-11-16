package agents;

import behaviors.ReceptionistChoosesAvailableWorker;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Receptionist extends Agent {

    private static final long serialVersionUID = 1L;
    private DFAgentDescription dfad;
    private ConcurrentHashMap<String, Waiter> waitersState;
    private ConcurrentHashMap<String, Cook> cooksState;
    private ArrayList<ClientGroup> allClients;
    private ArrayList<String> waitingClients;

    //CONSTRUCTOR
    public Receptionist(ConcurrentHashMap<String, Waiter> waiters, ConcurrentHashMap<String, Cook> cooks, ArrayList<ClientGroup> allClients) {

        this.waitersState = waiters;
        this.cooksState = cooks;
        this.allClients = allClients;
        this.waitingClients = new ArrayList<>();
    }

    //GETS
    public ConcurrentHashMap<String, Waiter> getWaitersState() { return this.waitersState; }
    public ConcurrentHashMap<String, Cook> getCooksState() { return this.cooksState; }
    public ArrayList<String> getWaitingClients() { return this.waitingClients; }
    public ArrayList<ClientGroup> getAllClients() { return this.allClients; }

    //SETS
    public void setWaitersState(ConcurrentHashMap<String, Waiter> newWaiters) { this.waitersState = newWaiters;}
    public void setCooksState(ConcurrentHashMap<String, Cook> newCooks) { this.cooksState = newCooks;}
    public void setWaitingClients(ArrayList<String> newWaitingClients) { this.waitingClients = newWaitingClients; }
    public void setClients(ArrayList<ClientGroup> newClients) { this.allClients = newClients; }

    public void setup(){

        System.out.println("recepcionist");

        yellowPagesRegister();

        SequentialBehaviour loop = new SequentialBehaviour();
        //add behaviors
        loop.addSubBehaviour(new ReceptionistChoosesAvailableWorker(this));
        addBehaviour(loop);

    }

    private void yellowPagesRegister() {
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Receptionist");
        sd.setName(getLocalName());

        this.dfad = new DFAgentDescription();
        dfad.setName(getAID());
        dfad.addServices(sd);
        try {
            DFService.register(this, this.dfad);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

    }
}
