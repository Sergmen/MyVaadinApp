package task.views;


import task.common.Priority;
import task.entities.PatientEntity;
import task.entities.RecipeEntity;
import task.service.PatientService;
import task.service.RecipeService;
import task.views.Froms.Menu;
import task.views.Froms.RecipeForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


public class RecipeView extends VerticalLayout implements View {

    private RecipeService recipeService  = RecipeService.getInstance();
    private PatientService patientService = PatientService.getInstance();

    private Label label  = new Label("Рецепты");


    private ComboBox<PatientEntity> filterByPatient = new ComboBox<>("Фильтр по пациентам");
    private ComboBox<Priority> filterByPriority = new ComboBox<>("Фильтр по приоритету");
    private TextField filterByDescription = new TextField("Фильтр по описанию");
    private Button filterButton = new Button("Применить фильтр");
    private HorizontalLayout filterToolbar = new HorizontalLayout(filterByPatient, filterByPriority,filterByDescription, filterButton);

    private RecipeForm form = new RecipeForm(this);
    private Grid<RecipeEntity> recipeGrid= new Grid<>(RecipeEntity.class);
    HorizontalLayout main = new HorizontalLayout(recipeGrid, form);


    private Button addButton = new Button("Добавить");
    private Button editButton = new Button("Изменить");
    private Button deliteButton = new Button("Удалить");
    private HorizontalLayout toolbar = new HorizontalLayout(addButton,editButton,deliteButton);

    public RecipeView() {

        setSizeFull();
        setSpacing(true);
        setLabel();
        setMain();
        setGridFormVisible(true);
        setToolBar();
        setFilterToolbar();
        addComponents(label,new Menu(),filterToolbar, main,toolbar);
    }



    private void setGridFormVisible(boolean recipeGridVisible) {
            if (recipeGridVisible) {
                recipeGrid.setVisible(true);
                form.setVisible(false);
            }
            else {
                recipeGrid.setVisible(false);
                form.setVisible(true);
            }
    }

    private void setMain() {
        main.setSizeFull();
        main.setExpandRatio(recipeGrid, 1);
        recipeGrid.setColumns("description", "patient", "doctor", "сreationDate", "expirationDate", "priority", "id");
        recipeGrid.getColumn("description").setCaption("Описание");
        recipeGrid.getColumn("patient").setCaption("Пациент");
        recipeGrid.getColumn("doctor").setCaption("Врач");
        recipeGrid.getColumn("сreationDate").setCaption("Дата создания");
        recipeGrid.getColumn("expirationDate").setCaption("Срок действия");
        recipeGrid.getColumn("priority").setCaption("Приоритет");
        recipeGrid.setSizeFull();
        recipeGrid.setItems(recipeService.findAll());
    }

    private void setLabel() {
        label.setSizeFull();
        label.setStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        label.addStyleName(ValoTheme.LABEL_H2);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setMain();
        setFilterToolbar();
        setGridFormVisible(true);
        toolbar.setVisible(true);
        filterToolbar.setVisible(true);
    }

    private void setToolBar(){
        addButton.addClickListener(e->{
            form.setRecipe(new RecipeEntity());
            setGridFormVisible(false);
            filterToolbar.setVisible(false);
            toolbar.setVisible(false);
        });
        editButton.addClickListener(e->{
            RecipeEntity recipeEntity;
            try {
                recipeEntity=recipeGrid.getSelectedItems().stream().findFirst().get();
            }
            catch (NoSuchElementException ex){
                Notification.show("Выбирите элемент для редактирования!");
                recipeEntity = null;
            }
            if (recipeEntity!=null) {
                setGridFormVisible(false);
                filterToolbar.setVisible(false);
                toolbar.setVisible(false);
                form.setRecipe(recipeEntity);
            }

        });
        deliteButton.addClickListener(e->{
            RecipeEntity recipeEntity;
            try {
                recipeEntity=recipeGrid.getSelectedItems().stream().findFirst().get();
            }
            catch (NoSuchElementException ex){
                Notification.show("Выбирите элемент для удаления!");
                recipeEntity = null;
            }
            if (recipeEntity!=null) {
                try {
                    recipeService.delete(recipeEntity);
                } catch (Exception e1) {
                    Notification.show("Ошибка удаления рецепта!");
                }
                setRecipeGrid();
            }
        });

    }

    private void setRecipeGrid() {
        recipeGrid.setItems(recipeService.findAll());
    }

    public void setToolbarVisible(){
        toolbar.setVisible(true);
    }


    public void setRecipeGridVisible() {
        setRecipeGrid();
        recipeGrid.setVisible(true);
    }


    public void setFilterToolbarVisible() {
        filterToolbar.setVisible(true);
    }

    private void setFilterToolbar() {
        List<PatientEntity> patients = patientService.findAll();
        filterByPatient.setItems(patients);
        filterByPatient.setItemCaptionGenerator(PatientEntity::toString);

        filterByPriority.setItems(Priority.values());
        filterByPriority.setItemCaptionGenerator(new ItemCaptionGenerator<Priority>() {
            @Override
            public String apply(Priority priority) {
                return priority.name();
            }
        });

        filterButton.addClickListener(e->{

            List<RecipeEntity> filteredRecipeGrid = recipeService.findAll().stream()
                    .filter(recipe ->filterByPatient.getValue()==null?true:recipe.getPatient().equals(filterByPatient.getValue()))
                    .filter(recipe ->filterByPriority.getValue()==null?true:recipe.getPriority().equals(filterByPriority.getValue()))
                    .filter(recipe ->filterByDescription.getValue()==null?true:recipe.getDescription().toLowerCase().contains(filterByDescription.getValue().toLowerCase()))
                    .collect(Collectors.toList());


            recipeGrid.setItems(filteredRecipeGrid);
        });

        filterToolbar.setComponentAlignment(filterButton, Alignment.BOTTOM_CENTER);
    }
}

