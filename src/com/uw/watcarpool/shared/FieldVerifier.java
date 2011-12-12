package com.uw.watcarpool.shared;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.datepicker.client.DateBox;



/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is not translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

	/**
	 * Verifies that the specified name is valid for our service.
	 * 
	 * In this example, we only require that the name is at least four
	 * characters. In your application, you can use more complex checks to ensure
	 * that usernames, passwords, email addresses, URLs, and other fields have the
	 * proper syntax.
	 * 
	 * @param name the name to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidContact(String number) {
		boolean isValid = false;  
	  
		String expression = "^[-+]?[0-9]*\\.?[0-9]+$";  
		RegExp regExp = RegExp.compile(expression);
		
		if(regExp.test(number) && number.length()==10){  
	   		isValid = true;  
		}  
		
		return isValid;  
 }
	public static boolean validateDriverInputs(DateBox datetime, int pickup, int dropoff, String spots, String price) {
		boolean isValid = true;  
	    StringBuilder sb =new StringBuilder();
	    String expression = "^[-+]?[0-9]*\\.?[0-9]+$";  
		RegExp regExp = RegExp.compile(expression);
		if(datetime.getValue()!=null)
		{
		Date clientDate = zeroTime(datetime.getValue());
		Date today = zeroTime(new Date());
			if (clientDate.before(today) || datetime.getValue().toString().trim().substring(11,19).equalsIgnoreCase("12:00:00")||datetime.getValue()==null)
			{
			   isValid=false;
			   sb.append("* Please input a valid and reasonable carpool datetime \n");
			}
		}
		else
		{
			isValid=false;
			sb.append("* Please select your carpool date \n");
		}
		if (pickup==dropoff)
		{
		   isValid=false; 
		   sb.append("* Pickup and Dropoff regions cannot be the same \n");
		}
		if (!regExp.test(spots)||spots.length()==0)
		{
		  isValid=false;
	     sb.append("* You must enter an integer in the Spots field \n");
		}
		if (!regExp.test(price)||price.length()==0)
		{
		  isValid=false;
	      sb.append("* You must enter an integer in the Price field \n");
		}
		if (!isValid)
		{
			Window.alert(sb.toString());
		}
		
		return isValid;  
 }
	public static boolean validatePassengerInputs(DateBox datetime, int pickup, int dropoff, String numPassengers) {
		boolean isValid = true;  
	    StringBuilder sb =new StringBuilder();
	    String expression = "^[-+]?[0-9]*\\.?[0-9]+$";  
		RegExp regExp = RegExp.compile(expression);
		if(datetime.getValue()!=null)
		{
		Date clientDate = zeroTime(datetime.getValue());
		Date today = zeroTime(new Date());
			if (clientDate.before(today) || datetime.getValue().toString().trim().substring(11,19).equalsIgnoreCase("12:00:00")||datetime.getValue()==null)
			{
			   isValid=false;
			   sb.append("* Please input a valid and reasonable carpool datetime \n");
			}
		}
		else
		{
			isValid=false;
			sb.append("* Please select your carpool date \n");
		}
		if (pickup==dropoff)
		{
		   isValid=false; 
		   sb.append("* Pickup and Dropoff regions cannot be the same \n");
		}
		if (!regExp.test(numPassengers)||numPassengers.length()==0)
		{
		  isValid=false;
	      sb.append("* You must enter an integer in the # of Passengers field \n");
		}
		
		if (!isValid)
		{
			Window.alert(sb.toString());
		}
		return isValid;  
 }
	private static Date zeroTime(final Date date)
	{
	    return DateTimeFormat.getFormat("yyyyMMdd").parse(DateTimeFormat.getFormat("yyyyMMdd").format(date));
	}

}
