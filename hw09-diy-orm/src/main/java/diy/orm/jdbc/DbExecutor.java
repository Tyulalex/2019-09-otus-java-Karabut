package diy.orm.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DbExecutor<T> {

    ResultSet select(Connection connection, PreparedStatement pst) throws SQLException;

    long insert(Connection connection, PreparedStatement pst) throws SQLException;

    void update(Connection connection, PreparedStatement pst) throws SQLException;
}
