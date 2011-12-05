package com.uw.watcarpool.client;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Transient;

@SuppressWarnings("serial")
public class Booking implements Serializable {
		
		@Id public Long _UUID;
	    @Transient String doNotPersist;
	    public String _matchId;
	    public Driver _driver;
	    public Passenger _passenger;
	    @SuppressWarnings("unused")
		private Booking(){};
	    public Booking (String matchId, Driver driver, Passenger passenger)
	    {
	    	this._driver=driver;
	    	this._passenger=passenger;
	    	this._matchId=matchId; 
	    
	    }

}
