package com.uw.watcarpool.server;

import java.util.HashMap;
import java.util.Map;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestResponse;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.list.AccountList;

// TODO: Auto-generated Javadoc
/**
 * The Class TwilioSMS.
 */
public class TwilioSMS{
	/** The Constant ACCOUNT_SID. */
	public static final String ACCOUNT_SID = "ACa2ea968d271b4b21a099e47eeb6b1936";
	public static final String AUTH_TOKEN = "3b090d6d031af1eb723f23dbc8aa773a";
	
	
	// Create a rest client
	TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);


	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws TwilioRestException
	 *             the twilio rest exception
	 */
	


	public String sendMessage(String _to, String _message) throws TwilioRestException
    {
	
		// Get the main account (The one we used to authenticate the client
		Account mainAccount = client.getAccount();		
		
		// Send an sms
		SmsFactory smsFactory = mainAccount.getSmsFactory();
		Map<String, String> smsParams = new HashMap<String, String>();
		smsParams.put("To", _to); // Replace with a valid phone number
		smsParams.put("From", "(646) 755-7665"); // Replace with a valid phone										// number in your account
		smsParams.put("Body", _message);
		smsFactory.create(smsParams);

		

		// Make a raw request to the api.
		TwilioRestResponse resp = client.request("/2010-04-01/Accounts", "GET",
				null);
		if (!resp.isError()) {
			return resp.getResponseText();
		}
		else
		{
			return "Failed to send the message.";
		}

	}

}

