package message.system.repository.db.api.dao;

import message.system.repository.db.api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long saveUser(User user);

    List<User> getUsersList();

    Optional<User> findByLogin(String login);
}
