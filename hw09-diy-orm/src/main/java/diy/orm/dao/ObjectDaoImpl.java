package diy.orm.dao;

import diy.orm.jdbc.JdbcTemplate;
import diy.orm.sessionmanager.SessionManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.Optional;

@Slf4j
public class ObjectDaoImpl implements ObjectDao {

    private final SessionManager sessionManager;
    private final JdbcTemplate jdbcTemplate;

    public ObjectDaoImpl(SessionManager sessionManager, JdbcTemplate jdbcTemplate) {
        this.sessionManager = sessionManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional findById(long id, Class clazz) {
        try {
            return jdbcTemplate.load(getConnection(), id, clazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ObjectDaoException(e);
        }
    }

    @Override
    public void saveObject(Object obj) {
        try {
            jdbcTemplate.create(getConnection(), obj);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ObjectDaoException(e);
        }
    }

    @Override
    public void updateObject(Object obj) {
        try {
            jdbcTemplate.update(getConnection(), obj);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ObjectDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }

    private Connection getConnection() {
        return getSessionManager().getCurrentSession().getConnection();
    }
}
