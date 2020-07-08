package task.views.Froms;

import task.common.Priority;
import task.entities.DoctorEntity;
import task.entities.PatientEntity;
import task.entities.RecipeEntity;
import task.service.DoctorService;
import task.service.PatientService;
import task.service.RecipeService;
import task.views.RecipeView;
import com.vaadin.data.Binder;
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

        setSelectionsComponents();
        setBinderValidators();
        save.addClickListener(e -> this.save());
        cancel.addClickListener(e -> this.cancel());

    }

    private void setBinderValidators() {
        binder.forField(description)
                .withValidator(value-> value != null, "Поле не может быть пустым!")
                .bind(RecipeEntity::getDescription,RecipeEntity::setDescription);

        binder.forField(patientField)
                .withValidator(value-> value != null, "Поле не может быть пустым!")
                .bind(RecipeEntity::getPatient,RecipeEntity::setPatient);

        binder.forField(doctorField)
                .withValidator(value-> value != null, "Поле не может быть пустым!")
                .bind(RecipeEntity::getDoctor,RecipeEntity::setDoctor);

        binder.forField(сreationDate)
                .withValidator(value-> value != null, "Поле не может быть пустым!")
                .withConverter(value->value==null?null:new LocalDate(value.getYear(),value.getMonthValue(), value.getDayOfMonth()), modelValue->modelValue==null?null:java.time.LocalDate.ofYearDay(modelValue.getYear(),modelValue.getDayOfYear()), "Введен неверный формат даты!")
                .bind(RecipeEntity::getСreationDate,RecipeEntity::setСreationDate);

        binder.forField(expirationDate)
                .withValidator(value-> value != null, "Поле не может быть пустым!")
                .withValidator(value->сreationDate.getValue()==null?true:(value.compareTo(сreationDate.getValue())>0?true:false), "Срок действия рецепта не может быть раньше дата создания!" )
                .withConverter(value->value==null?null:new LocalDate(value.getYear(),value.getMonthValue(), value.getDayOfMonth()), modelValue->modelValue==null?null:java.time.LocalDate.ofYearDay(modelValue.getYear(),modelValue.getDayOfYear()), "Введен неверный формат даты!")
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
        recipeView.setFilterToolbarVisible();
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
            recipeView.setFilterToolbarVisible();
        }
        else {
            Notification.show("Введите корректные данные!");
        }
    }

    private void setSelectionsComponents(){
        List<DoctorEntity> doctors = doctorService.findAll();
        List<PatientEntity> patients = patientService.findAll();

        doctorField.setItems(doctors);
        doctorField.setItemCaptionGenerator(DoctorEntity::toString);
        doctorField.setSelectedItem(doctors.get(0));

        patientField.setItems(patients);
        patientField.setItemCaptionGenerator(PatientEntity::toString);
        patientField.setSelectedItem(patients.get(0));


        priority.setItems(Priority.values());
        priority.setItemCaptionGenerator(new ItemCaptionGenerator<Priority>() {
            @Override
            public String apply(Priority priority) {
                return priority.name();
            }
        });

    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        setSelectionsComponents();
    }
}