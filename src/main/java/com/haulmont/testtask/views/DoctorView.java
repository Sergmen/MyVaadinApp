package com.haulmont.testtask.views;

import com.haulmont.testtask.common.DoctorStatistics;
import com.haulmont.testtask.entities.DoctorEntity;
import com.haulmont.testtask.service.DoctorService;
import com.haulmont.testtask.views.Froms.DoctorForm;
import com.haulmont.testtask.views.Froms.Menu;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import java.util.*;

public class DoctorView extends VerticalLayout implements View {

    private DoctorService patientService  = DoctorService.getInstance();
    private DoctorForm form = new DoctorForm(this);
    private Grid<DoctorEntity> doctorGrid= new Grid<>(DoctorEntity.class);
    private Grid<DoctorStatistics> doctorStat= new Grid<>(DoctorStatistics.class);
    private Label label  = new Label("Врачи");
    private HorizontalLayout toolbar = new HorizontalLayout();
    private VerticalLayout statistics = new VerticalLayout();
    private Button addButton = new Button("Добавить");
    private Button editButton = new Button("Изменить");
    private Button deliteButton = new Button("Удалить");
    private Button statisticsButton = new Button("Статистика");
    private Button okButton = new Button("OK");

    public DoctorView() {
        setSizeFull();
        setSpacing(true);

        label.setSizeFull();
        label.setStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        label.addStyleName(ValoTheme.LABEL_H2);

        HorizontalLayout main = new HorizontalLayout(doctorGrid, form, statistics);
        main.setSizeFull();
        main.setExpandRatio(doctorGrid, 1);

        doctorGrid.setColumns("name", "surname", "patronymic", "specialization", "id");
        doctorGrid.getColumn("name").setCaption("Имя");
        doctorGrid.getColumn("surname").setCaption("Фамилия");
        doctorGrid.getColumn("patronymic").setCaption("Отчество");
        doctorGrid.getColumn("specialization").setCaption("Специализация");
        doctorGrid.setSizeFull();
        setDoctorGrid();

        setToolbar();
        setStatistics();
        form.setVisible(false);
        doctorGrid.setVisible(false);
        addComponents(label, new Menu(),main, toolbar);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        doctorGrid.setVisible(false);
        form.setVisible(false);
        setDoctorGridVisible();
        setToolbarVisible();
    }

    private void setToolbar(){

        addButton.addClickListener(e->{
            doctorGrid.setVisible(false);
            toolbar.setVisible(false);
            form.setVisible(true);
            form.setDoctor(new DoctorEntity());
        });
        editButton.addClickListener(e->{
            DoctorEntity doctorEntity;
            try {
                doctorEntity=doctorGrid.getSelectedItems().stream().findFirst().get();
            }
            catch (NoSuchElementException ex){
                Notification.show("Выбирите элемент для редактирования!");
                doctorEntity = null;
            }
            if (doctorEntity!=null) {
                doctorGrid.setVisible(false);
                toolbar.setVisible(false);
                form.setVisible(true);
                form.setDoctor(doctorEntity);
            }
        });
        deliteButton.addClickListener(e->{
            DoctorEntity doctorEntity;
            try {
                doctorEntity=doctorGrid.getSelectedItems().stream().findFirst().get();
            }
            catch (NoSuchElementException ex){
                Notification.show("Выбирите элемент для удаления!");
                doctorEntity = null;
            }
            if (doctorEntity!=null) {
                try {
                    patientService.delete(doctorEntity);
                } catch (Exception e1) {
                    Notification.show("Ошибка удаления врача!");
                }
                setDoctorGrid();
            }
        });

        statisticsButton.addClickListener(e -> {
            statistics.setVisible(true);
            doctorGrid.setVisible(false);
            toolbar.setVisible(false);
            List<DoctorStatistics> stat = null;
            try {
                stat = patientService.getDoctorStatistics();
                doctorStat.setItems(stat);;
            } catch (Exception e1) {
                Notification.show("Ошибка получения статистики по врачам!");
                statistics.setVisible(false);
                doctorGrid.setVisible(true);
                toolbar.setVisible(true);
            }
        });

        toolbar.addComponents(addButton,editButton,deliteButton,statisticsButton);
    }

    public void setToolbarVisible(){
        toolbar.setVisible(true);
    }

    private void setDoctorGrid() {
        doctorGrid.setItems(patientService.findAll());

    }
    public void setDoctorGridVisible() {
        setDoctorGrid();
        doctorGrid.setVisible(true);
    }

    private void setStatistics(){
        statistics.addComponents(doctorStat,okButton);
        statistics.setVisible(false);

        doctorStat.getColumn("name").setCaption("Врач(id)");
        doctorStat.getColumn("recipesNumber").setCaption("Количество рецептов");
        doctorStat.setSizeFull();

        okButton.addClickListener(e -> {
            statistics.setVisible(false);
            doctorGrid.setVisible(true);
            toolbar.setVisible(true);
        });

    }

}
