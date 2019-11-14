package behaviors;

import agents.Waiter;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;

public class WaiterListenCheckRequest extends SimpleBehaviour {

	private Waiter waiter;
	private boolean finished = false;

	public WaiterListenCheckRequest(Waiter waiter) {
		this.waiter = waiter;
	}

	public void action() {

		ACLMessage msg = this.waiter.blockingReceive();

		if (msg != null) {
		    if (msg.getSender().getLocalName().substring(0, 12).equals("client_group")) {

		        try {
					ACLMessage msgToClient = new ACLMessage(ACLMessage.INFORM);
					AID dest = msg.getSender();				
					msgToClient.addReceiver(dest);
					ArrayList<String> message = (ArrayList) msg.getContentObject();
					
					int client_number = Integer.parseInt(message.get(1));
					boolean smoking = false;
					if(message.get(3)== "For son-smokers, if possible.")
						smoking = true;
					//ASSIGN TABLE IF AVAILABLE
					boolean table_found = false;
					System.out.println("Available tables: "+this.waiter.getTables().size());
					for(int i = 0; i < this.waiter.getTables().size(); i++) {
						
						if(this.waiter.getTables().get(i).getSeats() >= client_number && 
							this.waiter.getTables().get(i).getEmpty() && 
							this.waiter.getTables().get(i).isSmokers()==smoking) {
							System.out.println("Found");
							this.waiter.getTables().get(i).assignClients();
							
							msgToClient.setContent("We found a table");
						
						table_found = true;

							
						}
						if(table_found)
							break;
							
					}
					if(!table_found) {
						msgToClient.setContent("We did not find a table");
					}
					System.out.println("SENT:" + msgToClient.getContent() + " TO: " + dest.getLocalName());
					this.waiter.send(msgToClient);
					
				}
				catch (UnreadableException e) {
					e.printStackTrace();
				}

	
}
}
	
return;	}

	public boolean done() {
		return this.finished;
	}

}