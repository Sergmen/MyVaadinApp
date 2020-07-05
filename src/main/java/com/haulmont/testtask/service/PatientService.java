package com.haulmont.testtask.service;

import com.haulmont.testtask.common.ProjectSessionManager;
import com.haulmont.testtask.entities.PatientEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientService {


    private static PatientService instance;
    private static final Logger LOGGER = Logger.getLogger(PatientService.class.getName());

    private final HashMap<Long, PatientEntity> patients = new HashMap<>();
    private long nextId = 0;

    private PatientService() {
    }


    public static PatientService getInstance() {
        if (instance == null) {
            instance = new PatientService();
        }
        return instance;
    }

    /**
     * @return all available PatientEntity objects.
     */
    public synchronized List<PatientEntity> findAll() {
        Session session = ProjectSessionManager.getSession();
        ArrayList<PatientEntity> patients = (ArrayList<PatientEntity>) session.createNamedQuery("findAll").getResultList();
        return  patients;
    }

    /**
     * Finds all Customer's that match given filter.
     *
     * @param stringFilter
     *            filter that returned objects should match or null/empty string
     *            if all objects should be returned.
     * @return list a Customer objects
     */
    public synchronized List<PatientEntity> findByName(String stringFilter) {
        Session session = ProjectSessionManager.getSession();
        List<PatientEntity> patients = session.createNamedQuery("findByName").setParameter("name",stringFilter).getResultList();
        return patients;
    }



    /**
     * Deletes a patient from a system
     *
     * @param patient
     *            the Patient to be deleted
     */
    public synchronized void delete(PatientEntity patient) throws Exception {

        try {
            Session session = ProjectSessionManager.getSession();
            Transaction tx = session.getTransaction();
            tx.begin();
            session.remove(patient);
            tx.commit();
        }
        catch (Exception e){
            LOGGER.log(Level.SEVERE,"Ошибка удаления пациента:", e);
            throw e;
        }
    }

    /**
     * Persists or updates patient in the system. Also assigns an identifier
     * for new PatientEntity.
     *
     * @param patient
     */
    public synchronized void saveOrUpdate(PatientEntity patient) throws Exception{
        if (patient == null) {
            LOGGER.log(Level.SEVERE,
                    "Заполните данные пациента");
            return;
        }
        try {
            Session session = ProjectSessionManager.getSession();
            Transaction tx = session.getTransaction();
            tx.begin();
            session.saveOrUpdate(patient);
            tx.commit();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE,
                    "Ошибка сохранения/обновления пациента");
            throw ex;
         }
    }


}
