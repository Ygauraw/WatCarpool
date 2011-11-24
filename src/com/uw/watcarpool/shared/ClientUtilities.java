package com.uw.watcarpool.shared;

import java.util.List;

import com.google.gwt.view.client.ListDataProvider;
import com.uw.watcarpool.client.Driver;

public class ClientUtilities {
	 private static String clientIdentity=null;
	 public static void populateDrivers(ListDataProvider<Driver> dataProvider, List<Driver> fetchedDrivers)
	 {
		// Add the data to the data provider, which automatically pushes it to the widget
		    
		    List<Driver> drivers = dataProvider.getList();
		    drivers.clear();
		    for (Driver d: fetchedDrivers)
		    {
		    	drivers.add(d);
		    }
		    
	 }
	 
	 public static void setClientIdentity(String identity)
	 {
		 clientIdentity=identity;
	 }
	 
	 public static String getClientIdentity()
	 {
		return  clientIdentity;
	 }
	 
	 
}
