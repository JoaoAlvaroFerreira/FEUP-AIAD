package behaviors;

import jade.core.AID;
import jade.core.behaviours.*;
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
	
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		
		String info = "";
		msg.setContent(info);
		//add destination below
		//AID dest = new AID((String) this.voter.getChiefOfStaffInfo().keySet().toArray()[0], false);
		AID dest = null;
		msg.addReceiver(dest);
		this.client.send(msg);
	}

	@Override
	public boolean done() {
		
		return this.finished;
	}

}
