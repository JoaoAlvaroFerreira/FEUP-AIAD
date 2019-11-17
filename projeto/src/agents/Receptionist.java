package agents;

import behaviors.ReceptionistListen;
import extras.Table;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class Receptionist extends Agent {

    private static final long serialVersionUID = 1L;
    private DFAgentDescription dfad;
    private ArrayList<Waiter> waiters;
    private ArrayList<Cook> cooks;
    private ArrayList<Table> tables;
    private ArrayList<ACLMessage> waitingAvailableWaiterTable;
    private ArrayList<ACLMessage> waitingAvailableCook;

    //CONSTRUCTOR
    public Receptionist(ArrayList<Waiter> waiters, ArrayList<Cook> cooks, ArrayList<Table> tables) {

        this.waiters = waiters;
        this.cooks = cooks;
        this.waitingAvailableWaiterTable = new ArrayList<>();
        this.tables = tables;
    }

    //GETS
    public ArrayList<Waiter> getWaiters() { return this.waiters; }
    public ArrayList<Cook> getCooks() { return this.cooks; }
    public ArrayList<ACLMessage> getwaitingAvailableWaiterTable() { return this.waitingAvailableWaiterTable; }
    public ArrayList<ACLMessage> getWaitingAvailableCook() { return this.waitingAvailableCook; }
    public ArrayList<Table> getTables() {
        return tables;
    }


    //SETS
    public void setWaiters(ArrayList<Waiter> newWaiters) { this.waiters = newWaiters;}
    public void setCooks(ArrayList<Cook> newCooks) { this.cooks = newCooks;}
    public void setwaitingAvailableWaiterTable(ArrayList<ACLMessage> waitingAvailableWaiterTable) { this.waitingAvailableWaiterTable = waitingAvailableWaiterTable; }
    public void setWaitingAvailableCook(ArrayList<ACLMessage> newWaitingCooks) { this.waitingAvailableCook = newWaitingCooks; }
    public void setTables(ArrayList<Table> tables) {
        this.tables = tables;
    }


    public void setup(){

        System.out.println("recepcionist");

        yellowPagesRegister();

        CyclicBehaviour receptionist_listen = new ReceptionistListen(this);
        addBehaviour(receptionist_listen);


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
