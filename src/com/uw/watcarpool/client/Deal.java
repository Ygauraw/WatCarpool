package com.uw.watcarpool.client;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

@SuppressWarnings("serial")
public class Deal implements Serializable {
	
	@Id public Long _UUID;
    @Transient String doNotPersist;
    public Date _timestampe;
    public Date _carpoolDate;
    public String _driverContact;
    public String _driverEmail;
    public String _passengerContact;
    public String _passengerEmail;
    public String _dropoffLoc;
    public String _pickupLoc;
    
    
    @SuppressWarnings("unused")
	private Deal(){};
    public Deal (Date carpoolDate, String driverContact, String driverEmail, String passengerContact, String passengerEmail,
    		String dropoffLoc, String pickupLoc)
    {
	    this._timestampe=new Date();
	    this._carpoolDate=carpoolDate;
	    this._driverContact=driverContact;
	    this._driverEmail=driverEmail;
	    this._passengerContact=passengerContact;
	    this._passengerEmail=passengerEmail;
	    this._dropoffLoc=dropoffLoc;
	    this._pickupLoc=pickupLoc;
     
     
    }
}
