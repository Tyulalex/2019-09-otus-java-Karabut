package diy.orm.jdbc;

import diy.orm.h2.DataSourceH2;
import diy.orm.model.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

class JdbcTemplateImplTest {

    private final static String CREATE_ACCOUNT_TABLE_SQL = "create table Account(no bigint(20) NOT NULL auto_increment, " +
            " type varchar(255), rest number);";
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;


    @BeforeEach
    void setUp() throws Exception {
        this.dataSource = new DataSourceH2();
        this.dataSource.getConnection().setAutoCommit(true);
        DbExecutor dbExecutor = new DbExecutorImpl();
        this.jdbcTemplate = new JdbcTemplateImpl<>(dbExecutor);
        this.executeSql(dataSource.getConnection(), CREATE_ACCOUNT_TABLE_SQL);
    }

    @Test
    void testCreateObject() throws Exception {
        Account account = new Account("accountType", new BigDecimal(3.5));
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);
        this.jdbcTemplate.create(connection, account);
        Assertions.assertEquals(account, executeSelectSql("SELECT no, type, rest FROM account WHERE no = 1;"));
    }

    @Test
    void testLoadObject() throws Exception {

        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);
        executeSql(connection, "INSERT INTO account (type, rest) VALUES ('Personal', 134.5);");
        Optional<Account> loadedAccount = this.jdbcTemplate.load(connection, 1, Account.class);
        Assertions.assertEquals(new Account(1, "Personal", new BigDecimal(134.5)), loadedAccount.get());
    }

    @Test
    void testUpdateObject() throws Exception {

        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);
        executeSql(connection, "INSERT INTO account (type, rest) VALUES ('Personal', 134.5);");
        Account account = new Account(1, "Physical", new BigDecimal(12.5));
        this.jdbcTemplate.update(connection, account);

        Assertions.assertEquals(account,
                executeSelectSql("SELECT no, type, rest FROM account WHERE no = 1;"));
    }


    @Test
    void testCreateOrUpdateObjectWithUpdateCase() throws Exception {

        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);
        executeSql(connection, "INSERT INTO account (type, rest) VALUES ('Personal', 134.5);");
        Account account = new Account(1, "Physical", new BigDecimal(12.5));
        this.jdbcTemplate.createOrUpdate(connection, account);

        Assertions.assertEquals(account,
                executeSelectSql("SELECT no, type, rest FROM account WHERE no = 1;"));
    }

    @Test
    void testCreateOrUpdateObjectWithCreateCase() throws Exception {

        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(true);
        executeSql(connection, "INSERT INTO account (type, rest) VALUES ('Personal', 134.5);");
        Account account = new Account(2, "Physical", new BigDecimal(12.5));
        this.jdbcTemplate.createOrUpdate(connection, account);

        Assertions.assertEquals(account,
                executeSelectSql("SELECT no, type, rest FROM account WHERE no = 2;"));
    }


    private void executeSql(Connection connection, String sql) throws SQLException {
        try (
                PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.executeUpdate();
        }
    }

    private Account executeSelectSql(String sql) throws SQLException {
        Account account = new Account();
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement pst = connection.prepareStatement(sql)) {
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {

                account.setNo(resultSet.getLong("no"));
                account.setRest(resultSet.getBigDecimal("rest"));
                account.setType(resultSet.getString("type"));

            }
        }
        return account;
    }

    @AfterEach
    void tearDown() throws Exception {
        this.executeSql(this.dataSource.getConnection(), "DROP TABLE account");
    }
}