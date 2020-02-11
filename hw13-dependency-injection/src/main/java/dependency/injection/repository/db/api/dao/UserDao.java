package dependency.injection.repository.db.api.dao;

import dependency.injection.repository.db.api.model.User;
import dependency.injection.repository.db.api.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long saveUser(User user);

    SessionManager getSessionManager();

    List<User> getUsersList();

    Optional<User> findByLogin(String login);
}
