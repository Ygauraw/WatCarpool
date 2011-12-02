package com.uw.watcarpool.client;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Transient;

@SuppressWarnings("serial")
public class ContactInfo implements Serializable {
	
	@Id public Long _UUID;
    @Transient String doNotPersist;
    public String _phone;
    public String _email;
    @SuppressWarnings("unused")
	private ContactInfo(){};
    public ContactInfo (String phone, String email)
    {
    	this._phone=phone;
    	this._email=email;
    
    }

}