package hw11.mycache.api.dao;

import hw11.mycache.api.model.User;
import hw11.mycache.api.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);

    long saveUser(User user);

    SessionManager getSessionManager();
}
