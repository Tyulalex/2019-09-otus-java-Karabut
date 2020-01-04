package diy.orm;

import diy.orm.dao.ObjectDao;
import diy.orm.dao.ObjectDaoImpl;
import diy.orm.h2.DataSourceH2;
import diy.orm.jdbc.DbExecutor;
import diy.orm.jdbc.DbExecutorImpl;
import diy.orm.jdbc.JdbcTemplate;
import diy.orm.jdbc.JdbcTemplateImpl;
import diy.orm.model.Account;
import diy.orm.model.User;
import diy.orm.service.DbServiceModel;
import diy.orm.service.DbServiceModelImpl;
import diy.orm.sessionmanager.SessionManager;
import diy.orm.sessionmanager.impl.SessionManagerImpl;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class Main {

    public static String CREATE_USER_TABLE_SQL = "create table User(id bigint(20) NOT NULL auto_increment," +
            " name varchar(255), age int(3))";

    public static String CREATE_ACCOUNT_TABLE_SQL = "create table Account(no bigint(20) NOT NULL auto_increment, " +
            " type varchar(255), rest number);";

    public static void main(String[] args) throws Exception {
        DataSource dataSource = new DataSourceH2();
        Main demo = new Main();

        demo.executeSql(dataSource, CREATE_USER_TABLE_SQL);
        demo.executeSql(dataSource, CREATE_ACCOUNT_TABLE_SQL);
        SessionManager sessionManager = new SessionManagerImpl(dataSource);
        DbExecutor dbExecutor = new DbExecutorImpl();

        JdbcTemplate<Object> jdbcTemplate = new JdbcTemplateImpl<>(dbExecutor);
        ObjectDao objectDao = new ObjectDaoImpl(sessionManager, jdbcTemplate);
        DbServiceModel dbServiceModel = new DbServiceModelImpl(objectDao);


        User user1 = new User("dbServiceUser", 13);
        dbServiceModel.saveObject(user1);
        log.info("Save object with ID {}", user1.getId());
        Optional<User> loadedUser = dbServiceModel.getObject(user1.getId(), User.class);
        User user2 = loadedUser.orElse(null);
        log.info("Find User {}", user2);

        dbServiceModel.updateObject(new User(user1.getId(), "updateDbServiceUser", 25));
        User reLoadedUser = (User) dbServiceModel.getObject(user1.getId(), User.class).orElse(null);
        log.info("Updated User {}", reLoadedUser);

        Account account1 = new Account("accountType", new BigDecimal(1.5));
        dbServiceModel.saveObject(account1);
        Optional<Account> loadedAccount = dbServiceModel.getObject(account1.getNo(), Account.class);
        Account account = loadedAccount.orElse(null);
        log.info("Find Account {}", account);


        dbServiceModel.updateObject(new Account(account1.getNo(), "updatedAccountType", new BigDecimal(4.5)));
        Account reloadedAccount = (Account) dbServiceModel.getObject(account1.getNo(), Account.class).orElse(null);
        log.info("Updated Account {}", reloadedAccount);


    }

    private void executeSql(DataSource dataSource, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.executeUpdate();
        }
        log.info("SQL executed: {}", sql);
    }
}
