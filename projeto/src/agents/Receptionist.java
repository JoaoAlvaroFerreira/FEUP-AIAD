package agents;

import behaviors.ReceptionistChoosesAvailableWorker;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Receptionist extends Agent {

    private static final long serialVersionUID = 1L;
    private DFAgentDescription dfad;
    private ConcurrentHashMap<String, Waiter> waiters;
    private ConcurrentHashMap<String, Cook> cooks;
    private ArrayList<ACLMessage> waitingWaiterClients;
    private ArrayList<ACLMessage> waitingTableClients;
    private ArrayList<ACLMessage> waitingAvailableCook;

    //CONSTRUCTOR
    public Receptionist(ConcurrentHashMap<String, Waiter> waiters, ConcurrentHashMap<String, Cook> cooks) {

        this.waiters = waiters;
        this.cooks = cooks;
        this.waitingWaiterClients = new ArrayList<>();
        this.waitingTableClients = new ArrayList<>();

    }

    //GETS
    public ConcurrentHashMap<String, Waiter> getWaiters() { return this.waiters; }
    public ConcurrentHashMap<String, Cook> getCooks() { return this.cooks; }
    public ArrayList<ACLMessage> getWaitingWaiterClients() { return this.waitingWaiterClients; }
    public ArrayList<ACLMessage> getWaitingTableClients() { return this.waitingTableClients; }
    public ArrayList<ACLMessage> getWaitingAvailableCook() { return this.waitingAvailableCook; }

    //SETS
    public void setWaiters(ConcurrentHashMap<String, Waiter> newWaiters) { this.waiters = newWaiters;}
    public void setCooks(ConcurrentHashMap<String, Cook> newCooks) { this.cooks = newCooks;}
    public void setWaitingWaiterClients(ArrayList<ACLMessage> newWaitingWaiterClients) { this.waitingWaiterClients = newWaitingWaiterClients; }
    public void setWaitingTableClients(ArrayList<ACLMessage> newWaitingTableClients) { this.waitingTableClients = newWaitingTableClients; }
    public void setWaitingAvailableCook(ArrayList<ACLMessage> newWaitingCooks) { this.waitingAvailableCook = newWaitingCooks; }

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
