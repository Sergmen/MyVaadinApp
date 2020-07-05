package com.haulmont.testtask;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class Menu extends CustomComponent {

	public Menu() {
		HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(patientButton());
        layout.addComponent(doctorButton());
        layout.addComponent(recipeButton());
		layout.setSizeUndefined();
		layout.setSpacing(true);
		setSizeFull();
		setCompositionRoot(layout);
	}

    private Button patientButton() {
        Button button = new Button("Пациенты", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                getUI().getNavigator().navigateTo(Router.PATIENTVIEW);
            }
        });
        return button;
    }

    private Button doctorButton() {
		Button button = new Button("Доктора", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(Router.DOCTORVIEW);
			}
		});
		return button;
	}

	private Button recipeButton() {
		Button button = new Button("Рецепты", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo(Router.RECIPEVIEW);
			}
		});
		return button;
	}
}
