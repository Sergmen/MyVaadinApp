package task.views;

import task.common.DoctorStatistics;
import task.entities.DoctorEntity;
import task.service.DoctorService;
import task.views.Froms.DoctorForm;
import task.views.Froms.Menu;

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
    private HorizontalLayout main = new HorizontalLayout(doctorGrid, form, statistics);
    private Button addButton = new Button("Добавить");
    private Button editButton = new Button("Изменить");
    private Button deliteButton = new Button("Удалить");
    private Button statisticsButton = new Button("Статистика");
    private Button okButton = new Button("OK");

    public DoctorView() {
        setSizeFull();
        setSpacing(true);
        setLabel();
        setMain();
        setMainVisible(ViewType.Grid);
        setToolBar();
        toolbar.setVisible(true);
        addComponents(label,new Menu(),main,toolbar);
    }


    private void setMainVisible(ViewType viewType) {

        switch (viewType) {
            case Grid:
                doctorGrid.setVisible(true);
                form.setVisible(false);
                statistics.setVisible(false);
                return;
            case Form:
                doctorGrid.setVisible(false);
                form.setVisible(true);
                statistics.setVisible(false);
                return;
            case Statistics:
                doctorGrid.setVisible(false);
                form.setVisible(false);
                statistics.setVisible(true);
                return;
        }

    }

    private void setMain() {
        main.setSizeFull();
        main.setExpandRatio(doctorGrid, 1);
        form.setSizeFull();
        doctorGrid.setColumns("surname","name", "patronymic", "specialization", "id");
        doctorGrid.getColumn("name").setCaption("Имя");
        doctorGrid.getColumn("surname").setCaption("Фамилия");
        doctorGrid.getColumn("patronymic").setCaption("Отчество");
        doctorGrid.getColumn("specialization").setCaption("Специализация");
        doctorGrid.setSizeFull();
        setDoctorGrid();
        setStatistics();
    }

    private void setLabel() {
        label.setSizeFull();
        label.setStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        label.addStyleName(ValoTheme.LABEL_H2);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setMainVisible(ViewType.Grid);
        toolbar.setVisible(true);
        setDoctorGrid();
        setStatistics();
    }

    private void setToolBar(){

        addButton.addClickListener(e->{
            toolbar.setVisible(false);
            setMainVisible(ViewType.Form);
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
                toolbar.setVisible(false);
                setMainVisible(ViewType.Form);
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
                } catch (Exception ex) {
                   Notification.show("Ошибка удаления врача!" );
                }
                setDoctorGrid();
            }
        });

        statisticsButton.addClickListener(e -> {
            toolbar.setVisible(false);
            setMainVisible(ViewType.Statistics);
            List<DoctorStatistics> stat = null;
            try {
                stat = patientService.getDoctorStatistics();
                doctorStat.setItems(stat);
            } catch (Exception e1) {
                Notification.show("Ошибка получения статистики по врачам!");
                toolbar.setVisible(true);
                setMainVisible(ViewType.Grid);
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
        setMainVisible(ViewType.Grid);
    }

    private void setStatistics(){
        statistics.addComponents(doctorStat,okButton);
        statistics.setVisible(false);
        statistics.setSizeFull();

        doctorStat.getColumn("name").setCaption("Врач(id)");
        doctorStat.getColumn("recipesNumber").setCaption("Количество рецептов");
        doctorStat.setSizeFull();

        okButton.addClickListener(e -> {
            setMainVisible(ViewType.Grid);
            toolbar.setVisible(true);
        });

    }

    private enum ViewType {
        Grid, Form, Statistics
    }


}
