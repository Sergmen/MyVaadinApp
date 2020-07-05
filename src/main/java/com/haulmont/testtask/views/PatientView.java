package com.haulmont.testtask.views;

import com.haulmont.testtask.Menu;
import com.haulmont.testtask.PatientForm;
import com.haulmont.testtask.Router;
import com.haulmont.testtask.entities.PatientEntity;
import com.haulmont.testtask.service.PatientService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.NoSuchElementException;


public class PatientView extends VerticalLayout implements View {

    private PatientService patientService  = PatientService.getInstance();
    private PatientForm form = new PatientForm(this);
    private Grid<PatientEntity> patientGrid= new Grid<>(PatientEntity.class);
    private Label label  = new Label("Пациенты");
    private HorizontalLayout toolbar = new HorizontalLayout();
    private Button addButton = new Button("Добавить");
    private Button editButton = new Button("Изменить");
    private Button deliteButton = new Button("Удалить");

    public PatientView() {
        setSizeFull();
        setSpacing(true);
        addComponent(label);
        label.setStyleName(".v-align-center");
        addComponent(new Menu());

        HorizontalLayout main = new HorizontalLayout(patientGrid, form);
        main.setSizeFull();
        main.setExpandRatio(patientGrid, 1);
        patientGrid.setColumns("name", "surname", "patronymic", "phone", "id");
        patientGrid.getColumn("name").setCaption("Имя");
        patientGrid.getColumn("surname").setCaption("Фамилия");
        patientGrid.getColumn("patronymic").setCaption("Отчество");
        patientGrid.getColumn("phone").setCaption("Телефон");

        patientGrid.setSizeFull();
        setPatientGrid();
        setToolbar();
        form.setVisible(false);
        addComponent(main);
        addComponent(toolbar);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        form.setVisible(false);
        setPatientGridVisible();
        setToolbarVisible();
    }

    private void setToolbar(){
        addButton.addClickListener(e->{
            patientGrid.setVisible(false);
            toolbar.setVisible(false);
            form.setVisible(true);
            form.setPatient(new PatientEntity());
        });
        editButton.addClickListener(e->{
            PatientEntity patientEntity;
            try {
                patientEntity=patientGrid.getSelectedItems().stream().findFirst().get();
            }
            catch (NoSuchElementException ex){
                Notification.show("Выбирите элемент для редактирования!");
                patientEntity = null;
            }
            if (patientEntity!=null) {
                patientGrid.setVisible(false);
                toolbar.setVisible(false);
                form.setVisible(true);
                form.setPatient(patientEntity);
            }

        });
        deliteButton.addClickListener(e->{
            PatientEntity patientEntity;
            try {
                patientEntity=patientGrid.getSelectedItems().stream().findFirst().get();
            }
            catch (NoSuchElementException ex){
                Notification.show("Выбирите элемент для удаления!");
                patientEntity = null;
            }
            if (patientEntity!=null) {
                try {
                    patientService.delete(patientEntity);
                } catch (Exception e1) {
                    Notification.show("Ошибка удаления пациента!");
                }
                setPatientGrid();
            }
        });
        toolbar.addComponents(addButton,editButton,deliteButton);
    }

    public void setToolbarVisible(){
        toolbar.setVisible(true);
    }

    private void setPatientGrid() {
        patientGrid.setItems(patientService.findAll());

    }
    public void setPatientGridVisible() {
        setPatientGrid();
        patientGrid.setVisible(true);
   }


}
