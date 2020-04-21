package dependency.injection.service;

import dependency.injection.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    long createUser(UserDto userJson);

    Optional<UserDto> loadUser(long id);

    List<UserDto> getUsers();
}
