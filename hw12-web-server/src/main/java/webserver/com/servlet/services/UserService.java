package webserver.com.servlet.services;

import webserver.com.servlet.dto.UserJson;

import java.util.List;
import java.util.Optional;

public interface UserService {

    long createUser(UserJson userJson);

    Optional<UserJson> loadUser(long id);

    List<UserJson> getUsers();
}
