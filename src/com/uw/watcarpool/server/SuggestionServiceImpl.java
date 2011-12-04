package com.uw.watcarpool.server;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.uw.watcarpool.client.ContactInfo;
import com.uw.watcarpool.client.SuggestionService;

@SuppressWarnings("serial")
public class SuggestionServiceImpl extends RemoteServiceServlet implements
		SuggestionService {

	static{
		ObjectifyService.register(ContactInfo.class);
	  
		}
	
	public List<String> getSuggestions(String email) 
	{
		List<String> suggestions =new ArrayList<String>();
     	Objectify cfy = ObjectifyService.begin();
    	
    	Query<ContactInfo> q = cfy.query(ContactInfo.class);
    	QueryResultIterator<ContactInfo> iterator = q.iterator();
    	
    	while (iterator.hasNext()) {
    		ContactInfo c = iterator.next();
    	    if (c._email.equalsIgnoreCase(email))
    	    {
    	    	suggestions.add(c._phone);
    	    }
    	}
    	return suggestions;
		
	}
	public String registerContactInfo (String phone, String email)
	{
		Objectify cfy = ObjectifyService.begin();
		//Verify if the number already exist
		Query<ContactInfo> q = cfy.query(ContactInfo.class);
		QueryResultIterator<ContactInfo> iterator = q.iterator();
		boolean exist=false;
		String UUID=null;
    	while (iterator.hasNext()) {
    		ContactInfo c = iterator.next();
    	    if ((c._phone.equalsIgnoreCase(phone.trim())) &&(c._email.equalsIgnoreCase(email)))
    	    {
    	    	exist=true;   
    	    }
    	  
    	}
    	if (!exist)
  	    {
  	    	ContactInfo newNumber =new ContactInfo(phone, email);
  	    	assert newNumber._UUID != null;
  		    cfy.put(newNumber);
  		    UUID=newNumber._UUID.toString();
  	    }
		
		
		return "Registered a new number, id: "+UUID;
	    
	    
	}

}