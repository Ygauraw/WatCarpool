package com.uw.watcarpool.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataStoreServiceAsync {
	void checkPassengers(String kind, String name, String contact, Date date, String pickupLoc, String dropoffLoc, int spots, AsyncCallback<List<Passenger>> callback)
			throws IllegalArgumentException;
	
	void checkDrivers(String kind, String name, String contact, Date date, String pickupLoc, String dropoffLoc, int spots, AsyncCallback<List<Driver>> callback)
			throws IllegalArgumentException;
}
