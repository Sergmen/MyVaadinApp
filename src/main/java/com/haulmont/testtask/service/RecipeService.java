package com.haulmont.testtask.service;

import com.haulmont.testtask.common.ProjectSessionManager;
import com.haulmont.testtask.entities.RecipeEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecipeService {

    private static RecipeService instance;
    private static final Logger LOGGER = Logger.getLogger(RecipeService.class.getName());
    private Session session = ProjectSessionManager.getSession();

    private RecipeService() {
    }


    public static RecipeService getInstance() {
        if (instance == null) {
            instance = new RecipeService();
        }
        return instance;
    }


    public synchronized List<RecipeEntity> findAll() {
        ArrayList<RecipeEntity> recipes = (ArrayList<RecipeEntity>) session.createNamedQuery("findAllRecipes").getResultList();
        return  recipes;
    }


    public synchronized void delete(RecipeEntity recipe) throws Exception {

        try {
            Transaction tx = session.getTransaction();
            tx.begin();
            session.remove(recipe);
            tx.commit();
        }
        catch (Exception e){
            LOGGER.log(Level.SEVERE,"Ошибка удаления рецепта:", e);
            throw e;
        }
    }


    public synchronized void saveOrUpdate(RecipeEntity recipe) throws Exception{
        if (recipe == null) {
            LOGGER.log(Level.SEVERE,
                    "Заполните данные в рецепте");
            return;
        }
        try {
            Transaction tx = session.getTransaction();
            tx.begin();
            session.saveOrUpdate(recipe);
            tx.commit();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Ошибка сохранения/обновления рецепта", ex.getMessage());
            throw ex;
        }
    }


}
