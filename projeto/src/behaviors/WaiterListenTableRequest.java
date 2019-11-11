package behaviors;

import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.HashMap;

import agents.Waiter;
import agents.ClientGroup;
import jade.core.AID;
import jade.core.Agent;

public class WaiterListenTableRequest extends SimpleBehaviour {

	private Waiter waiter;
	private boolean finished = false;

	public WaiterListenTableRequest(Waiter waiter) {
		this.waiter = waiter;
	}

	public void action() {

		System.out.println("Waiter Listen Table Request");
		ACLMessage msg = this.waiter.blockingReceive();
		System.out.println("Waiter Listen Table Request 2");
		if (msg != null) {
			if (msg.getSender().getLocalName().substring(0, 5).equals("voter")) {
				/*
				try {
					ArrayList<String> message = (ArrayList) msg.getContentObject();
					this.chiefOfStaff.logger.info("RECEIVED:  " + message + " FROM: " + msg.getSender().getLocalName());
					String candidate = message.get(1);
					String belief = message.get(3);
					int value = -1;
					if (message.get(5) != null)
						value = Integer.parseInt(message.get(5));

					//System.out.println("                 - CHIEF OF STAFF: " + this.chiefOfStaff.getLocalName()
						//	+ " LISTENING VOTER CHOICES: " + msg.getSender().getLocalName() + " " + message);

					this.chiefOfStaff.getStateChosenCandidates().add(candidate);

					if (this.chiefOfStaff.getStateChosenBeliefs().get(belief) == null) {
						ArrayList<Integer> values = new ArrayList();
						values.add(value);
						this.chiefOfStaff.getStateChosenBeliefs().put(belief, values);
					} else {
						this.chiefOfStaff.getStateChosenBeliefs().get(belief).add(value);
					

				} catch (UnreadableException e) {
					e.printStackTrace();
				}*/
			}
		} else {
			block();
		}

		/*if (this.chiefOfStaff.getStateChosenCandidates().size() == this.chiefOfStaff.getNrVotersState()) {
			this.chiefOfStaff.calculateChooseCandidate();
			this.chiefOfStaff.calculateChooseBelief();
			this.finished = true;
			ACLMessage msgToCandidate = new ACLMessage(ACLMessage.INFORM);
			AID dest = new AID(this.chiefOfStaff.getBoss().getLocalName(), false);
			msgToCandidate.setContent("Finished");
			msgToCandidate.addReceiver(dest);
			this.chiefOfStaff.send(msgToCandidate);

		}
 */
		return;
	}

	public boolean done() {
		return this.finished;
	}

}