package behaviors;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShowGui;
import jade.lang.acl.ACLMessage;
import agents.ClientGroup;

public class ClientsRequestTable extends SimpleBehaviour  {

	private boolean finished = false;
	private ClientGroup client;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ClientsRequestTable(ClientGroup client) {
		this.client = client;
	}

	@Override
	public void action() {
		
		System.out.println("Action request table");

		myAgent.getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
		myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(myAgent.getDefaultDF());
		msg.setOntology(JADEManagementOntology.NAME);
		msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		Action a = new Action();
		a.setActor( myAgent.getDefaultDF() );
		a.setAction( new ShowGui() );
		try {
			myAgent.getContentManager().fillContent(msg,a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		myAgent.send(msg);
		myAgent.doDelete();
	
	}
	/*
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		
		String info = "";
		msg.setContent(info);
		//add destination below
		//AID dest = new AID((String) this.voter.getChiefOfStaffInfo().keySet().toArray()[0], false);
		AID dest = null;
		msg.addReceiver(dest);
		this.client.send(msg);
	}*/

	@Override
	public boolean done() {
		
		return this.finished;
	}

}
