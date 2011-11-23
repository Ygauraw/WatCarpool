package com.uw.watcarpool.shared;

import java.util.List;

import com.google.gwt.view.client.ListDataProvider;
import com.uw.watcarpool.client.Driver;

public class ClientUtilities {
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
}
