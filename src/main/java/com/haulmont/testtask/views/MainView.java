package com.haulmont.testtask.views;

import com.haulmont.testtask.Menu;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;



@SuppressWarnings("serial")
public class MainView extends HorizontalLayout implements View {

	public MainView() {
		setSizeFull();
		setSpacing(true);
		addComponent(new Menu());
		addComponent(headingLabel());

	}

	@Override
	public void enter(ViewChangeEvent event) {
		Notification.show("Showing view: Main!");
	}
	
	private Label headingLabel() {
		return new Label("Main");
	}
	

}
