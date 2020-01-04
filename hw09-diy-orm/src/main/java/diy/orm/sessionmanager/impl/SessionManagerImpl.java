package diy.orm.sessionmanager.impl;

import diy.orm.sessionmanager.DatabaseSession;
import diy.orm.sessionmanager.SessionManager;
import diy.orm.sessionmanager.SessionManagerException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SessionManagerImpl implements SessionManager {

    private static final int TIMEOUT_IN_SECONDS = 5;
    private final DataSource dataSource;
    private Connection connection;
    private DatabaseSession databaseSession;


    public SessionManagerImpl(DataSource dataSource) {
        if (dataSource == null) {
            throw new SessionManagerException("DataSource is null");
        }
        this.dataSource = dataSource;
    }

    @Override
    public void beginSession() {
        try {
            connection = dataSource.getConnection();
            databaseSession = new DatabaseSessionImpl(connection);
        } catch (SQLException ex) {
            throw new SessionManagerException(ex);
        }
    }

    @Override
    public void commitSession() {
        checkConnection();
        try {
            connection.commit();
        } catch (SQLException ex) {
            throw new SessionManagerException(ex);
        }
    }

    @Override
    public void rollbackSession() {
        checkConnection();
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw new SessionManagerException(ex);
        }
    }

    @Override
    public void close() {
        checkConnection();
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new SessionManagerException(ex);
        }
    }

    @Override
    public DatabaseSession getCurrentSession() {
        checkConnection();
        return this.databaseSession;
    }

    private void checkConnection() {
        try {
            if (connection == null || !connection.isValid(TIMEOUT_IN_SECONDS)) {
                throw new SessionManagerException("Connection is invalid");
            }
        } catch (SQLException ex) {
            throw new SessionManagerException(ex);
        }
    }
}
