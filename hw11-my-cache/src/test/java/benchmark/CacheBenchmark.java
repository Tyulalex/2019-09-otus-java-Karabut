package benchmark;

import hw11.mycache.api.dao.UserDao;
import hw11.mycache.api.model.Address;
import hw11.mycache.api.model.Phone;
import hw11.mycache.api.model.User;
import hw11.mycache.api.service.DbServiceUser;
import hw11.mycache.cache.HwCache;
import hw11.mycache.cache.HwListener;
import hw11.mycache.cache.MyCache;
import hw11.mycache.hibernateimpl.HibernateUtils;
import hw11.mycache.hibernateimpl.dao.UserDaoHibernate;
import hw11.mycache.hibernateimpl.service.CacheDbServiceUserImpl;
import hw11.mycache.hibernateimpl.service.DbServiceUserImpl;
import hw11.mycache.hibernateimpl.sessionmanager.SessionManagerHibernate;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@State(value = Scope.Thread)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CacheBenchmark {

    DbServiceUser dbServiceUserWithOutCache;
    DbServiceUser dbServiceUserWithCache;
    User user1;
    User user2;

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(CacheBenchmark.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }


    @Setup
    public void setup() throws Exception {
        user1 = this.getDefaultUser("Vasya");
        user2 = this.getDefaultUser("Vasya");
        SessionManagerHibernate sessionManager = this.buildSessionManager();
        UserDao userDao = new UserDaoHibernate(sessionManager);
        dbServiceUserWithOutCache = new DbServiceUserImpl(userDao);

        HwCache<String, User> cache = new MyCache<>();
        HwListener<String, User> listener =
                (key, value, action) -> log.info("key:{}, value:{}, action: {}", key, value, action);

        cache.addListener(listener);
        dbServiceUserWithCache = new CacheDbServiceUserImpl(dbServiceUserWithOutCache, cache);

    }

    @Benchmark
    public void dbServiceWithoutCacheSaveAndLoadUser() {
        this.saveAndLoadUser(user1, dbServiceUserWithOutCache);
    }

    @Benchmark
    public void dbServiceWithCacheSaveAndLoadUser() {
        this.saveAndLoadUser(user2, dbServiceUserWithCache);
    }

    public SessionManagerHibernate buildSessionManager() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate-test.cfg.xml", User.class, Address.class, Phone.class);

        return new SessionManagerHibernate(sessionFactory);
    }

    public User getDefaultUser(String name) {
        Phone phone = new Phone("+7(123) 123-12-12");
        Address address = new Address();
        User user = new User(name, 12, address);
        user.addPhone(phone);
        address.setId(0);
        address.setStreet("Vavilova street 12-34");

        return user;
    }

    public void saveAndLoadUser(User user, DbServiceUser dbServiceUser) {
        long id = dbServiceUser.saveUser(user);
        Optional<User> mayBeCreatedUser = dbServiceUser.getUser(id);

        mayBeCreatedUser.ifPresent(createdUser -> log.info("Created User: {}", createdUser));
    }
}
