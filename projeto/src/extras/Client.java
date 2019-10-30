package extras;

public class Client {

	boolean shrimpAllergy = false;
	boolean nutAllergy = false;
	boolean vegetarian = false;
	boolean child = false;
	
	public Client() {
		
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
}
