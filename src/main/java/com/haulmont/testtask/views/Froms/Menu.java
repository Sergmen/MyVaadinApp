package com.haulmont.testtask.views.Froms;

import com.haulmont.testtask.Router;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;


public class Menu extends CustomComponent {

	private Button patientButton = new Button("Пациенты");
	private Button doctorButton = new Button("Врачи");
	private Button recipeButton = new Button("Рецепты");

	public Menu() {
		HorizontalLayout layout = new HorizontalLayout();
        layout.addComponents(patientButton,doctorButton,recipeButton);
		setButtons();
		layout.setSizeUndefined();
		layout.setSpacing(true);
		setSizeFull();
		setCompositionRoot(layout);
	}


	private void setButtons() {
		patientButton.addClickListener(e->{
			getUI().getNavigator().navigateTo(Router.PATIENTVIEW);
		});

		doctorButton.addClickListener(e->{
			getUI().getNavigator().navigateTo(Router.DOCTORVIEW);
		});


		recipeButton.addClickListener(e->{
			getUI().getNavigator().navigateTo(Router.RECIPEVIEW);

		});

    }
}
