package extras;

public class Client {

	boolean shrimpAllergy = false;
	boolean nutAllergy = false;
	boolean vegetarian = false;
	boolean child = false;
	private boolean smoker = false;
	
	public Client() {
			
	}

	public Client(boolean shrimp, boolean nut, boolean veg, boolean kid, boolean smoke) {

		shrimpAllergy = shrimp;
		nutAllergy = nut;
		vegetarian = veg;
		child = kid;
		setSmoker(smoke);
		
		if(kid)
			setSmoker(false);
	}

	public void makeAllergicShrimp() {
		this.shrimpAllergy = true;
	}
	public void makeAllergicNuts() {
		this.nutAllergy = true;
	}
	public void makeVegetarian() {
		this.vegetarian = true;
	}
	public void makeChild() {
		this.child = true;
	}
	
	public boolean hasShrimpAllergy() {
		return shrimpAllergy;
	}
	public boolean hasNutAllergy() {
		return nutAllergy;
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
