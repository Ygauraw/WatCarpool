package com.uw.watcarpool.client.composite;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.uw.watcarpool.client.DataStoreServiceAsync;
import com.uw.watcarpool.client.Driver;
import com.uw.watcarpool.client.LoginInfo;
import com.uw.watcarpool.client.Passenger;
import com.uw.watcarpool.client.SuggestionServiceAsync;
import com.uw.watcarpool.shared.ClientUtilities;
import com.uw.watcarpool.shared.FieldVerifier;

public class MyButtonPanel  extends Composite{
	
	// Declare Internal GUI Components
	final HorizontalPanel hPanel=new HorizontalPanel();
	final Label errorLabel = new Label();
	final Button driverCloseBtn = new Button("Close");
	final TextBox pickupLoc = new TextBox();
	final TextBox availSpots = new TextBox();
	final Button passengerCloseBtn = new Button("Close");
	final TextBox numPassengers = new TextBox();
	final DialogBox driverDialog = new DialogBox();
	final Button driverSubmitBtn = new Button("Go");
	final VerticalPanel driverDialogPanel = new VerticalPanel();
	final HorizontalPanel driverButtonPanel = new HorizontalPanel();
	final DialogBox passengerDialog = new DialogBox();
	final Button passengerSubmitBtn = new Button("Go");
	final VerticalPanel passengerDialogPanel = new VerticalPanel();
	final HorizontalPanel passengerButtonPanel = new HorizontalPanel();
	
