package com.uw.watcarpool.client;

import java.util.Date;
import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
@RemoteServiceRelativePath("dataStore")
public interface DataStoreService extends RemoteService {
	List<Passenger> checkPassengers(String kind, String name, String contact, Date date, String pickupLoc, String dropoffLoc, int spots, int price, String comment) throws IllegalArgumentException;
	List<Driver> checkDrivers(String kind, String name, String contact, Date date, String pickupLoc, String dropoffLoc, int spots, String comment) throws IllegalArgumentException;
	String updateDrivers(String dUUID, Date date, String userId, String dropoffLoc, String contact) throws IllegalArgumentException;
	String updatePassengers(String pUUID, Date date, String userId, String dropoffLoc, String contact) throws IllegalArgumentException;
	List<Booking> getBookings(String email) throws IllegalArgumentException;
	String deleteBooking(String matchId, boolean isDeal) throws IllegalArgumentException;
}
