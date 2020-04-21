package dependency.injection.repository.db.api.service;

import dependency.injection.repository.db.api.model.User;

import java.util.List;
import java.util.Optional;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getUsersList();

    Optional<User> getUser(String login);

}
