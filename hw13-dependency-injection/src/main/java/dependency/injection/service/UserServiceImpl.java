package dependency.injection.service;

import dependency.injection.converter.UserConverter;
import dependency.injection.dto.UserDto;
import dependency.injection.repository.db.api.model.User;
import dependency.injection.repository.db.api.service.DbServiceUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private DbServiceUser dbServiceUser;

    private UserConverter userConverter;

    @Override
    public long createUser(UserDto userJson) {
        User user = userConverter.fromUserJson(userJson);
        dbServiceUser.saveUser(user);
        return user.getId();
    }

    @Override
    public Optional<UserDto> loadUser(long id) {
        Optional<User> userOptional = dbServiceUser.getUser(id);
        if (userOptional.isEmpty()) {

            return Optional.empty();
        }
        User user = userOptional.get();
        UserDto userJson = userConverter.toUserJson(user);

        return Optional.of(userJson);
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> userJsons = new ArrayList<>();
        List<User> users = dbServiceUser.getUsersList();
        for (User user : users) {
            userJsons.add(userConverter.toUserJson(user));
        }
        return userJsons;
    }
}
