package com.uw.watcarpool.client;



import java.util.List;

import com.uw.watcarpool.client.composite.MyButtonPanel;
import com.uw.watcarpool.client.composite.MyLoginPanel;
import com.uw.watcarpool.client.composite.MyTabPanel;
import com.uw.watcarpool.shared.ClientUtilities;
import com.uw.watcarpool.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WatCarpool implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	// Initialize RPC Services
	private final DataStoreServiceAsync dataStoreService = GWT.create(DataStoreService.class);  //GAE DataStore
	private final LoginServiceAsync loginService = GWT.create(LoginService.class); //GAE UserService
	private final SuggestionServiceAsync suggestionService = GWT.create(SuggestionService.class); //Suggestion Service
	
   // Declare sharable GUI components
	private LoginInfo loginInfo = null;
	final Label loginLabel = new Label("");
	final Anchor signInLink = new Anchor(" Sign In ");
	final Anchor signOutLink = new Anchor(" Sign Out ");
	boolean noSuggestions =false;
	final Button driverBtn = new Button("Offer a Carpool");	
	final Button passengerBtn = new Button("Find a Carpool");
	final DateBox carpoolDate = new DateBox();
	final DateBox tripDate = new DateBox();
	final TextBox dropoffLoc = new TextBox();
	final TextBox destination = new TextBox();
	final Button refreshBtn = new Button("Refresh");
	final ListDataProvider<Driver> driverDataProvider = new ListDataProvider<Driver>();
	final ListDataProvider<Passenger> passengerDataProvider = new ListDataProvider<Passenger>();
	final ListDataProvider<Booking> bookingDataProvider = new ListDataProvider<Booking>();

	// Initialize Composite GUI Components
	MyTabPanel tabPanel;
	MyButtonPanel buttonsPanel;
	MyLoginPanel loginPanel;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	
		//Verify Login Status
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	    	  Window.alert("Request remote service failed...");
	    	  error.printStackTrace();
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) {
	        	loadGWTComponents();
	        	loginLabel.setText("Welcome, "+result.getEmailAddress());
	        	loginLabel.setVisible(true);
	 		    signOutLink.setHref(loginInfo.getLogoutUrl());
	        	signOutLink.setVisible(true);
	        	signInLink.setVisible(false);	        	
	        	
	        } else {
	        	//Cookie Management
	    		Cookies.removeCookie("_ah_SESSION");
	    	    //loadGWTComponents();
	        	renderLogin();
	        	signInLink.setHref(loginInfo.getLoginUrl());
	         	signInLink.setVisible(true);
	 		    signOutLink.setVisible(false);
	        }
	      }
	    });
		
	 
	}
	
	private void loadGWTComponents()
	{
	   // Initialize Login Panel
	   loginPanel = new MyLoginPanel(loginLabel,signInLink,signOutLink);
	   // Create and initialize the SuggestionBox
       final SuggestBox contactField = new SuggestBox(initSuggestions());
	
		// Initialize Contact TextBox
	    contactField.setTitle("e.g. 5191234567");
		contactField.setFocus(true);
		
		// Initialize the tabPanel
		tabPanel = new  MyTabPanel(dataStoreService,driverDataProvider,passengerDataProvider,
				bookingDataProvider,loginInfo, tripDate, carpoolDate, destination,dropoffLoc);
				
		// Initialize the buttonsPanel
		buttonsPanel = new  MyButtonPanel(dataStoreService, tabPanel, contactField, 
		suggestionService, loginInfo,driverDataProvider, passengerDataProvider, driverBtn, passengerBtn,
		tripDate,carpoolDate, destination,dropoffLoc);
					
		
		// Add Refresh Button Handler
		refreshBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.Location.reload();
			}
		});

		

		// Add the GUI components to the RootPanel
		RootPanel.get("loginPanel").add(loginPanel);
		RootPanel.get("contactFieldContainer").add(contactField);
		RootPanel.get("buttonsContainer").add(buttonsPanel);
		RootPanel.get("tabPanelContainer").add(tabPanel);
		RootPanel.get("refreshButtonContainer").add(refreshBtn);
	}
	
	
	//jQuery module - Login Panel
	private static native void renderLogin() /*-{
	 $wnd.jQuery(function ($) {
			$("#basic-modal-content").modal({onOpen: function (dialog) {
	    	dialog.overlay.fadeIn('slow', function () {
			dialog.data.hide();
			dialog.container.fadeIn('slow', function () {
			dialog.data.slideDown('slow');
			});
		});
	}});
	
	
	});
}-*/;
	
	/*
	 * Helper Methods
	 */
	 private MultiWordSuggestOracle initSuggestions()
	 {
			/*
	    	 * Load Suggestion Oracles
	    	 */
		   final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	       suggestionService.getSuggestions(loginInfo.getEmailAddress(),new AsyncCallback<List<String>>() {
	  	      public void onFailure(Throwable error) {
	  	    	  Window.alert("Request remote service failed...");
	  	    	  error.printStackTrace();
	  	      }

	  	      public void onSuccess(List<String> suggestions) {

	  	        if(suggestions.size()!=0) {
	  	         
	  	        String []phones = new String[suggestions.size()];
	  	        suggestions.toArray(phones);
	  	          for (int i = 0; i < phones.length; ++i) {
	  	            oracle.add(phones[i]);
	  	          }
	           
	   	        
	  	        } else {
	               noSuggestions=true;   	
	  	        }
	  	      }
	    	});
	       return oracle;
	 }
	  
}
