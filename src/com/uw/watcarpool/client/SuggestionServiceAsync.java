package com.uw.watcarpool.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SuggestionServiceAsync {
	void getSuggestions(String email, AsyncCallback<List<String>> callback)
			throws IllegalArgumentException;
	
	void registerContactInfo(String phone, String email, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
}
