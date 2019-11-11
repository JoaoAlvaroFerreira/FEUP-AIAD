package extras;

public class Table {
    boolean smokers;
    int seats;
    int state; // 0 - livre, 1 - ocupada, 2 - suja

    public Table(int seats, boolean smokers){
    	this.seats = seats;
    	this.smokers = smokers;
    }
}
