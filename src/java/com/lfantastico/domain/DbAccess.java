package com.lfantastico.domain;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class DbAccess {
    private static final Logger LOG = LoggerFactory.getLogger(DbAccess.class);

    private static final SessionFactory FACTORY;
    private static final ThreadLocal<DbAccess> current
            = new ThreadLocal<DbAccess>();

    private Session session;
    private Transaction tx;

    public static DbAccess get() {
        return current.get();
    }

    public static DbAccess open() {
        if (current.get() != null) {
            throw new IllegalStateException(
                    "Illegal attempt to create a DB connection");
        }
        DbAccess db = new DbAccess();
        current.set(db);
        return db;
    }

    private DbAccess() {
    }

    public void commit() {
        if (tx != null) {
            try {
                if (tx.isActive()) {
                    tx.commit();
                }
            } finally {
                tx = null;
            }
        }
    }

    public void close() {
        if (current.get() != this) {
            throw new IllegalStateException(
                    "Illegal attempt to close a DB connection");
        }
        current.set(null);
        if (tx != null) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } finally {
                tx = null;
            }
        }
        if (session != null) {
            LOG.info("Close session");
            try {
                session.close();
            } finally {
                session = null;
            }
        }
    }

    public <T,I extends Serializable> T get(Class<T> clazz, I id) {
        @SuppressWarnings("unchecked")
        T result = (T)getSession().get(clazz, id);
        return result;
    }

    public <T> List<T> getAll(Class<T> clazz) {
        Criteria criteria = getSession().createCriteria(clazz);
        @SuppressWarnings("unchecked")
        List<T> result = (List<T>)criteria.list();
        return result;
    }

    public void save(Object obj) {
        getSession().save(obj);
    }

    public void update(Object obj) {
        getSession().update(obj);
    }

    public void saveOrUpdate(Object obj) {
        getSession().saveOrUpdate(obj);
    }

    public void merge(Object obj) {
        getSession().merge(obj);
    }

    public Query createQuery(String qry) {
        return getSession().createQuery(qry);
    }

    public Criteria createCriteria(Class<?> clazz) {
        return getSession().createCriteria(clazz);
    }

    private Session getSession() {
        if (session == null) {
            LOG.info("Creating session");
            session = FACTORY.openSession();
            tx = session.beginTransaction();
        }
        return session;
    }

    static {
        try {
            // Create the SessionFactory from hibernate.xml
            Configuration cfg = new Configuration();
            cfg.configure("hibernate.xml");
            FACTORY = cfg.buildSessionFactory();
        } catch (Throwable e) {
            LOG.error("Initial SessionFactory creation failed.", e);
            throw new ExceptionInInitializerError(e);
        }
    }
}
