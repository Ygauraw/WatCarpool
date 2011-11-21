package com.uw.watcarpool.client;


import java.util.List;
import com.uw.watcarpool.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
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

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final DataStoreServiceAsync dataStoreService = GWT
			.create(DataStoreService.class);
    
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Declare and initialize all the GUI components based on the root panel
		final TextBox contactField = new TextBox();
		
		final Label errorLabel = new Label();
		contactField.setText("GWT User");
		// Focus the cursor on the name field when the app loads
		contactField.setFocus(true);
		contactField.selectAll();

		
		// Driver
		final Button driverBtn = new Button("Offer a Carpool");
		driverBtn.addStyleName("openInputButton");
		final Button driverCloseBtn = new Button("Close");	
		//driverCloseBtn.getElement().setId("closeButton");// We can set the id of a widget by accessing its Element
		
		
		
		final DateBox carpoolDate = new DateBox();
		final TextBox pickupLoc = new TextBox();
		final TextBox dropoffLoc = new TextBox();
		final TextBox availSpots = new TextBox();
		
		// Fetched Drivers' Table
		final CellTable<Driver> dTable = new CellTable<Driver>();
		
		//Contact Column
		TextColumn<Driver> dContactCol = new TextColumn<Driver>() {
		      @Override
		      public String getValue(Driver d) {
		        return d._contact;
		      }
		    };

		 
		    
		//Date/Time Column
		TextColumn<Driver> dDateTimeCol = new TextColumn<Driver>() {
			      @Override
			      public String getValue(Driver d) {
			        return d._date.toString();
			      }
			    };

			   

	    
	    //Pickup Location Column
	    TextColumn<Driver> dPickupCol = new TextColumn<Driver>() {
	    	@Override
	    	public String getValue(Driver d) {
	    		return d._pickupLoc.toString();
	    	}
	    };
	    
	    //Dropoff Location Column
		TextColumn<Driver> dDropoffCol = new TextColumn<Driver>() {
		      @Override
		      public String getValue(Driver d) {
		        return d._dropoffLoc.toString();
		      }
		    };
		    
	    //Spots Location Column
		TextColumn<Driver> dSpotsCol = new TextColumn<Driver>() {
		      @Override
		      public String getValue(Driver d) {
		        return d._spots+"";
		      }
		    };
		//Add those columns to the drivers' table
		dTable.addColumn(dContactCol, "Contact Number");
		dTable.addColumn(dDateTimeCol, "Carpool Date");
		dTable.addColumn(dPickupCol, "Pickup Location");
		dTable.addColumn(dDropoffCol, "Dropoff Location");
		dTable.addColumn(dSpotsCol, "Available Spots");
		dTable.setTitle("Available Carpools");
		
		    
		// Create a data provider.
		final ListDataProvider<Driver> driverDataProvider = new ListDataProvider<Driver>();
		driverDataProvider.addDataDisplay(dTable);
		
        //Hide the drivers table when there is no data 
		dTable.setVisible(false);
        
		// Passenger
		final Button passengerBtn = new Button("Find a Carpool");
		passengerBtn.addStyleName("openInputButton");
		final Button passengerCloseBtn = new Button("Close");
		//passengerCloseBtn.getElement().setId("closeButton");
		
		final DateBox tripDate = new DateBox();
		final TextBox destination = new TextBox();
		final TextBox numPassengers = new TextBox();
		

		// Declare and initialize all the GUI components on the pop-up dialog panels
		// Driver
		final DialogBox driverDialog = new DialogBox();
		final Button driverSubmitBtn = new Button("Go");
		final VerticalPanel driverDialogPanel = new VerticalPanel();
		final HorizontalPanel driverButtonPanel = new HorizontalPanel();
		
		driverDialog.setText("Basic Carpool Info");
		driverDialog.setAnimationEnabled(true);	
        driverSubmitBtn.getElement().setId("driverSubmitBtn");       
        driverDialogPanel.addStyleName("driverDialogPanel");
        driverDialogPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		driverDialog.setWidget(driverDialogPanel);

       
        
        // Create input fields
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
		
		
		// Passenger
		final DialogBox passengerDialog = new DialogBox();
		final Button passengerSubmitBtn = new Button("Go");
		final VerticalPanel passengerDialogPanel = new VerticalPanel();
		final HorizontalPanel passengerButtonPanel = new HorizontalPanel();
		
		passengerDialog.setAnimationEnabled(true);	
		passengerSubmitBtn.getElement().setId("passengerSubmitBtn");
		passengerDialogPanel.addStyleName("passengerDialogPanel");
		passengerDialog.setWidget(passengerDialogPanel);
		passengerDialogPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		
		
		// Create input fields
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

		
		
		// Add the GUI components to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("contactFieldContainer").add(contactField);
		RootPanel.get("driverButtonContainer").add(driverBtn);
		RootPanel.get("passengerButtonContainer").add(passengerBtn);
		RootPanel.get("errorLabelContainer").add(errorLabel);
		RootPanel.get("fetchedDriversContainer").add(dTable);
		
		


        /**     
         * Add button handlers       
         */
		
		
	// Add a handler to driver's button
		class DriverDialogHanlder implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the driverBtn.
			 */
			public void onClick(ClickEvent event) {
				
				
				//hide previous open tables
				dTable.setVisible(false);
				
				//validate contact number
				String contact = contactField.getText();
				if (FieldVerifier.isValidContact(contact)) {
					errorLabel.setText(""); //remove the previous error message if any;
					driverDialog.setText("Tell us a little bit more...");
					driverDialog.center();
					driverSubmitBtn.setFocus(true);	
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
				dTable.setVisible(false);
				
				//validate contact number
				String contact = contactField.getText();
				if (FieldVerifier.isValidContact(contact)) {
					errorLabel.setText(""); //remove the previous error message if any;
					passengerDialog.setText("Tell us a little bit more...");
					passengerDialog.center();
					passengerSubmitBtn.setFocus(true);			
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
					// First, we validate the input.
					errorLabel.setText("");
					
					// Then, we send the input to the server.
					driverBtn.setEnabled(false);
					
					dataStoreService.checkPassengers("offering",contactField.getText(),contactField.getText(), carpoolDate.getValue(),pickupLoc.getText(), 
							dropoffLoc.getText(),Integer.parseInt(availSpots.getText()),
							new AsyncCallback<List<Passenger>>() {
								public void onFailure(Throwable caught) {
									// Show the RPC error message to the user
									driverDialog
											.setText("Oops..");
									driverDialog.center();
									driverSubmitBtn.setFocus(true);
									caught.printStackTrace();
								}

								public void onSuccess(List<Passenger> drivers) {
									driverDialog.setText("Confirmed");
									driverDialog.center();
									driverSubmitBtn.setFocus(true);
								}
							});
				}		
		});
		
		// Add a handler to passenger's submit button
		passengerSubmitBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
					// First, we validate the input.
					errorLabel.setText("");
					// Then, we send the input to the server.
					passengerBtn.setEnabled(false);
					
					dataStoreService.checkDrivers("seeking",contactField.getText(),contactField.getText(), tripDate.getValue(),"NA", 
							destination.getText(),Integer.parseInt(numPassengers.getText()),
							new AsyncCallback<List<Driver>>() {
								public void onFailure(Throwable caught) {
									// Show the RPC error message to the user
									passengerDialog
											.setText("Oops..");					
									passengerDialog.center();
									passengerSubmitBtn.setFocus(true);
									caught.printStackTrace();
								}

								public void onSuccess(List<Driver> drivers) {
									passengerDialog.hide();	
									if(drivers!=null)
									{									
									populateDrivers(driverDataProvider, drivers);
									passengerBtn.setEnabled(true);
									dTable.setVisible(true);
									}
									else
									{
										dTable.setVisible(false);
										Window.alert("No matched carpools at this time, we'll notify you once matched carpool(s) entered in to your system");
									    passengerBtn.setEnabled(true);
									}
								}
							});

				}		
		});


	}
	 private void populateDrivers(ListDataProvider<Driver> dataProvider, List<Driver> fetchedDrivers)
	 {
		// Add the data to the data provider, which automatically pushes it to the widget
		    
		    List<Driver> drivers = dataProvider.getList();
		    drivers.clear();
		    for (Driver d: fetchedDrivers)
		    {
		    	drivers.add(d);
		    }
		    
	 }
	 

	
	
}
