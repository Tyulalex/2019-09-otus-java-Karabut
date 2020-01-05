package hibernate.hibernateimpl.service;

import hibernate.api.dao.UserDao;
import hibernate.api.model.User;
import hibernate.api.service.DbServiceException;
import hibernate.api.service.DbServiceUser;
import hibernate.api.sessionmanager.SessionManager;
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
