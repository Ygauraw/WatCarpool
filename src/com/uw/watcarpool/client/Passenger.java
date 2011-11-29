package com.uw.watcarpool.client;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Transient;

public class Passenger implements Serializable {

 private static final long serialVersionUID = -3985879017430394322L;
 @Id public Long _UUID;
 @Transient String doNotPersist;
 public String _contact;
 public Date _date;
 public String _pickupLoc; 
 public String _dropoffLoc; 
 public int _spots;
 public String _userId;
 public boolean _pending;
 public boolean _matched;
 public String _matchedId;
 
 private Passenger(){};
 public Passenger (String contact, Date date, String pickupLoc, String dropoffLoc, int spots,  String userId)
 {

	 this._contact=contact;
	 this._date=date;
	 this._pickupLoc=pickupLoc;
	 this._dropoffLoc=dropoffLoc;
	 this._spots=spots;
	 this._userId= userId;
	 this._pending=false;
	 this._matched=false;
     this._matchedId="";
 }
}