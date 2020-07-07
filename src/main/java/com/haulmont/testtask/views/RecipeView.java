package com.haulmont.testtask.views;


import com.haulmont.testtask.entities.RecipeEntity;
import com.haulmont.testtask.service.RecipeService;
import com.haulmont.testtask.views.Froms.Menu;
import com.haulmont.testtask.views.Froms.RecipeForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.NoSuchElementException;


public class RecipeView extends VerticalLayout implements View {

    private RecipeService recipeService  = RecipeService.getInstance();
    private RecipeForm form = new RecipeForm(this);
    private Grid<RecipeEntity> recipeGrid= new Grid<>(RecipeEntity.class);
    private Label label  = new Label("Рецепты");
    private HorizontalLayout toolbar = new HorizontalLayout();
    private Button addButton = new Button("Добавить");
    private Button editButton = new Button("Изменить");
    private Button deliteButton = new Button("Удалить");

    public RecipeView() {
        setSizeFull();
        setSpacing(true);
        label.setSizeFull();
        label.setStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        label.addStyleName(ValoTheme.LABEL_H2);
        addComponent(label);
        addComponent(new Menu());
        HorizontalLayout main = new HorizontalLayout(recipeGrid, form);
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
        setRecipeGrid();
        setToolbar();
        form.setVisible(false);
        addComponent(main);
        addComponent(toolbar);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        form.setVisible(false);
        setRecipeGridVisible();
        setToolbarVisible();
    }

    private void setToolbar(){
        addButton.addClickListener(e->{
            recipeGrid.setVisible(false);
            toolbar.setVisible(false);
            form.setVisible(true);
            form.setRecipe(new RecipeEntity());
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
                recipeGrid.setVisible(false);
                toolbar.setVisible(false);
                form.setVisible(true);
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
        toolbar.addComponents(addButton,editButton,deliteButton);
    }

    public void setToolbarVisible(){
        toolbar.setVisible(true);
    }

    private void setRecipeGrid() {
        recipeGrid.setItems(recipeService.findAll());

    }
    public void setRecipeGridVisible() {
        setRecipeGrid();
        recipeGrid.setVisible(true);
    }



}
