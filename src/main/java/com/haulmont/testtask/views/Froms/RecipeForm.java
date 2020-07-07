package com.haulmont.testtask.views.Froms;

import com.haulmont.testtask.common.Priority;
import com.haulmont.testtask.common.Validators;
import com.haulmont.testtask.entities.DoctorEntity;
import com.haulmont.testtask.entities.PatientEntity;
import com.haulmont.testtask.entities.RecipeEntity;
import com.haulmont.testtask.service.DoctorService;
import com.haulmont.testtask.service.PatientService;
import com.haulmont.testtask.service.RecipeService;
import com.haulmont.testtask.views.RecipeView;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.joda.time.LocalDate;


import java.util.List;

public class RecipeForm extends FormLayout {
    private TextField description = new TextField("Описание");
    private ComboBox<PatientEntity> patientField = new ComboBox<>("Пациент");
    private ComboBox<DoctorEntity> doctorField = new ComboBox<>("Врач");
    private DateField сreationDate = new DateField("Дата создания");
    private DateField expirationDate = new DateField("Срок действия");
    private ComboBox<Priority> priority = new ComboBox<>("Приоритет");
    private RecipeService service = RecipeService.getInstance();
    private DoctorService doctorService = DoctorService.getInstance();
    private PatientService patientService = PatientService.getInstance();
    private RecipeEntity recipe;
    private RecipeView recipeView;
    private Binder<RecipeEntity> binder = new Binder<>(RecipeEntity.class);
    private Button save = new Button("Сохранить");
    private Button cancel = new Button("Отмена");





    public RecipeForm(RecipeView recipeView) {
        this.recipeView = recipeView;

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        addComponents(description, patientField,doctorField, сreationDate, expirationDate, priority, buttons);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        сreationDate.setData(java.time.LocalDate.now());

        setSelectionsComponents();
        setBinderValidators();
        save.addClickListener(e -> this.save());
        cancel.addClickListener(e -> this.cancel());

    }

    private void setBinderValidators() {
        binder.forField(description)
                .withValidator(Validators.textLenghtValidator)
                .withValidator(new RegexpValidator("Описание должно состоять из русских букв!","^[А-Яа-я]+$"))
                .bind(RecipeEntity::getDescription,RecipeEntity::setDescription);

        binder.forField(patientField)
                .withValidator(value-> value != null, "Поле не может быть пустым!")
                .bind(RecipeEntity::getPatient,RecipeEntity::setPatient);

        binder.forField(doctorField)
                .withValidator(value-> value != null, "Поле не может быть пустым!")
                .bind(RecipeEntity::getDoctor,RecipeEntity::setDoctor);

        binder.forField(сreationDate)
                .withValidator(value-> value != null, "Поле не может быть пустым!")
                .withConverter(value->new LocalDate(value.getYear(),value.getMonthValue(), value.getDayOfMonth()), modelValue->java.time.LocalDate.ofYearDay(modelValue.getYear(),modelValue.getDayOfYear()), "Введен неверный формат даты!")
                .bind(RecipeEntity::getСreationDate,RecipeEntity::setСreationDate);

        binder.forField(expirationDate)
                .withValidator(value-> value != null, "Поле не может быть пустым!")
                .withConverter(value->new LocalDate(value.getYear(),value.getMonthValue(), value.getDayOfMonth()), modelValue->java.time.LocalDate.ofYearDay(modelValue.getYear(),modelValue.getDayOfYear()), "Введен неверный формат даты!")
                .withValidator(value-> value.compareTo(new LocalDate(сreationDate.getValue().getYear(),сreationDate.getValue().getMonthValue(),сreationDate.getValue().getDayOfMonth()))>0,"Срок действия рецепта не может быть раньше даты создания!")
                .bind(RecipeEntity::getExpirationDate,RecipeEntity::setExpirationDate);

        binder.forField(priority)
                .withValidator(value-> value != null, "Поле не может быть пустым!")
                .bind(RecipeEntity::getPriority,RecipeEntity::setPriority);

    }

    public void setRecipe(RecipeEntity recipe) {
        this.recipe = recipe;
        binder.setBean(recipe);
    }

    private void cancel() {
        setVisible(false);
        recipeView.setRecipeGridVisible();
        recipeView.setToolbarVisible();
    }

    private void save() {
        if (binder.isValid()) {
            try {
                service.saveOrUpdate(recipe);
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("Ошибка сохранения рецепта!");
            }
            setVisible(false);
            recipeView.setRecipeGridVisible();
            recipeView.setToolbarVisible();
        }
        else {
            Notification.show("Введите корректные данные!");
        }
    }

    private void setSelectionsComponents(){
        List<DoctorEntity> doctors = doctorService.findAll();
        List<PatientEntity> patients = patientService.findAll();

        doctorField.setItems(doctors);
        doctorField.setItemCaptionGenerator(DoctorEntity::getName);
        doctorField.setSelectedItem(doctors.get(0));

        patientField.setItems(patients);
        patientField.setItemCaptionGenerator(PatientEntity::getName);
        patientField.setSelectedItem(patients.get(0));


        priority.setItems(Priority.values());
        priority.setItemCaptionGenerator(new ItemCaptionGenerator<Priority>() {
            @Override
            public String apply(Priority priority) {
                return priority.name();
            }
        });

    }
}