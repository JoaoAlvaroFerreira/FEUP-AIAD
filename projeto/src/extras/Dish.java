package extras;

public class Dish {

   private boolean hasAlergens;
   private boolean isVegetarian;
   private boolean kidsMeal;

   public Dish(boolean allergic, boolean veggie, boolean kids) {
	   this.hasAlergens = allergic;
	   this.isVegetarian = veggie;
	   this.kidsMeal = kids;
   }

   //GETS
   public boolean isAllergic() { return this.hasAlergens;}
   public boolean isVeggie() { return this.isVegetarian;}
   public boolean isKidsMeal() { return this.kidsMeal;}

   //SETS
   public void setAllergic(boolean allergen) { this.hasAlergens = allergen; }
   public void setVeggie(boolean veggie) { this.isVegetarian = veggie; }
   public void setKidsMeal(boolean kids) { this.kidsMeal = kids; }

}
