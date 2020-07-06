package com.haulmont.testtask.views.Froms;

import com.haulmont.testtask.common.Priority;
import com.haulmont.testtask.common.Validators;
import com.haulmont.testtask.entities.DoctorEntity;
import com.haulmont.testtask.entities.PatientEntity;
import com.haulmont.testtask.entities.RecipeEntity;
import com.haulmont.testtask.service.DoctorService;
import com.haulmont.testtask.service.RecipeService;
import com.haulmont.testtask.views.DoctorView;
import com.haulmont.testtask.views.RecipeView;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;

public class RecipeForm extends FormLayout {
    private TextField description = new TextField("Описание");
    private ComboBox<PatientEntity> patientEntity = new ComboBox<>("Пациент");
    private ComboBox<DoctorEntity> doctorEntity = new ComboBox<>("Врач");
    private DateField сreationDate = new DateField("Дата создания");
    private DateField expirationDate = new DateField("Срок действия");
    private ComboBox<Priority> priority = new ComboBox<>("Приоритет");


    private RecipeService service = RecipeService.getInstance();
    private RecipeEntity recipeEntity;
    private RecipeView recipeView;
    private Binder<RecipeEntity> binder = new Binder<>(RecipeEntity.class);




public RecipeForm(RecipeView recipeView) {
        this.recipeView = recipeView;

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        addComponents(surname, name,patronymic, specialization, buttons);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        setBinderValidators();
        save.addClickListener(e -> this.save());
        cancel.addClickListener(e -> this.cancel());

    }

    private void setBinderValidators() {
        binder.forField(name)
                .withValidator(Validators.textLenghtValidator)
                .withValidator(new RegexpValidator("Имя должно состоять из русских букв!","^[А-Яа-я]+$"))
                .bind(DoctorEntity::getName,DoctorEntity::setName);

        binder.forField(surname)
                .withValidator(Validators.textLenghtValidator)
                .withValidator(new RegexpValidator("Имя должно состоять из русских букв!","^[А-Яа-я]+$"))
                .bind(DoctorEntity::getSurname,DoctorEntity::setSurname);

        binder.forField(patronymic)
                .withValidator(Validators.textLenghtValidator)
                .withValidator(new RegexpValidator("Имя должно состоять из русских букв!","^[А-Яа-я]+$"))
                .bind(DoctorEntity::getPatronymic,DoctorEntity::setPatronymic);

        binder.forField(specialization)
                .withValidator(Validators.textLenghtValidator)
                .withValidator(new RegexpValidator("Специализация должна состоять из русских букв!","^[А-Яа-я]+$"))
                .bind(DoctorEntity::getSpecialization,DoctorEntity::setSpecialization);

    }

    public void setDoctor(DoctorEntity doctor) {
        this.doctor = doctor;
        binder.setBean(doctor);
        name.selectAll();
    }

    private void cancel() {
        setVisible(false);
        doctorView.setDoctorGridVisible();
        doctorView.setToolbarVisible();
    }

    private void save() {
        if (binder.isValid()) {
            try {
                service.saveOrUpdate(doctor);
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("Ошибка сохранения врача!");
            }
            setVisible(false);
            doctorView.setDoctorGridVisible();
            doctorView.setToolbarVisible();
        }
        else {
            Notification.show("Введите корректные данные!");
        }
    }

    private void setSelectionsComponents(){
        // List of Planet objects
        List<Planet> planets = new ArrayList<>();
        planets.add(new Planet("Mercury"));
        planets.add(new Planet("Venus"));
        planets.add(new Planet("Earth"));

// Create a selection component
        ComboBox<Planet> select = new ComboBox<>("My Select");

// Add an items to the ComboBox
        select.setItems(planets);

        select.setItemCaptionGenerator(Planet::getName);

// Select the first
        select.setSelectedItem(planets.get(0));
// or
// select.setValue(planets.get(0));
    }
}