package com.uw.watcarpool.client.composite;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class MyLoginPanel extends Composite{
	final HorizontalPanel loginPanel = new HorizontalPanel();
	
	final Image loginIcon= new Image("img/login.png");
	public MyLoginPanel(final Label loginLabel, final Anchor signInLink, final Anchor signOutLink )
    {
		initWidget(loginPanel);
		// Init Login Panel
		loginPanel.addStyleName("loginPanel");
		loginPanel.add(loginIcon);
		loginPanel.add(loginLabel);
	    loginPanel.add(signInLink);
	    loginPanel.add(signOutLink);
	    loginLabel.setVisible(false);
	    loginLabel.setVisible(false);
	    loginPanel.setVisible(true);
    }
}
