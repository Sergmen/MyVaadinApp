package com.haulmont.testtask.views.Froms;

import com.haulmont.testtask.common.Validators;
import com.haulmont.testtask.entities.DoctorEntity;
import com.haulmont.testtask.service.DoctorService;
import com.haulmont.testtask.views.DoctorView;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class DoctorForm extends FormLayout {
    private TextField name = new TextField("Имя");
    private TextField surname = new TextField("Фамилия");
    private TextField patronymic = new TextField("Отчество");
    private TextField specialization = new TextField("Специализация");
    private Button save = new Button("Сохранить");
    private Button cancel = new Button("Отмена");
    private DoctorService service = DoctorService.getInstance();
    private DoctorEntity doctor;
    private DoctorView doctorView;
    private Binder<DoctorEntity> binder = new Binder<>(DoctorEntity.class);

    public DoctorForm(DoctorView doctorView) {
        this.doctorView = doctorView;

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
}

