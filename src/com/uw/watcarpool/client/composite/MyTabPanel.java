package com.uw.watcarpool.client.composite;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
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
			final ListBox destination, final ListBox dropoffLoc, final SuggestBox contactNumber)
    {
		// Initialize Tab Panel
		initWidget(tabPanel);
		tabPanel.setAnimationEnabled(true);
		tabPanel.setVisible(true);
	    tabPanel.add(dTable, "Fetched Drivers");
	    tabPanel.add(pTable, "Fetched Passengers");
	    tabPanel.add(new HTML("2"),"My Active Listings");
	    tabPanel.add(bTable, "My Pending Bookings");
	    tabPanel.setWidth("1200px");
		tabPanel.selectTab(0);
		
		// Load Drivers Table
		loadDriversTable(dataStoreService, driverDataProvider,bookingDataProvider,loginInfo, tripDate, destination, contactNumber);
		// Load Passengers Table
		loadPassengersTable(dataStoreService, passengerDataProvider,bookingDataProvider,loginInfo, carpoolDate,  dropoffLoc, contactNumber);
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
			final LoginInfo loginInfo, final DateBox tripDate, final ListBox destination, final SuggestBox contactNumber)
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
	    		return d._pickupLoc;
	    	}
	    };
	    
	    // Dropoff Location Column
		TextColumn<Driver> dDropoffCol = new TextColumn<Driver>() {
		      @Override
		      public String getValue(Driver d) {
		        return d._dropoffLoc;
		      }
		    };
		    
	    // Spots Location Column
		TextColumn<Driver> dSpotsCol = new TextColumn<Driver>() {
		      @Override
		      public String getValue(Driver d) {
		        return d._spots+"";
		      }
		    };
		    
	    // Price Column
		TextColumn<Driver> dPriceCol = new TextColumn<Driver>() {
		      @Override
		      public String getValue(Driver d) {
		        return d._price+"";
		      }
		    };
		// Add those columns to the drivers' table

		dTable.addColumn(dDateTimeCol, "Carpool Date");
		dTable.addColumn(dPickupCol, "Pickup Region");
		dTable.addColumn(dDropoffCol, "Dropoff Region");
		dTable.addColumn(dSpotsCol, "Available Spots");
		dTable.addColumn(dPriceCol, "Price($)");
		// Add ButtonCell, click to show driver's comments
	    addDriverColumn(new ButtonCell(), "Details", new GetDriverValue<String>() {
	      @Override
	      public String getValue(Driver d) {
	        return "Show";
	      }
	    }, new FieldUpdater<Driver, String>() {
	      @Override
	      public void update(int index, Driver d, String value) {
	        Window.alert("<Driver's Comment> \n"+d._detail);
	      }
	    });
	    
	    // Add Contact Action Button Column
 		addDriverColumn(new ActionCell<Driver>("Contact", new ActionCell.Delegate<Driver>() {
 	      @Override
 	      public void execute(final Driver d) { 	          
 	    	 // Update Driver and Match tables
		    	dataStoreService.updateDrivers(d._UUID.toString(),tripDate.getValue(), loginInfo.getEmailAddress(),destination.getItemText(destination.getSelectedIndex()).toUpperCase().trim(),
						contactNumber.getText(), new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								
								caught.printStackTrace();
							}

							public void onSuccess(String uuid) {
								if(uuid!=null)
								{
								Window.alert("The driver has been notified. Please keep your reference id: "+uuid);
								deleteDriverRow(d, driverDataProvider);
								}
								else
								{
							    Window.alert("Oops, the request to contact the driver failed");
								}
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
            
 	      }
 	    }), "", new GetDriverValue<Driver>() {
 	      @Override
 	      public Driver getValue(Driver d) {
 	        return d;
 	      }
 	    }, null);
		dTable.setTitle("Available Carpools");
		dTable.setColumnWidth(dDateTimeCol, 16.0, Unit.PCT);
		// attach the datasource to the drivers table
		driverDataProvider.addDataDisplay(dTable);
        // Hide the drivers table when there is no data 
		dTable.setVisible(false);
		

	}
	

	private void loadPassengersTable(final DataStoreServiceAsync dataStoreService, final ListDataProvider<Passenger> passengerDataProvider,
			final ListDataProvider<Booking> bookingDataProvider,final LoginInfo loginInfo, final DateBox carpoolDate, final ListBox dropoffLoc, final SuggestBox contactNumber)
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
		TextColumn<Passenger> pPickupCol = new TextColumn<Passenger>() {
		      @Override
		      public String getValue(Passenger p) {
		        return p._pickupLoc;
		      }
		    };
	    // Dropoff Location Column
		TextColumn<Passenger> pDropoffCol = new TextColumn<Passenger>() {
		      @Override
		      public String getValue(Passenger p) {
		        return p._dropoffLoc;
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
		
		pTable.addColumn(pDateTimeCol, "Carpool Date");
		pTable.addColumn(pPickupCol, "Pickup Region");
		pTable.addColumn(pDropoffCol, "Dropoff Region");
		pTable.addColumn(pSpotsCol, "# of Passengers");
		// Add ButtonCell, click to show driver and passenger's comments
	    addPassengerColumn(new ButtonCell(), "Details", new GetPassengerValue<String>() {
	      @Override
	      public String getValue(Passenger p) {
	        return "Show";
	      }
	    }, new FieldUpdater<Passenger, String>() {
	      @Override
	      public void update(int index, Passenger p, String value) {
	        Window.alert("<Passenger's Comment> \n"+p._detail);
	      }
	    });
	    // Add Contact Action Button Column
  		addPassengerColumn(new ActionCell<Passenger>("Contact", new ActionCell.Delegate<Passenger>() {
  	      @Override
  	      public void execute(final Passenger p) { 	          
  	    	 // Update Driver and Match tables
  	    	dataStoreService.updatePassengers(p._UUID.toString(),carpoolDate.getValue(), loginInfo.getEmailAddress(),dropoffLoc.getItemText(dropoffLoc.getSelectedIndex()).toUpperCase().trim(),
						contactNumber.getText(), new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								
								caught.printStackTrace();
							}

							public void onSuccess(String uuid) {
								if (uuid!=null)
								{
								Window.alert("The passenger has been notified. Please keep your reference id: "+uuid);
								deletePassengerRow(p, passengerDataProvider);
								}
								else
								{
								 Window.alert("Oops, the request to contact the passenger failed");
								}
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
             
  	      }
  	    }), "", new GetPassengerValue<Passenger>() {
  	      @Override
  	      public Passenger getValue(Passenger p) {
  	        return p;
  	      }
  	    }, null);
		pTable.setTitle("Available Passengers");
		pTable.setColumnWidth(pDateTimeCol, 16.0, Unit.PCT);
		// attach the datasource to the passengers table
		passengerDataProvider.addDataDisplay(pTable);
        // Hide the passengers table when there is no data 
		pTable.setVisible(false);
	

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
		    	StringBuilder sb = new StringBuilder();
		    	sb.append(b._driver._userId+"\n");
		    	sb.append(b._driver._contact);
		        return sb.toString();
		      }
		    };
		driverContactCol.setSortable(true);
    
		TextColumn<Booking> passengerContactCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
	    	  StringBuilder sb = new StringBuilder();
		      sb.append(b._passenger._userId+"\n");
		      sb.append(b._passenger._contact);
		      return sb.toString();
		     
		      }
		    };
		passengerContactCol.setSortable(true);
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

	    TextColumn<Booking> spotsCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
		        return b._driver._spots+"";
		        
		        		
		      }
		    };
		spotsCol.setSortable(true);
		
		TextColumn<Booking> priceCol = new TextColumn<Booking>() {
		      @Override
		      public String getValue(Booking b) {
		        return "$"+b._driver._price+"";
		      }
		    };
		priceCol.setSortable(true);
		
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
		bTable.addColumn(carpoolDateCol, "Carpool Date");
		bTable.addColumn(driverContactCol, "Driver");
		bTable.addColumn(passengerContactCol, "Passenger");
		bTable.addColumn(pickupCol, "Pickup Region");
		bTable.addColumn(dropoffCol, "Dropoff Region");
		bTable.addColumn(spotsCol, "Remaining Spots");
		bTable.addColumn(priceCol, "Price");
		// Add ButtonCell, click to show driver and passenger's comments
	    addBookingColumn(new ButtonCell(), "Details", new GetBookingValue<String>() {
	      @Override
	      public String getValue(Booking b) {
	        return "Show";
	      }
	    }, new FieldUpdater<Booking, String>() {
	      @Override
	      public void update(int index, Booking b, String value) {
	        Window.alert("<Driver's Comment> \n"+b._driver._detail+"\n"+
	        		     "<Passenger's Comment> \n "+b._passenger._detail);
	      }
	    });
		// Add Confirm Action Button Column
		addBookingColumn(new ActionCell<Booking>("Confirm", new ActionCell.Delegate<Booking>() {
	      @Override
	      public void execute(final Booking b) {
	    	  if (b._bookerEmail.equalsIgnoreCase(loginInfo.getEmailAddress()))
	    	  {
	    		Window.alert("Sorry, you cannot confirm your own booking, please wait for the counter party to confirm it.");
	    	  }
	    	  else
	    	  {
	          
	    	  // Update the Database 
			  dataStoreService.deleteBooking(b._matchId, true, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						
					}

					public void onSuccess(String dealID) {
						if (dealID!=null)
						{
						 // Update the GUI
							 deleteBookingRow(b, bookingDataProvider);
							 Window.alert("The booking has been confirm ("+dealID+") Both the driver and passenger will be notified shortly. Please keep the confirmation ID for departure.");
						}
						else
						{
							Window.alert("Oops, updating the database failed.");
						}
					}
				});
	    	  }
	      }
	    }), "", new GetBookingValue<Booking>() {
	      @Override
	      public Booking getValue(Booking b) {
	        return b;
	      }
	    }, null);
		
		// Add Decline Action Button Column
		addBookingColumn(new ActionCell<Booking>("Delete", new ActionCell.Delegate<Booking>() {
	      @Override
	      public void execute(final Booking b) {
	    	  // Update the Database 
			  dataStoreService.deleteBooking(b._matchId,false, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						
					}

					public void onSuccess(String dealID) {
							 deleteBookingRow(b, bookingDataProvider);
							 Window.alert("The pending booking has been deleted successfully.");
						}
				});
	    	 
	      }
	    }), "", new GetBookingValue<Booking>() {
	      @Override
	      public Booking getValue(Booking b) {
	        return b;
	      }
	    }, null);
		bTable.setTitle("My Pending Bookings");
		bTable.setVisible(true);
		bTable.setColumnWidth(carpoolDateCol, 16.0, Unit.PCT);
		bTable.setColumnWidth(driverContactCol, 15.0, Unit.PCT);
		bTable.setColumnWidth(passengerContactCol, 15.0, Unit.PCT);
		bTable.setColumnWidth(spotsCol, 10.0, Unit.PCT);
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
	// Helper Methods
	// Add a new column to the bTable
	 private <C> Column<Booking, C> addBookingColumn(Cell<C> cell, String headerText,
		      final GetBookingValue<C> getter, FieldUpdater<Booking, C> fieldUpdater) {
		    Column<Booking, C> column = new Column<Booking, C>(cell) {
		      @Override
		      public C getValue(Booking object) {
		        return getter.getValue(object);
		      }
		    };
		    column.setFieldUpdater(fieldUpdater);
		    if (cell instanceof AbstractEditableCell<?, ?>) {
		      //editableCells.add((AbstractEditableCell<?, ?>) cell);
		    }
		   bTable.addColumn(column, headerText);
		    return column;
		  }
	  /**
	   * Get a cell value from a record.
	   * 
	   * @param <C> the cell type
	   */
	  private static interface GetBookingValue<C> {
	    C getValue(Booking b);
	  }
	  
	// Add a new column to the dTable
	 private <C> Column<Driver, C> addDriverColumn(Cell<C> cell, String headerText,
		      final GetDriverValue<C> getter, FieldUpdater<Driver, C> fieldUpdater) {
		    Column<Driver, C> column = new Column<Driver, C>(cell) {
		      @Override
		      public C getValue(Driver object) {
		        return getter.getValue(object);
		      }
		    };
		    column.setFieldUpdater(fieldUpdater);
		    if (cell instanceof AbstractEditableCell<?, ?>) {
		      //editableCells.add((AbstractEditableCell<?, ?>) cell);
		    }
		   dTable.addColumn(column, headerText);
		    return column;
		  }
	  /**
	   * Get a cell value from a record.
	   * 
	   * @param <C> the cell type
	   */
	  private static interface GetDriverValue<C> {
	    C getValue(Driver d);
	  }
	 
	 // Add a new column to the dTable
	 private <C> Column<Passenger, C> addPassengerColumn(Cell<C> cell, String headerText,
		      final GetPassengerValue<C> getter, FieldUpdater<Passenger, C> fieldUpdater) {
		    Column<Passenger, C> column = new Column<Passenger, C>(cell) {
		      @Override
		      public C getValue(Passenger object) {
		        return getter.getValue(object);
		      }
		    };
		    column.setFieldUpdater(fieldUpdater);
		    if (cell instanceof AbstractEditableCell<?, ?>) {
		      //editableCells.add((AbstractEditableCell<?, ?>) cell);
		    }
		   pTable.addColumn(column, headerText);
		    return column;
		  }
	  /**
	   * Get a cell value from a record.
	   * 
	   * @param <C> the cell type
	   */
	  private static interface GetPassengerValue<C> {
	    C getValue(Passenger p);
	  }
	 
		  
	  // Delete the confirmed booking row
	  private void deleteBookingRow(Booking b, ListDataProvider<Booking> bookingDataProvider)
	  {
		  
		  // Update the GUI
		  List<Booking> list = bookingDataProvider.getList();
		  int indexOf = list.indexOf(b);
		  list.remove(indexOf);
		  bookingDataProvider.refresh();

	  }
	  // Delete clicked driver row
	  private void deleteDriverRow(Driver d, ListDataProvider<Driver> driverDataProvider)
	  {
		  
		  // Update the GUI
		  List<Driver> list = driverDataProvider.getList();
		  int indexOf = list.indexOf(d);
		  list.remove(indexOf);
		  driverDataProvider.refresh();

	  }
	  // Delete clicked passenger row
	  private void deletePassengerRow(Passenger p, ListDataProvider<Passenger> passengerDataProvider)
	  {
		  
		  // Update the GUI
		  List<Passenger> list = passengerDataProvider.getList();
		  int indexOf = list.indexOf(p);
		  list.remove(indexOf);
		  passengerDataProvider.refresh();

	  }
   }
