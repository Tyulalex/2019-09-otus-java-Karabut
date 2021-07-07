package message.system.service;

import message.system.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    long createUser(UserDto userJson);

    Optional<UserDto> loadUser(long id);

    List<UserDto> getUsers();
}
