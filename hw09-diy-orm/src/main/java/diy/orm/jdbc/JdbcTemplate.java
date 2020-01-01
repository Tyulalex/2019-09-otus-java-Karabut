package diy.orm.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface JdbcTemplate<T> {

    void create(Connection connection, T objectData) throws SQLException, IllegalAccessException;

    void update(Connection connection, T objectData) throws SQLException, IllegalAccessException;

    void createOrUpdate(Connection connection, T objectData) throws ReflectiveOperationException, SQLException; // опционально.

    Optional<T> load(Connection connection, long id, Class<T> clazz) throws SQLException, ReflectiveOperationException;
}
