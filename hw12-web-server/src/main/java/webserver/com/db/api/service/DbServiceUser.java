package webserver.com.db.api.service;


import webserver.com.db.api.model.User;

import java.util.List;
import java.util.Optional;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getUsersList();

    Optional<User> getUser(String login);

}
