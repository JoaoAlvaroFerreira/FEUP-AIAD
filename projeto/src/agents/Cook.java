package agents;
import behaviors.CookPreparesFood;
import extras.Dish;
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
	private Dish dish;
	private String specialization;
	private boolean busy;
	// 'VEGAN' 'ALLERGY' 'CHILD'

	//CONSTRUCTOR
	public Cook(Dish dish, String specialization) {
		this.setDish(dish);
		this.setSpecialization(specialization);
		this.busy = false;
	}

	//GET
	public Dish getDish() { return this.dish;}
	public String getSpecialization() { return this.specialization;}
	public boolean getBusy() { return this.busy;}

	//SET
	public void setDish(Dish newDish) { this.dish = newDish; }
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
		//sd.setType();
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
