package diy.orm.service;

import diy.orm.dao.ObjectDao;
import diy.orm.sessionmanager.SessionManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class DbServiceModelImpl<T> implements DbServiceModel {

    private final ObjectDao objectDao;

    public DbServiceModelImpl(ObjectDao objectDao) {
        this.objectDao = objectDao;
    }

    @Override
    public void saveObject(Object obj) {
        try (SessionManager sessionManager = objectDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                objectDao.saveObject(obj);
                sessionManager.commitSession();
                log.info("created object: {}", obj.toString());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<T> getObject(long id, Class clazz) {
        try (SessionManager sessionManager = objectDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<T> object = objectDao.findById(id, clazz);
                log.info("user: {}", object.orElse(null));
                return object;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public void updateObject(Object obj) {
        try (SessionManager sessionManager = objectDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                objectDao.updateObject(obj);
                sessionManager.commitSession();
                log.info("object: {} updated", obj);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }
}
