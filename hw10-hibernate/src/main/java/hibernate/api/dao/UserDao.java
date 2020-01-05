package hibernate.api.dao;

import hibernate.api.model.User;
import hibernate.api.sessionmanager.SessionManager;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);

    long saveUser(User user);

    SessionManager getSessionManager();
}
