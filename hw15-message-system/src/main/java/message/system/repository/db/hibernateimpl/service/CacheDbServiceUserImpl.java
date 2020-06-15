package message.system.repository.db.hibernateimpl.service;

import lombok.extern.slf4j.Slf4j;
import message.system.repository.cache.HwCache;
import message.system.repository.db.api.model.User;
import message.system.repository.db.api.service.DbServiceUser;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CacheDbServiceUserImpl implements DbServiceUser {

    private DbServiceUser dbServiceUser;
    private HwCache<String, User> userCache;

    public CacheDbServiceUserImpl(DbServiceUser dbServiceUser, HwCache<String, User> cache) {
        this.dbServiceUser = dbServiceUser;
        this.userCache = cache;
    }

    @Override
    public long saveUser(User user) {
        long id = this.dbServiceUser.saveUser(user);
        this.userCache.put(String.valueOf(id), user);

        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        User userFromCache = this.userCache.get(String.valueOf(id));
        if (userFromCache == null) {
            log.debug("Cache is empty, getting user from database");
            Optional<User> optionalUser = this.dbServiceUser.getUser(id);
            optionalUser.ifPresent(user -> this.userCache.put(String.valueOf(id), user));
            return optionalUser;
        }

        return Optional.of(userFromCache);
    }

    @Override
    public List<User> getUsersList() {
        return this.dbServiceUser.getUsersList();
    }

    @Override
    public Optional<User> getUser(String login) {
        User userFromCache = this.userCache.get(login);
        if (userFromCache == null) {
            log.debug("Cache is empty, getting user from database");
            Optional<User> optionalUser = this.dbServiceUser.getUser(login);
            optionalUser.ifPresent(user -> this.userCache.put(login, user));
            return optionalUser;
        }
        return Optional.of(userFromCache);
    }
}
