package webserver.com.servlet.services;

import webserver.com.db.api.model.Address;
import webserver.com.db.api.model.Phone;
import webserver.com.db.api.model.RoleMapping;
import webserver.com.db.api.model.User;
import webserver.com.db.api.service.DbServiceUser;
import webserver.com.servlet.dto.UserJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private DbServiceUser dbServiceUser;

    public UserServiceImpl(DbServiceUser serviceUser) {
        this.dbServiceUser = serviceUser;
    }

    @Override
    public long createUser(UserJson userJson) {
        User user = fromUserJson(userJson);
        dbServiceUser.saveUser(user);
        return user.getId();
    }

    @Override
    public Optional<UserJson> loadUser(long id) {
        Optional<User> userOptional = dbServiceUser.getUser(id);
        if (userOptional.isEmpty()) {

            return Optional.empty();
        }
        User user = userOptional.get();
        UserJson userJson = toUserJson(user);

        return Optional.of(userJson);
    }

    @Override
    public List<UserJson> getUsers() {
        List<UserJson> userJsons = new ArrayList<>();
        List<User> users = dbServiceUser.getUsersList();
        for (User user : users) {
            userJsons.add(toUserJson(user));
        }
        return userJsons;
    }

    private UserJson toUserJson(User user) {
        UserJson userJson = UserJson
                .builder()
                .name(user.getName())
                .age(user.getAge())
                .login(user.getLogin())
                .build();
        UserJson.AddressJson addressJson = new UserJson.AddressJson(user.getAddress().getStreet());
        userJson.setAddress(addressJson);
        for (Phone phone : user.getPhones()) {
            UserJson.PhoneJson phoneJson = new UserJson.PhoneJson(phone.getNumber());
            userJson.addPhone(phoneJson);
        }
        return userJson;
    }

    private User fromUserJson(UserJson userJson) {
        Address address = Address.builder().street(userJson.getAddress().getStreet()).build();
        User user = User.builder()
                .address(address)
                .age(userJson.getAge())
                .name(userJson.getName())
                .role(RoleMapping.WEBUSER)
                .login(userJson.getLogin())
                .password(userJson.getPassword())
                .build();
        for (UserJson.PhoneJson phoneJson : userJson.getPhones()) {
            Phone phone = Phone.builder().number(phoneJson.getNumber()).build();
            user.addPhone(phone);
        }
        return user;
    }
}
