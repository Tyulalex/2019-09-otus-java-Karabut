package webserver.com.servlet.services;

import webserver.com.db.api.model.RoleMapping;
import webserver.com.db.api.model.User;
import webserver.com.db.api.service.DbServiceUser;
import webserver.com.utils.Encryption;

import java.util.Optional;

public class UserAuthServiceImpl implements UserAuthService {

    private final DbServiceUser dbServiceUser;

    public UserAuthServiceImpl(DbServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public boolean authenticate(String login, String password) {
        Optional<User> user = dbServiceUser.getUser(login);

        if (user.isPresent() && user.get().getRole().equals(RoleMapping.ADMIN)) {
            String passwordDB = user.get().getPassword();
            return passwordDB.equals(Encryption.encrypt(password));
        }
        return false;
    }
}
