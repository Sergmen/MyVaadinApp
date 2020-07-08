package task.views.Froms;
import task.common.Validators;
import com.vaadin.data.validator.*;
import task.entities.PatientEntity;
import task.service.PatientService;
import task.views.PatientView;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class PatientForm extends FormLayout {

    private TextField name = new TextField("Имя");
    private TextField surname = new TextField("Фамилия");
    private TextField patronymic = new TextField("Отчество");
    private TextField phone = new TextField("Телефон");
    private Button save = new Button("Сохранить");
    private Button cancel = new Button("Отмена");

    private PatientService service = PatientService.getInstance();
    private PatientEntity patient;
    private PatientView patientView;
    private Binder<PatientEntity> binder = new Binder<>(PatientEntity.class);

    public PatientForm(PatientView patientView) {
        this.patientView = patientView;

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        addComponents(surname,name,patronymic, phone, buttons);


        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(KeyCode.ENTER);

        setBinderValidators();
        save.addClickListener(e -> this.save());
        cancel.addClickListener(e -> this.cancel());


    }

    private void setBinderValidators() {
        binder.forField(name)
                .withValidator(Validators.textLenghtValidator)
                .withValidator(new RegexpValidator("Имя должно состоять из русских букв!","^[А-Яа-я]+$"))
                .bind(PatientEntity::getName,PatientEntity::setName);

        binder.forField(surname)
                .withValidator(Validators.textLenghtValidator)
                .withValidator(new RegexpValidator("Имя должно состоять из русских букв!","^[А-Яа-я]+$"))
                .bind(PatientEntity::getSurname,PatientEntity::setSurname);

        binder.forField(patronymic)
                .withValidator(Validators.textLenghtValidator)
                .withValidator(new RegexpValidator("Имя должно состоять из русских букв!","^[А-Яа-я]+$"))
                .bind(PatientEntity::getPatronymic,PatientEntity::setPatronymic);

        binder.forField(phone)
                .withValidator(Validators.textLenghtValidator)
                .withValidator(new RegexpValidator("Введите номер телефона в формате 8XXXXXXXXXX","^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{10,10}$"))
                .bind(PatientEntity::getPhone,PatientEntity::setPhone);

    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
        binder.setBean(patient);
        name.selectAll();
    }

    private void cancel() {
        setVisible(false);
        patientView.setPatientGridVisible();
        patientView.setToolbarVisible();
    }

    private void save() {
        if (binder.isValid()) {
            try {
                service.saveOrUpdate(patient);
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show("Ошибка сохранения пациента!");
            }
            setVisible(false);
            patientView.setPatientGridVisible();
            patientView.setToolbarVisible();
        }
        else {
            Notification.show("Введите корректные данные!");
        }
    }

}
