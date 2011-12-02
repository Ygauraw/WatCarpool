package com.uw.watcarpool.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("suggestion")
public interface SuggestionService extends RemoteService {
	List<String> getSuggestions(String email) throws IllegalArgumentException;
	String registerContactInfo(String phone, String email) throws IllegalArgumentException;
}
