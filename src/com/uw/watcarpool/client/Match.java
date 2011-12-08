package com.uw.watcarpool.client;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

@SuppressWarnings("serial")
public class Match implements Serializable {
	
	@Id public Long _UUID;
    @Transient String doNotPersist;
    public Date _timestampe;
    public Long _driver;
    public Long _passenger;
    public String _bookerEmail;
    
    @SuppressWarnings("unused")
	private Match(){};
    public Match (Long driverPointer, Long passengerPointer, String bookerEmail)
    {
     this._timestampe=new Date();
     this._driver=driverPointer;
     this._passenger=passengerPointer;
     this._bookerEmail=bookerEmail;
     
    }
}
