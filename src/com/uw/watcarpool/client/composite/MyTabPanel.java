package com.uw.watcarpool.client.composite;

import java.util.Comparator;
import java.util.List;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.uw.watcarpool.client.Booking;
import com.uw.watcarpool.client.DataStoreServiceAsync;
import com.uw.watcarpool.client.Driver;
import com.uw.watcarpool.client.LoginInfo;
import com.uw.watcarpool.client.Passenger;
import com.uw.watcarpool.shared.ClientUtilities;

public class MyTabPanel  extends Composite{
	// Declare internal GUI components
	final TabPanel tabPanel = new TabPanel();
	final CellTable<Booking> bTable = new CellTable<Booking>();
	final CellTable<Driver> dTable = new CellTable<Driver>(); // Matched Drivers' Results Table
	final CellTable<Passenger> pTable = new CellTable<Passenger>(); // Matched Passengers' Results Table
	
	
	public MyTabPanel(final DataStoreServiceAsync dataStoreService, final ListDataProvider<Driver> driverDataProvider,
			final ListDataProvider<Passenger> passengerDataProvider,final ListDataProvider<Booking> bookingDataProvider,final LoginInfo loginInfo, final DateBox tripDate,final DateBox carpoolDate, 
			final TextBox destination, final TextBox dropoffLoc)
    {
		// Initialize Tab Panel
		initWidget(tabPanel);
		tabPanel.setAnimationEnabled(true);
		tabPanel.setVisible(true);
	    tabPanel.add(dTable, "Available Drivers");
	    tabPanel.add(pTable, "Available Passengers");
	    tabPanel.add(new HTML("2"),"Manage My Carpools");
	    tabPanel.add(bTable, "Manage My Bookings");
	    tabPanel.setWidth("1200px");
		tabPanel.selectTab(0);
		
		// Load Drivers Table
		loadDriversTable(dataStoreService, driverDataProvider,bookingDataProvider,loginInfo, tripDate, destination);
		// Load Passengers Table
		loadPassengersTable(dataStoreService, passengerDataProvider,bookingDataProvider,loginInfo, carpoolDate,  dropoffLoc);
		// Load Bookings Table
		loadBookingsTable(dataStoreService, bookingDataProvider,loginInfo);
		
		
    }
	
	/*
	 * Setters and Getters
	 */
	public void setdTableVisible(boolean b)
	{
		dTable.setVisible(b);
	}
	
	public void setpTableVisible(boolean b)
	{
		pTable.setVisible(b);
	}
	
