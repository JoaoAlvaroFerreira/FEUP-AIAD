package extras;

import agents.ClientGroup;

public class Table {
    boolean smokers;
    int seats;
    boolean clients;
    

    public Table(int seats, boolean smokers){
    	this.seats = seats;
    	this.smokers = smokers;
    	this.clients = false;
    }
    
    public void assignClients() {
    	this.clients = true;
    }
    
    public void cleanTable() {
    	this.clients = false;
    }
    
    public boolean getEmpty() {
    	return !clients;
    }
    
    public boolean isSmokers() {
    	return smokers;
    }
    
    public int getSeats() {
    	return seats;
    }
}
