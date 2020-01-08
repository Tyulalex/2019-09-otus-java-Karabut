package hw11.mycache;

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

import java.util.Optional;

@Slf4j
public class Main {

    public User getDefaultUser(String name) {
        Phone phone = new Phone("+7(123) 123-12-12");
        Address address = new Address();
        User user = new User(name, 12, address);
        user.addPhone(phone);
        address.setId(0);
        address.setStreet("Vavilova street 12-34");

        return user;
    }

    public SessionManagerHibernate buildSessionManager() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml", User.class, Address.class, Phone.class);

        return new SessionManagerHibernate(sessionFactory);
    }

    public void saveAndLoadUser(User user, DbServiceUser dbServiceUser) {
        long id = dbServiceUser.saveUser(user);
        Optional<User> mayBeCreatedUser = dbServiceUser.getUser(id);

        mayBeCreatedUser.ifPresent(createdUser -> log.info("Created User: {}", createdUser));
    }

    public static void main(String[] args) {
        Main main = new Main();
        User user = main.getDefaultUser("Vasya");
        SessionManagerHibernate sessionManager = main.buildSessionManager();
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DbServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        main.saveAndLoadUser(user, dbServiceUser);

        HwCache<String, User> cache = new MyCache<>();
        HwListener<String, User> listener =
                (key, value, action) -> log.info("key:{}, value:{}, action: {}", key, value, action);

        cache.addListener(listener);

        DbServiceUser cacheDbServiceUser = new CacheDbServiceUserImpl(dbServiceUser, cache);

        User anotherUser = main.getDefaultUser("John");
        main.saveAndLoadUser(anotherUser, cacheDbServiceUser);
    }
}
