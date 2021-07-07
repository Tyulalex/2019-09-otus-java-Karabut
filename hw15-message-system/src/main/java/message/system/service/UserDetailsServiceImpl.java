package message.system.service;

import lombok.Getter;
import message.system.dto.LoginDto;
import message.system.repository.db.api.model.RoleMapping;
import message.system.repository.db.api.model.User;
import message.system.repository.db.api.service.DbServiceUser;
import message.system.utils.Encryption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DbServiceUser dbServiceUser;

    public UserDetailsServiceImpl(@Qualifier("cacheDbServiceUser") DbServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    public boolean authenticate(LoginDto loginDto) {
        Optional<User> user = dbServiceUser.getUser(loginDto.getUsername());

        if (user.isPresent() && user.get().getRole().equals(RoleMapping.ADMIN)) {
            String passwordDB = user.get().getPassword();
            return passwordDB.equals(Encryption.encrypt(loginDto.getPassword()));
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = this.dbServiceUser.getUser(login);
        if (user.isPresent()) {
            User realUser = user.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .password(realUser.getPassword())
                    .username(realUser.getLogin())
                    .roles(realUser.getRole().name())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
