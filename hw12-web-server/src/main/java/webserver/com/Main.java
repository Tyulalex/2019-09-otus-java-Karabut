package webserver.com;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import webserver.com.db.api.dao.UserDao;
import webserver.com.db.api.model.Address;
import webserver.com.db.api.model.Phone;
import webserver.com.db.api.model.RoleMapping;
import webserver.com.db.api.model.User;
import webserver.com.db.api.service.DbServiceUser;
import webserver.com.db.hibernateimpl.HibernateUtils;
import webserver.com.db.hibernateimpl.dao.UserDaoHibernate;
import webserver.com.db.hibernateimpl.service.DbServiceUserImpl;
import webserver.com.db.hibernateimpl.sessionmanager.SessionManagerHibernate;
import webserver.com.server.UsersWebServer;
import webserver.com.server.UsersWebServerImpl;
import webserver.com.servlet.services.*;

import java.io.IOException;
import java.util.List;

@Slf4j
public class Main {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";


    public static void main(String[] args) throws IOException {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(
                "hibernate.cfg.xml", User.class, Address.class, Phone.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DbServiceUser dbServiceUser = new DbServiceUserImpl(userDao);
        loadUsersInDb(dbServiceUser);
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        List<User> users = dbServiceUser.getUsersList();
        gson.toJson(users);
        loadAdminUser(dbServiceUser);

        UserService userService = new UserServiceImpl(dbServiceUser);

        UserAuthService userAuthServiceForFilterBasedSecurity = new UserAuthServiceImpl(dbServiceUser);
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        UsersWebServer usersWebServer = new UsersWebServerImpl(WEB_SERVER_PORT, userService, gson,
                templateProcessor, userAuthServiceForFilterBasedSecurity);
        try {
            usersWebServer.start();
            usersWebServer.join();
        } catch (Exception e) {
            log.error("Exception occured {}", e.getMessage());
        }
    }


    private static void loadUsersInDb(DbServiceUser dbServiceUser) {

        User user1 = getDefaultUser("John", "user1");
        User user2 = getDefaultUser("Ted", "user2");
        User user3 = getDefaultUser("Sam", "user3");

        dbServiceUser.saveUser(user1);
        dbServiceUser.saveUser(user2);
        dbServiceUser.saveUser(user3);

        List<User> users = dbServiceUser.getUsersList();

        log.info("Loaded users from db: {}", users.toString());
    }

    private static void loadAdminUser(DbServiceUser dbServiceUser) {
        String login = "admin";
        String password = "1234";
        User admin = buildAdminUser("Admin", login, password);
        dbServiceUser.saveUser(admin);
        log.info("Admin user inserted with login {} and password {}", login, password);
    }


    private static User getDefaultUser(String name, String login) {
        Phone phone = new Phone("+7(123) 123-12-12");
        Address address = new Address();
        User user = User.builder()
                .name(name)
                .age(12)
                .address(address)
                .password("123")
                .role(RoleMapping.WEBUSER)
                .login(login)
                .build();
        user.addPhone(phone);
        address.setId(0);
        address.setStreet("Vavilova street 12-34");
        return user;
    }

    private static User buildAdminUser(String name, String login, String password) {
        return User.builder()
                .name(name)
                .login(login)
                .password(password)
                .role(RoleMapping.ADMIN).build();
    }
}
