package hw11.mycache.hibernateimpl.service;

import hw11.mycache.api.model.User;
import hw11.mycache.api.service.DbServiceUser;
import hw11.mycache.cache.HwCache;
import lombok.extern.slf4j.Slf4j;

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
            return this.dbServiceUser.getUser(id);
        }

        return Optional.of(userFromCache);
    }
}
