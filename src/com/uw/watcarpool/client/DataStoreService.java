package com.uw.watcarpool.client;

import java.util.Date;
import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("dataStore")
public interface DataStoreService extends RemoteService {
	List<Passenger> checkPassengers(String kind, String name, String contact, Date date, String pickupLoc, String dropoffLoc, int spots) throws IllegalArgumentException;
	List<Driver> checkDrivers(String kind, String name, String contact, Date date, String pickupLoc, String dropoffLoc, int spots) throws IllegalArgumentException;
	String updateDrivers(String dUUID, Date date, String userId, String dropoffLoc) throws IllegalArgumentException;
	List<Booking> getBookings(String email) throws IllegalArgumentException;
}
