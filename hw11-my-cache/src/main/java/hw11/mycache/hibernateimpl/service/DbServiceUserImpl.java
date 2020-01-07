package hw11.mycache.hibernateimpl.service;

import hw11.mycache.api.dao.UserDao;
import hw11.mycache.api.model.User;
import hw11.mycache.api.service.DbServiceException;
import hw11.mycache.api.service.DbServiceUser;
import hw11.mycache.api.sessionmanager.SessionManager;
import lombok.extern.slf4j.Slf4j;

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
}
