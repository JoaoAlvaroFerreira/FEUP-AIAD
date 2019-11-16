package agents;
import behaviors.CookPreparesFood;
import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class Cook extends Agent {

	/**
	 * 
	 **/
	private static final long serialVersionUID = 1L;
	private DFAgentDescription dfad;
	private String specialization;
	private boolean busy;
	// 'VEGGIE' 'ALLERGY' 'CHILD'

	//CONSTRUCTOR
	public Cook(String specialization) {
		this.setSpecialization(specialization);
		this.busy = false;
	}

	//GET
	public String getSpecialization() { return this.specialization;}
	public boolean getBusy() { return this.busy;}

	//SET
	public void setSpecialization(String newSpecialization) { this.specialization = newSpecialization; }
	public void setBusy(boolean newState) { this.busy = newState;}

	public void setup(){
		
		System.out.println("Cook");

		yellowPagesRegister();

		SequentialBehaviour loop = new SequentialBehaviour();
		//add behaviors
		loop.addSubBehaviour(new CookPreparesFood(this));
		addBehaviour(loop);
	}

	private void yellowPagesRegister() {

		ServiceDescription sd = new ServiceDescription();
		sd.setType("Cook");
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
