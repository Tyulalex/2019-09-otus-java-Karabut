package webserver.com.db.api.dao;

import webserver.com.db.api.model.User;
import webserver.com.db.api.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);

    long saveUser(User user);

    SessionManager getSessionManager();

    List<User> getUsersList();

    Optional<User> findByLogin(String login);
}
