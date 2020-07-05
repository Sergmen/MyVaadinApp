package com.haulmont.testtask.common;


import org.hibernate.Session;

import javax.persistence.*;

public class ProjectSessionManager {

    private static Session session = null;

    private ProjectSessionManager() { }

    public static Session getSession() {
        if (session==null) {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("PersistenceCore");
            EntityManager em = factory.createEntityManager();
            session = em.unwrap(Session.class);
        }

        return session;
    }
}



