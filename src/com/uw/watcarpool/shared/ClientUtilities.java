package com.uw.watcarpool.shared;

import java.util.List;

import com.google.gwt.view.client.ListDataProvider;
import com.uw.watcarpool.client.Booking;
import com.uw.watcarpool.client.Driver;
import com.uw.watcarpool.client.Passenger;

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
	 
	 public static void populatePassengers(ListDataProvider<Passenger> dataProvider, List<Passenger> fetchedPassengers)
	 {
		// Add the data to the data provider, which automatically pushes it to the widget
		    
		    List<Passenger> passengers = dataProvider.getList();
		    passengers.clear();
		    for (Passenger p: fetchedPassengers)
		    {
		    	passengers.add(p);
		    }
		    
	 }
	 
	 
	 public static void populateBookings(ListDataProvider<Booking> dataProvider, List<Booking> myBookings)
	 {
		// Add the data to the data provider, which automatically pushes it to the widget
		    
		    List<Booking> bookings = dataProvider.getList();
		    bookings.clear();
		    for (Booking b: myBookings)
		    {
		    	bookings.add(b);
		    }
		    
	 }

	
}
