package hw11.mycache.hibernateimpl.service;

import hw11.mycache.api.model.User;
import hw11.mycache.api.service.DbServiceUser;
import hw11.mycache.cache.HwCache;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class CacheDbServiceUserImpl implements DbServiceUser {

    private DbServiceUser dbServiceUser;
    private HwCache<Long, Optional<User>> userCache;

    public CacheDbServiceUserImpl(DbServiceUser dbServiceUser, HwCache cache) {
        this.dbServiceUser = dbServiceUser;
        this.userCache = cache;
    }

    @Override
    public long saveUser(User user) {
        long id = this.dbServiceUser.saveUser(user);
        this.userCache.put(id, Optional.of(user));
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        Optional<User> mayBeUser = this.userCache.get(id);
        if (mayBeUser.isEmpty()) {
            log.debug("Cache is empty, getting user from database");
            return this.dbServiceUser.getUser(id);
        }
        return mayBeUser;
    }
}
