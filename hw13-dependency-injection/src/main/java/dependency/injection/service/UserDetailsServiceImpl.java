package dependency.injection.service;

import dependency.injection.dto.LoginDto;
import dependency.injection.repository.db.api.model.RoleMapping;
import dependency.injection.repository.db.api.model.User;
import dependency.injection.repository.db.api.service.DbServiceUser;
import dependency.injection.utils.Encryption;
import lombok.Getter;
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
