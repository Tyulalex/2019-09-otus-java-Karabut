package hibernate.hibernateimpl.dao;

import hibernate.api.model.Address;
import hibernate.api.model.Phone;
import hibernate.api.model.User;
import hibernate.hibernateimpl.HibernateUtils;
import hibernate.hibernateimpl.sessionmanager.SessionManagerHibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoHibernateTest {

    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate-test.cfg.xml";


    private SessionFactory sessionFactory;
    private SessionManagerHibernate sessionManagerHibernate;
    private UserDaoHibernate userDaoHibernate;


    @BeforeEach
    public void setUp() {
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE,
                User.class, Address.class, Phone.class);
        sessionManagerHibernate = new SessionManagerHibernate(sessionFactory);
        userDaoHibernate = new UserDaoHibernate(sessionManagerHibernate);

    }

    @DisplayName("UserDao should save User and its property Address and Phone")
    @Test
    void shouldSaveUser() {

        User expectedUser = buildDefaultUser();
        sessionManagerHibernate.beginSession();
        long id = userDaoHibernate.saveUser(expectedUser);
        sessionManagerHibernate.commitSession();

        assertThat(id).isEqualTo(1);

        User loadedUser = loadUser(id);

        assertThat(loadedUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", expectedUser.getName());
        assertThat(loadedUser.getPhones().size()).isEqualTo(2);
        assertThat(loadedUser.getPhones().get(0)).isEqualTo(expectedUser.getPhones().get(0));
        assertThat(loadedUser.getPhones().get(1)).isEqualTo(expectedUser.getPhones().get(1));
        assertThat(loadedUser.getAddress()).isEqualTo(expectedUser.getAddress());

        expectedUser.setAge(23);
        sessionManagerHibernate.beginSession();
        long newId = userDaoHibernate.saveUser(expectedUser);
        sessionManagerHibernate.commitSession();

        assertThat(newId).isEqualTo(id);

        User reLoadedUser = loadUser(newId);
        assertThat(reLoadedUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("age", expectedUser.getAge());
    }

    @DisplayName("UserDao should load User with addresses and phones")
    @Test
    void loadUser() {
        User expectedUser = buildDefaultUser();

        saveUser(expectedUser);

        assertThat(expectedUser.getId()).isGreaterThan(0);

        sessionManagerHibernate.beginSession();
        Optional<User> mayBeUser = userDaoHibernate.findById(expectedUser.getId());
        sessionManagerHibernate.commitSession();
        assertThat(mayBeUser).isPresent().get()
                .hasFieldOrPropertyWithValue("name", expectedUser.getName());
        assertThat(mayBeUser.get().getPhones().size()).isEqualTo(2);
        assertThat(mayBeUser.get().getPhones().get(0)).isEqualTo(expectedUser.getPhones().get(0));
        assertThat(mayBeUser.get().getPhones().get(1)).isEqualTo(expectedUser.getPhones().get(1));
        assertThat(mayBeUser.get().getAddress()).isEqualTo(expectedUser.getAddress());
    }


    private User loadUser(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(User.class, id);
        }
    }


    @AfterEach
    void tearDown() {
        sessionFactory.close();
    }


    private void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            saveUser(session, user);
        }
    }

    private void saveUser(Session session, User user) {
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
    }

    private User buildDefaultUser() {
        Address address = new Address(0, "street name");
        Phone phone = Phone.builder().number("3434343").build();
        Phone phone1 = Phone.builder().number("132wde3").build();
        User expectedUser = new User("John", 12, address);
        expectedUser.addPhone(phone);
        expectedUser.addPhone(phone1);
        return expectedUser;
    }

}