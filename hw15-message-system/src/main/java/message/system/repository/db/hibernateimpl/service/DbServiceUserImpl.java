package message.system.repository.db.hibernateimpl.service;

import lombok.extern.slf4j.Slf4j;
import message.system.repository.db.api.dao.UserDao;
import message.system.repository.db.api.model.User;
import message.system.repository.db.api.service.DbServiceException;
import message.system.repository.db.api.service.DbServiceUser;

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
        try {
            long userId = userDao.saveUser(user);
            log.info("created user: {}", userId);

            return userId;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DbServiceException(e);
        }
    }


    @Override
    public Optional<User> getUser(long id) {
        try {
            Optional<User> userOptional = userDao.findById(id);
            log.info("user: {}", userOptional.orElse(null));

            return userOptional;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUser(String login) {
        try {
            Optional<User> userOptional = userDao.findByLogin(login);
            log.info("user: {}", userOptional.orElse(null));

            return userOptional;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getUsersList() {
        try {
            List<User> users = userDao.getUsersList();
            log.info("loaded {} users", users.size());

            return users;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