	public MyButtonPanel(final DataStoreServiceAsync dataStoreService, final MyTabPanel tabPanel, 
			final SuggestBox contactField, final SuggestionServiceAsync suggestionService, final LoginInfo loginInfo,
			final ListDataProvider<Driver> driverDataProvider,final ListDataProvider<Passenger> passengerDataProvider, final Button driverBtn, final Button passengerBtn,
			final DateBox tripDate,final DateBox carpoolDate, final TextBox destination, final TextBox dropoffLoc)
    {
		    initWidget(hPanel);
	       // Init Driver Components
			driverBtn.addStyleName("openInputButton");
			driverDialog.setText("Basic Carpool Info");
			driverDialog.setAnimationEnabled(true);	
	        driverSubmitBtn.getElement().setId("driverSubmitBtn");       
	        driverDialogPanel.addStyleName("driverDialogPanel");
	        driverDialogPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
			driverDialog.setWidget(driverDialogPanel);

	        // Init Driver Input Panel
			driverDialogPanel.add(new HTML("<b>Date/Time:</b><br>"));
			driverDialogPanel.add(carpoolDate);
		    driverDialogPanel.add(new HTML("<br><b>Pickup Location:</b><br>"));
		    driverDialogPanel.add(pickupLoc);	
		    driverDialogPanel.add(new HTML("<br><b>Drop-off Location:</b><br>"));
		    driverDialogPanel.add(dropoffLoc);
			driverDialogPanel.add(new HTML("<br><b>Available Spots:</b><br>"));
			driverDialogPanel.add(availSpots);
			
			// Add components to their corresponding panels
			driverButtonPanel.add(driverSubmitBtn);
			driverButtonPanel.add(driverCloseBtn);
			driverDialogPanel.add(driverButtonPanel);
			
			
			// Init Passenger Components
		    passengerBtn.addStyleName("openInputButton");
			passengerDialog.setAnimationEnabled(true);	
			passengerSubmitBtn.getElement().setId("passengerSubmitBtn");
			passengerDialogPanel.addStyleName("passengerDialogPanel");
			passengerDialog.setWidget(passengerDialogPanel);
			passengerDialogPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
			
			
			// Init Passenger Input Panel
			passengerDialogPanel.add(new HTML("<b>Date/Time:</b><br>"));	
			passengerDialogPanel.add(tripDate);		
			passengerDialogPanel.add(new HTML("<br><b>Drop-off Location:</b><br>"));
			passengerDialogPanel.add(destination);
			passengerDialogPanel.add(new HTML("<br><b># of Passengers:</b><br>"));
			passengerDialogPanel.add(numPassengers);
					
			
			// Add components to their corresponding panels
			passengerButtonPanel.add(passengerSubmitBtn);
			passengerButtonPanel.add(passengerCloseBtn);
			passengerDialogPanel.add(passengerButtonPanel);
			
			
			
			
			/**
			 * Handlers for different button
			 */
		    // Add a handler to driver's button
			class DriverDialogHanlder implements ClickHandler, KeyUpHandler {
				/**
				 * Fired when the user clicks on the driverBtn.
				 */
				public void onClick(ClickEvent event) {
					//hide previous open tables
					tabPanel.setdTableVisible(false);
					
					//validate contact number
					String contact = contactField.getText();
					if (FieldVerifier.isValidContact(contact)) {
						errorLabel.setText(""); //remove the previous error message if any;
						driverDialog.setText("Tell us a little bit more...");
						driverDialog.center();	
					}
					else
					{
						errorLabel.setText("Oops, please enter your 10 digits contact # (e.g. 5191234567)");
						return;
					}

				}
		
				/**
				 * Fired when the user types in the contactField.
				 */
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						
					}
				}
			}

			// Associate the handler with the button
			DriverDialogHanlder driverHandler = new DriverDialogHanlder();
			driverBtn.addClickHandler(driverHandler);

		    
		   // Add a handler to passenger's button
			class PassengerDialogHanlder implements ClickHandler, KeyUpHandler {
				/**
				 * Fired when the user clicks on the passengerBtn.
				 */
				public void onClick(ClickEvent event) {
					
					//hide previous open tables
					tabPanel.setdTableVisible(false);
					
					//validate contact number
					String contact = contactField.getText();
					if (FieldVerifier.isValidContact(contact)) {
						errorLabel.setText(""); //remove the previous error message if any;
						passengerDialog.setText("Tell us a little bit more...");
						passengerDialog.center();			
					}
					else
					{
						errorLabel.setText("Oops, please enter your 10 digits contact # (e.g. 5191234567)");
						return;
					}		
				}

				/**
				 * Fired when the user types in the contactField.
				 */
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						
					}
				}
			}

			// Associate the handler with the button
			PassengerDialogHanlder passengerHandler = new PassengerDialogHanlder();
			passengerBtn.addClickHandler(passengerHandler);

			
			// Add a handler to driver's dialog close button
			driverCloseBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					driverDialog.hide();
					driverBtn.setEnabled(true);
					driverBtn.setFocus(true);
				}
			});
			
			// Add a handler to passenger's dialog close button
			passengerCloseBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					passengerDialog.hide();
					passengerBtn.setEnabled(true);
					passengerBtn.setFocus(true);
				}
			});
			
	
		    
			// Add a handler to driver's submit button
			driverSubmitBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					 //Register Passenger's contact number for suggestions next time (if not already exist in the database)
		        	 suggestionService.registerContactInfo(contactField.getText(), loginInfo.getEmailAddress(), new AsyncCallback<String>() {
		  	       	      public void onFailure(Throwable error) {
		  	       	    	  Window.alert("Request remote service failed...");
		  	       	    	  error.printStackTrace();
		  	       	      }

		  	       	      public void onSuccess(String result) {
		                  //Attach the new number to the user's email
		  	       	      }
		  	         	});
						// First, we validate the input.
						errorLabel.setText("");
						
						// Then, we send the input to the server.
						driverBtn.setEnabled(false);
						
						dataStoreService.checkPassengers("offering",contactField.getText(),contactField.getText(), carpoolDate.getValue(),pickupLoc.getText(), 
								dropoffLoc.getText(),Integer.parseInt(availSpots.getText()),
								new AsyncCallback<List<Passenger>>() {
									public void onFailure(Throwable caught) {
										// Show the RPC error message to the user
										driverDialog.setText("Check Passengers Request failed...");
										driverDialog.center();
										driverSubmitBtn.setFocus(true);
										caught.printStackTrace();
									}

									public void onSuccess(List<Passenger> passengers) {
										driverDialog.hide();	
										if(passengers!=null)
										{			
										ClientUtilities.populatePassengers(passengerDataProvider, passengers);
										driverBtn.setEnabled(true);
										tabPanel.setpTableVisible(true);
										tabPanel.setSelectTable(1);
										}
										else
										{
											tabPanel.setpTableVisible(false);
											Window.alert("No matched passengers at this time, we'll notify you once matched passenger(s) entered in to our system");
										    driverBtn.setEnabled(true);
										    Window.Location.reload();
										}
										
									}
								});
					}		
			});
			
			// Add a handler to passenger's submit button
			passengerSubmitBtn.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
				
					    //Register Passenger's contact number for suggestions next time (if not already exist in the database)
		        	 suggestionService.registerContactInfo(contactField.getText(), loginInfo.getEmailAddress(), new AsyncCallback<String>() {
		  	       	      public void onFailure(Throwable error) {
		  	       	    	  Window.alert("Request remote service failed...");
		  	       	    	  error.printStackTrace();
		  	       	      }

		  	       	      public void onSuccess(String result) {
		                  //Attach the new number to the user's email
		  	       	      }
		  	         	});
						// First, we validate the input.
						errorLabel.setText("");
						// Then, we send the input to the server.
						passengerBtn.setEnabled(false);
		
						dataStoreService.checkDrivers("seeking",contactField.getText(),contactField.getText(), tripDate.getValue(),"NA", 
								destination.getText(),Integer.parseInt(numPassengers.getText()),
								new AsyncCallback<List<Driver>>() {
									public void onFailure(Throwable caught) {
										// Show the RPC error message to the user
										passengerDialog.setText("Check Drivers Request failed...");					
										passengerDialog.center();
										passengerSubmitBtn.setFocus(true);
										caught.printStackTrace();
									}

									public void onSuccess(List<Driver> drivers) {
										passengerDialog.hide();	
										if(drivers!=null)
										{									
										ClientUtilities.populateDrivers(driverDataProvider, drivers);
										passengerBtn.setEnabled(true);
										tabPanel.setdTableVisible(true);
										tabPanel.setSelectTable(0);
										}
										else
										{
											tabPanel.setdTableVisible(false);
											Window.alert("No matched carpools at this time, we'll notify you once matched carpool(s) entered in to our system");
										    passengerBtn.setEnabled(true);
										    Window.Location.reload();
										}
									}
								});

					}		
			});
    hPanel.add(driverBtn);
    hPanel.add(passengerBtn);
    hPanel.setVisible(true);
 }

	
}
