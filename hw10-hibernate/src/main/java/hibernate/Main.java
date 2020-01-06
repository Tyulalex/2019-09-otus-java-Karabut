package hibernate;

import hibernate.api.dao.UserDao;
import hibernate.api.model.Address;
import hibernate.api.model.Phone;
import hibernate.api.model.User;
import hibernate.api.service.DbServiceUser;
import hibernate.hibernateimpl.HibernateUtils;
import hibernate.hibernateimpl.dao.UserDaoHibernate;
import hibernate.hibernateimpl.service.DbServiceUserImpl;
import hibernate.hibernateimpl.sessionmanager.SessionManagerHibernate;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.Optional;

@Slf4j
public class Main {

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml", User.class, Address.class, Phone.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DbServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        Phone phone = new Phone("+7(123) 123-12-12");
        Address address = new Address();
        User user = new User("Vasya", 12, address);
        user.addPhone(phone);
        address.setId(0);
        address.setStreet("Vavilova street 12-34");

        long id = dbServiceUser.saveUser(user);
        Optional<User> mayBeCreatedUser = dbServiceUser.getUser(id);

        mayBeCreatedUser.ifPresent(createdUser -> log.info("Created User: {}", createdUser));
    }
}
