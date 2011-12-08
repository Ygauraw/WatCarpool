package com.uw.watcarpool.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataStoreServiceAsync {
	void checkPassengers(String kind, String name, String contact, Date date, String pickupLoc, String dropoffLoc, int spots,int price ,String comment, AsyncCallback<List<Passenger>> callback)
			throws IllegalArgumentException;
	
	void checkDrivers(String kind, String name, String contact, Date date, String pickupLoc, String dropoffLoc, int spots, String comment, AsyncCallback<List<Driver>> callback)
			throws IllegalArgumentException;
	
	void updateDrivers(String dUUID, Date date, String userId, String dropoffLoc, String contact, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void updatePassengers(String pUUID, Date date, String userId, String dropoffLoc, String contact, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getBookings(String email, AsyncCallback<List<Booking>> callback)
			throws IllegalArgumentException;
	void deleteBooking(String matchId, boolean isDeal, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
}
