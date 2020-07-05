package com.haulmont.testtask.views;

import com.haulmont.testtask.Menu;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

public class DoctorView extends HorizontalLayout implements View {

    public DoctorView() {

        setSizeFull();
        setSpacing(true);
        addComponent(new Menu());
        addComponent(headingLabel());



    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Notification.show("Showing view: Main!");
    }

    private Label headingLabel() {
        return new Label("Doctor");
    }
}
