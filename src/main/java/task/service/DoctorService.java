package task.service;


import task.common.DoctorStatistics;
import task.common.ProjectSessionManager;
import task.entities.DoctorEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoctorService {
    private static DoctorService instance;
    private static final Logger LOGGER = Logger.getLogger(DoctorService.class.getName());
    private Session session = ProjectSessionManager.getSession();

    private DoctorService() {
    }


    public static DoctorService getInstance() {
        if (instance == null) {
            instance = new DoctorService();
        }
        return instance;
    }


    public synchronized List<DoctorEntity> findAll() {
        ArrayList<DoctorEntity> doctorsList = (ArrayList<DoctorEntity>) session.createNamedQuery("findAllDoctors").getResultList();
        return  doctorsList;
    }

    public synchronized List<DoctorStatistics> getDoctorStatistics() {

        List<DoctorStatistics> statistics = new ArrayList<>();
        Query query = session.createNativeQuery(getSqlDoctorStatistics());

        try {
            List<Object[]> result = query.getResultList();

            for (Object[] el : result) {
                DoctorStatistics doctorStatistics = new DoctorStatistics();
                doctorStatistics.setName((el[0] != null) ? (String)el[0] : null);
                doctorStatistics.setRecipesNumber((el[1] != null) ? (BigInteger)el[1] : BigInteger.valueOf(0));
                statistics.add(doctorStatistics);
            }
        }
        catch (Exception e){
            LOGGER.log(Level.SEVERE,"Ошибка получения статистики по врачу:", e.getStackTrace());
        }

        return statistics;
    }



    public synchronized void delete(DoctorEntity doctor) throws Exception {
        try {
            Transaction tx = session.getTransaction();
            tx.begin();
            session.remove(doctor);
            tx.commit();
        }
        catch (Exception e){
                LOGGER.log(Level.SEVERE, "Ошибка удаления врача!", e);
                throw e;
            }
    }


    public synchronized void saveOrUpdate(DoctorEntity doctor) throws Exception{
        if (doctor == null) {
            LOGGER.log(Level.SEVERE, "Заполните данные врача");
            return;
        }
        try {
            Transaction tx = session.getTransaction();
            tx.begin();
            session.saveOrUpdate(doctor);
            tx.commit();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Ошибка сохранения/обновления врача", e.getStackTrace());
            throw e;
        }
    }


    private String getSqlDoctorStatistics(){
        return "Select \n" +
                " CONCAT(doc.surname,' ',doc.name,'(',CAST(doc.id as varchar(255)),')')\n" +
                ",COUNT(rec.id)\n" +
                "FROM doctor AS doc\n" +
                "LEFT JOIN recipe AS rec ON doc.id = rec.doctor_id\n" +
                "GROUP BY CONCAT(doc.surname,' ',doc.name,'(',CAST(doc.id as varchar(255)),')')";
    }
}
