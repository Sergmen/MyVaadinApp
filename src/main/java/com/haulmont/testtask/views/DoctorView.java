package com.haulmont.testtask.views;

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
    private Label label  = new Label("Врачи");
    private HorizontalLayout toolbar = new HorizontalLayout();
    private HorizontalLayout statistics = new HorizontalLayout();
    private Button addButton = new Button("Добавить");
    private Button editButton = new Button("Изменить");
    private Button deliteButton = new Button("Удалить");
    private Button statisticsButton = new Button("Статистика");

    public DoctorView() {
        setSizeFull();
        setSpacing(true);
        label.setSizeFull();
        label.setStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        label.addStyleName(ValoTheme.LABEL_H2);
        addComponent(label);
        addComponent(new Menu());
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
        form.setVisible(false);
        statistics.setVisible(false);
        addComponent(main);
        addComponent(toolbar);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        statistics.setVisible(false);
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
            Map<String, Integer> map = null;
            try {
                map = patientService.getDoctorStatistics();
                setStatistics(map);
            } catch (Exception e1) {
                Notification.show("Ошибка получения статистики по врачам!");
                statistics.setVisible(false);
                doctorGrid.setVisible(true);
                toolbar.setVisible(true);
            }
        });


        toolbar.addComponents(addButton,editButton,deliteButton);
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

    private void setStatistics(Map<String, Integer> map){

        Table table = new Table("The Brightest Stars");

// Define two columns for the built-in container
        table.addContainerProperty("Name", String.class, null);
        table.addContainerProperty("Mag",  Float.class, null);

// Add a row the hard way
        Object newItemId = table.addItem();
        Item row1 = table.getItem(newItemId);
        row1.getItemProperty("Name").setValue("Sirius");
        row1.getItemProperty("Mag").setValue(-1.46f);

// Add a few other rows using shorthand addItem()
        table.addItem(new Object[]{"Canopus",        -0.72f}, 2);
        table.addItem(new Object[]{"Arcturus",       -0.04f}, 3);
        table.addItem(new Object[]{"Alpha Centauri", -0.01f}, 4);

// Show exactly the currently contained rows (items)
        table.setPageLength(table.size());


        Chart chart = new Chart(ChartType.BAR);
        chart.setWidth("400px");
        chart.setHeight("300px");


        Configuration conf = chart.getConfiguration();
        conf.setTitle("Статистика по врачам");


        ListSeries series = new ListSeries();
        series.setData(new ArrayList<>(map.values()));
        conf.addSeries(series);


        XAxis xaxis = new XAxis();
        String[] array = (String[]) map.keySet().toArray();
        xaxis.setCategories(array);
        xaxis.setTitle("Врачи");
        conf.addxAxis(xaxis);


        YAxis yaxis = new YAxis();
        yaxis.setTitle("Количество рецептов");
        conf.addyAxis(yaxis);

        statistics.addComponent(chart);
    }

}
