package diy.orm.dao;

import diy.orm.sessionmanager.SessionManager;

import java.util.Optional;

public interface ObjectDao<T> {

    Optional<T> findById(long id, Class clazz);

    void saveObject(T obj);

    void updateObject(T obj);

    SessionManager getSessionManager();
}
