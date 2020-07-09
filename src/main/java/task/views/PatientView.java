package task.views;

import task.views.Froms.Menu;
import task.views.Froms.PatientForm;
import task.entities.PatientEntity;
import task.service.PatientService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.NoSuchElementException;


public class PatientView extends VerticalLayout implements View {

    private PatientService patientService  = PatientService.getInstance();
    private PatientForm form = new PatientForm(this);
    private Grid<PatientEntity> patientGrid= new Grid<>(PatientEntity.class);
    private Label label  = new Label("Пациенты");
    private HorizontalLayout main = new HorizontalLayout(patientGrid, form);
    private HorizontalLayout toolbar = new HorizontalLayout();
    private Button addButton = new Button("Добавить");
    private Button editButton = new Button("Изменить");
    private Button deliteButton = new Button("Удалить");

    public PatientView() {
        setSizeFull();
        setSpacing(true);
        setLabel();
        setMain();
        setGridFormVisible(true);
        setToolBar();
        toolbar.setVisible(true);
        addComponents(label,new Menu(),main,toolbar);
    }


    private void setGridFormVisible(boolean patientGridVisible) {
        if (patientGridVisible) {
            patientGrid.setVisible(true);
            form.setVisible(false);
        }
        else {
            patientGrid.setVisible(false);
            form.setVisible(true);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setMain();
        setGridFormVisible(true);
        toolbar.setVisible(true);
    }

    private void setMain() {
        main.setSizeFull();
        main.setExpandRatio(patientGrid, 1);
        patientGrid.setColumns("surname", "name", "patronymic", "phone", "id");
        patientGrid.getColumn("name").setCaption("Имя");
        patientGrid.getColumn("surname").setCaption("Фамилия");
        patientGrid.getColumn("patronymic").setCaption("Отчество");
        patientGrid.getColumn("phone").setCaption("Телефон");
        patientGrid.setSizeFull();
        setPatientGrid();
    }

    private void setLabel() {
        label.setSizeFull();
        label.setStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        label.addStyleName(ValoTheme.LABEL_H2);
    }


    private void setToolBar(){
        addButton.addClickListener(e->{
            setGridFormVisible(false);
            toolbar.setVisible(false);
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
                setGridFormVisible(false);
                toolbar.setVisible(false);
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
                } catch (Exception ex) {
                        Notification.show("Ошибка удаления пациента!" );
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
