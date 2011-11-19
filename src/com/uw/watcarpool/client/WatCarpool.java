package com.uw.watcarpool.client;


import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.uw.watcarpool.shared.FieldVerifier;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
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
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

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
		
		// Fetched Result CellList
		final TextCell fetchedDrivers = new TextCell();
		final CellList<String> fetchedDriversList = new CellList<String>(fetchedDrivers); 
		fetchedDriversList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

	    // Add a selection model to handle user selection.
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
	    fetchedDriversList.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        String selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	          
	        }
	      }
	    });
	    
		final List<String> DAYS = Arrays.asList("Sunday", "Monday",
			      "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
		fetchedDriversList.setRowData(0, DAYS);
        fetchedDriversList.setVisible(true);

		
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
		RootPanel.get("fetchedDriversContainer").add(fetchedDriversList);
		
		


        /**     
         * Add button handlers       
         */
		
		
	// Add a handler to driver's button
		class DriverDialogHanlder implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the driverBtn.
			 */
			public void onClick(ClickEvent event) {
				
				//driverBtn.setEnabled(false);
				driverDialog.setText("Tell us a little bit more...");
				driverDialog.center();
				driverSubmitBtn.setFocus(true);				
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
				
				//passengerBtn.setEnabled(false);
				passengerDialog.setText("Tell us a little bit more...");
				passengerDialog.center();
				passengerSubmitBtn.setFocus(true);				
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
					String contact = contactField.getText();
					if (!FieldVerifier.isValidName(contact)) {
						errorLabel.setText("Please enter at least four characters");
						return;
					}

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
					String contact = contactField.getText();
					if (!FieldVerifier.isValidName(contact)) {
						errorLabel.setText("Please enter at least four characters");
						return;
					}

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
									passengerDialog.setText("Confirmed");
									passengerDialog.center();
									passengerSubmitBtn.setFocus(true);
								}
							});

				}		
		});

		
	}
	
	
}
