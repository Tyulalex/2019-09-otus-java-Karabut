package diy.orm.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.*;

@Slf4j
public class DbExecutorImpl implements DbExecutor {

    @Override
    public ResultSet select(Connection connection, PreparedStatement pst) throws SQLException {
        try {
            return pst.executeQuery();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public long insert(Connection connection, PreparedStatement pst) throws SQLException {
        String savePointName = generateSavePointString();
        Savepoint savepoint = connection.setSavepoint(savePointName);
        try {
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                rs.next();

                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            connection.rollback(savepoint);
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public void update(Connection connection, PreparedStatement pst) throws SQLException {
        String savePointName = generateSavePointString();
        Savepoint savepoint = connection.setSavepoint(savePointName);
        try {
            pst.executeUpdate();
        } catch (SQLException ex) {
            connection.rollback(savepoint);
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private String generateSavePointString() {
        return RandomStringUtils.random(5,
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }
}