	public void setbTableVisible(boolean b)
	{
		bTable.setVisible(b);
	}
	public void setSelectTable(int i)
	{
		tabPanel.selectTab(i);
	}
	
	
	/*
	 * Sub GUI Components
	 */
	private void loadDriversTable(final DataStoreServiceAsync dataStoreService, final ListDataProvider<Driver> driverDataProvider, final ListDataProvider<Booking> bookingDataProvider,
			final LoginInfo loginInfo, final DateBox tripDate, final TextBox destination)
	{
		/*
		 *  Create Columns for Drivers' Table
		 */
		// UUID Column
		TextColumn<Driver> dUUIDCol = new TextColumn<Driver>() {
		      @Override
		      public String getValue(Driver d) {
		        return d._UUID+"";
		      }
		    };
				    
    
		// Date/Time Column
		TextColumn<Driver> dDateTimeCol = new TextColumn<Driver>() {
			      @Override
			      public String getValue(Driver d) {
			    	@SuppressWarnings("deprecation")
					String date = DateTimeFormat.getMediumDateTimeFormat().format(d._date);
			        return date;
			      }
			    };
			    
	    // Pickup Location Column
	    TextColumn<Driver> dPickupCol = new TextColumn<Driver>() {
	    	@Override
	    	public String getValue(Driver d) {
	    		return d._pickupLoc.toString();
	    	}
	    };
	    
	    // Dropoff Location Column
		TextColumn<Driver> dDropoffCol = new TextColumn<Driver>() {
		      @Override
		      public String getValue(Driver d) {
		        return d._dropoffLoc.toString();
		      }
		    };
		    
	    // Spots Location Column
		TextColumn<Driver> dSpotsCol = new TextColumn<Driver>() {
		      @Override
		      public String getValue(Driver d) {
		        return d._spots+"";
		      }
		    };
		    
		// Add those columns to the drivers' table
		dTable.addColumn(dUUIDCol, "ID");
		dTable.addColumn(dDateTimeCol, "Carpool Date");
		dTable.addColumn(dPickupCol, "Pickup Location");
		dTable.addColumn(dDropoffCol, "Dropoff Location");
		dTable.addColumn(dSpotsCol, "Available Spots");
		dTable.setTitle("Available Carpools");
		
		// attach the datasource to the drivers table
		driverDataProvider.addDataDisplay(dTable);
        // Hide the drivers table when there is no data 
		dTable.setVisible(false);
		// Add SelectionModel to dTable; 
		final SingleSelectionModel<Driver> ssm = new SingleSelectionModel<Driver>();
		dTable.setSelectionModel(ssm);
		ssm.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		    @ Override
		    public void onSelectionChange(final SelectionChangeEvent event)
		    {
		    	
		        SC.confirm("Do you want to contact the driver?", new BooleanCallback() {
		            public void execute(Boolean value) {
		              if (value != null && value) {
		            	final Driver d = ssm.getSelectedObject();
		  		    	dataStoreService.updateDrivers(d._UUID.toString(),tripDate.getValue(), loginInfo.getEmailAddress(),destination.getText().toUpperCase().trim(),
		  						new AsyncCallback<String>() {
		  							public void onFailure(Throwable caught) {
		  								
		  								caught.printStackTrace();
		  							}

		  							public void onSuccess(String uuid) {
		  								Window.alert("The driver has been notified. Please keep your reference id: "+uuid);
		  							}
		  						});
		  		    	dataStoreService.getBookings(loginInfo.getEmailAddress(), new AsyncCallback<List<Booking>>() {
		  							public void onFailure(Throwable caught) {
		  								
		  								caught.printStackTrace();
		  							}

		  							public void onSuccess(List<Booking> myBookings) {
		  								ClientUtilities.populateBookings(bookingDataProvider, myBookings);	
		  							}
		  						});
		              } else {
		            	  //clear selection
		            	  //ssm.setSelected(ssm.getSelectedObject(), false);
		            	
		              }
		            }
		          });
		    	
		    }
		});
		

	}
	

	@SuppressWarnings("unused")
	private void loadPassengersTable(final DataStoreServiceAsync dataStoreService, final ListDataProvider<Passenger> passengerDataProvider,
			final ListDataProvider<Booking> bookingDataProvider,final LoginInfo loginInfo, final DateBox carpoolDate, final TextBox dropoffLoc)
	{
		/*
		 *  Create Columns for Passengers' Table
		 */
		// UUID Column
		TextColumn<Passenger> pUUIDCol = new TextColumn<Passenger>() {
		      @Override
		      public String getValue(Passenger p) {
		        return p._UUID+"";
		      }
		    };
				    
    
		// Date/Time Column
		TextColumn<Passenger> pDateTimeCol = new TextColumn<Passenger>() {
			      @Override
			      public String getValue(Passenger p) {
			    	@SuppressWarnings("deprecation")
					String date = DateTimeFormat.getMediumDateTimeFormat().format(p._date);
			        return date;
			      }
			    };
			    
	    
	    // Dropoff Location Column
		TextColumn<Passenger> pDropoffCol = new TextColumn<Passenger>() {
		      @Override
		      public String getValue(Passenger p) {
		        return p._dropoffLoc.toString();
		      }
		    };
		    
	    // Spots Location Column
		TextColumn<Passenger> pSpotsCol = new TextColumn<Passenger>() {
		      @Override
		      public String getValue(Passenger p) {
		        return p._spots+"";
		      }
		    };
		    
		// Add those columns to the drivers' table
		pTable.addColumn(pUUIDCol, "ID");
		pTable.addColumn(pDateTimeCol, "Carpool Date");
		pTable.addColumn(pDropoffCol, "Dropoff Location");
		pTable.addColumn(pSpotsCol, "# of Passengers");
		pTable.setTitle("Available Passengers");
		
		// attach the datasource to the passengers table
		passengerDataProvider.addDataDisplay(pTable);
        // Hide the passengers table when there is no data 
		pTable.setVisible(false);
		// Add SelectionModel to pTable; 
		final SingleSelectionModel<Passenger> ssm = new SingleSelectionModel<Passenger>();
		pTable.setSelectionModel(ssm);
		ssm.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		    @ Override
		    public void onSelectionChange(final SelectionChangeEvent event)
		    {
		    	
		        SC.confirm("Do you want to contact the passenger?", new BooleanCallback() {
		            public void execute(Boolean value) {
		              if (value != null && value) {
		            	final Passenger p = ssm.getSelectedObject();
		  		    	dataStoreService.updatePassengers(p._UUID.toString(),carpoolDate.getValue(), loginInfo.getEmailAddress(),dropoffLoc.getText().toUpperCase().trim(),
		  						new AsyncCallback<String>() {
		  							public void onFailure(Throwable caught) {
		  								
		  								caught.printStackTrace();
		  							}

		  							public void onSuccess(String uuid) {
		  								Window.alert("The passenger has been notified. Please keep your reference id: "+uuid);
		  							}
		  						});
		  		    	dataStoreService.getBookings(loginInfo.getEmailAddress(), new AsyncCallback<List<Booking>>() {
		  							public void onFailure(Throwable caught) {
		  								
		  								caught.printStackTrace();
		  							}

		  							public void onSuccess(List<Booking> myBookings) {
		  								ClientUtilities.populateBookings(bookingDataProvider, myBookings);	
		  							}
		  						});
		              } else {
		            	  //clear selection
		            	  //ssm.setSelected(ssm.getSelectedObject(), false);
		            	
		              }
		            }
		          });
		    	
		    }
		});
		

	}
	
	private void loadBookingsTable(final DataStoreServiceAsync dataStoreService, final ListDataProvider<Booking> bookingDataProvider,
			final LoginInfo loginInfo)
	{
		/*
		 * Create Columns for Bookings Table
		 */	    
		TextColumn<Booking> carpoolDateCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
		    	@SuppressWarnings("deprecation")
				String date = DateTimeFormat.getMediumDateTimeFormat().format(b._driver._date);
		        return date;
		      }
		    };
        carpoolDateCol.setSortable(true);

		TextColumn<Booking> driverContactCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
		        return b._driver._contact;
		      }
		    };
		driverContactCol.setSortable(true);
		
		TextColumn<Booking> driverEmailCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
		        return b._driver._userId;
		      }
		    };
		driverEmailCol.setSortable(true);
    
		TextColumn<Booking> passengerContactCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
		        return b._passenger._contact;
		      }
		    };
		passengerContactCol.setSortable(true);
		
		TextColumn<Booking> passengerEmailCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
		        return b._passenger._userId;
		      }
		    };
		passengerEmailCol.setSortable(true);

	    TextColumn<Booking> pickupCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
		        return b._driver._pickupLoc;
		      }
		    };
	    TextColumn<Booking> dropoffCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
		        return b._driver._dropoffLoc;
		      }
		    };
		dropoffCol.setSortable(true);

	    TextColumn<Booking> numSeatsCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
		        return b._driver._spots+"";
		      }
		    };
		numSeatsCol.setSortable(true);

	    TextColumn<Booking> numPassengersCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
		        return b._passenger._spots+"";
		      }
		    };
		numPassengersCol.setSortable(true);
 
		// Add sorting for the Carpool Date column
	   ListHandler<Booking> carpoolDateSortHandler = new ListHandler<Booking>(bookingDataProvider.getList());
	   carpoolDateSortHandler.setComparator(carpoolDateCol, new Comparator<Booking>() {
		     public int compare(Booking b1, Booking b2) {
		    	 if(b1._driver._date.after(b2._driver._date))
	    	            return 1;
	    	        else if(b1._driver._date.before(b2._driver._date))
	    	            return -1;
	    	        else
	    	            return 0; 
		     }
		   });
		bTable.addColumnSortHandler(carpoolDateSortHandler);
		
		// Add sorting for the Driver'Contact column
	   ListHandler<Booking> driverContactSortHandler = new ListHandler<Booking>(bookingDataProvider.getList());
	   driverContactSortHandler.setComparator(driverContactCol, new Comparator<Booking>() {
		     public int compare(Booking b1, Booking b2) {
		    	 return b1._driver._contact.compareTo(b2._driver._contact);
		     }
		   });
		bTable.addColumnSortHandler(driverContactSortHandler);
		
		// Add those columns to the drivers' table
		bTable.addColumn(carpoolDateCol, "Carppol Date");
		bTable.addColumn(driverContactCol, "Driver's Contact #");
		bTable.addColumn(driverEmailCol, "Driver's Email");
		bTable.addColumn(passengerContactCol, "Passenger's Contact #");
		bTable.addColumn(passengerEmailCol, "Passenger's Email");
		bTable.addColumn(pickupCol, "Pickup Location");
		bTable.addColumn(dropoffCol, "Dropoff Location");
		bTable.addColumn(numSeatsCol, "Available Spots");
		bTable.addColumn(numPassengersCol, "# of Passenger");
		bTable.setTitle("My Pending Bookings");
		bTable.setVisible(true);
		// Create a data provider.
		bookingDataProvider.addDataDisplay(bTable);
		
		// Get Bookings for the current logged in user
    	dataStoreService.getBookings(loginInfo.getEmailAddress(), new AsyncCallback<List<Booking>>() {
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					public void onSuccess(List<Booking> myBookings) {
						ClientUtilities.populateBookings(bookingDataProvider, myBookings);	
						tabPanel.selectTab(3);
					}
				});
	}
   }
