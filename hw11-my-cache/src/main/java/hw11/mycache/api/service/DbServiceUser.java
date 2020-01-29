package hw11.mycache.api.service;


import hw11.mycache.api.model.User;

import java.util.Optional;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

}
