package com.haulmont.testtask.service;

import com.haulmont.testtask.common.ProjectSessionManager;
import com.haulmont.testtask.entities.PatientEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientService {


    private static PatientService instance;
    private static final Logger LOGGER = Logger.getLogger(PatientService.class.getName());
    private Session session = ProjectSessionManager.getSession();

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
        ArrayList<PatientEntity> patients = (ArrayList<PatientEntity>) session.createNamedQuery("findAllPatients").getResultList();
        return  patients;
    }


    /**
     * Deletes a patient from a system
     *
     * @param patient
     *            the Patient to be deleted
     */
    public synchronized void delete(PatientEntity patient) throws Exception {

        try {
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
