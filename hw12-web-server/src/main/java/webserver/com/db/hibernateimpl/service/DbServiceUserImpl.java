package webserver.com.db.hibernateimpl.service;

import lombok.extern.slf4j.Slf4j;
import webserver.com.db.api.dao.UserDao;
import webserver.com.db.api.model.User;
import webserver.com.db.api.service.DbServiceException;
import webserver.com.db.api.service.DbServiceUser;
import webserver.com.db.api.sessionmanager.SessionManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DbServiceUserImpl implements DbServiceUser {

    private final UserDao userDao;

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long userId = userDao.saveUser(user);
                sessionManager.commitSession();
                log.info("created user: {}", userId);

                return userId;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);
                log.info("user: {}", userOptional.orElse(null));

                return userOptional;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUser(String login) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findByLogin(login);
                log.info("user: {}", userOptional.orElse(null));

                return userOptional;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> getUsersList() {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                List<User> users = userDao.getUsersList();
                sessionManager.commitSession();
                log.info("loaded {} users", users.size());

                return users;

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
        }
        return Collections.emptyList();
    }
}
