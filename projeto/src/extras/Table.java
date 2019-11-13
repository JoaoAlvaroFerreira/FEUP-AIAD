package extras;

import agents.ClientGroup;

public class Table {
    boolean smokers;
    int seats;
    String client_id;

    public Table(int seats, boolean smokers){
    	this.seats = seats;
    	this.smokers = smokers;
    	this.client_id = null;
    }
    
 
    
    public boolean isSmokers() {
    	return smokers;
    }
    
    public int getSeats() {
    	return seats;
    }
    public String getClientID() {
    	return client_id;
    }
    public void setClientID(String client_id) {
    	this.client_id = client_id;
    }
}
