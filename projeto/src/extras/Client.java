package extras;

public class Client {

	boolean allergy = false;
	boolean vegetarian = false;
	boolean child = false;
	private boolean smoker = false;

	public Client() {

		}
	public Client(boolean allergy, boolean veg, boolean kid, boolean smoke) {
		this.allergy = allergy;
		vegetarian = veg;
		child = kid;
		setSmoker(smoke);

		if(kid)
			setSmoker(false);
	}


	public void makeAllergic() {
		this.allergy = true;
	}
	public void makeVegetarian() {
		this.vegetarian = true;
	}
	public void makeChild() {
		this.child = true;
	}

	public boolean hasAllergy() {
		return allergy;
	}

	public boolean isVegetarian() {
		return vegetarian;
	}
	public boolean isChild() {
		return child;
	}
	public boolean isSmoker() {
		return smoker;
	}

	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}
}
