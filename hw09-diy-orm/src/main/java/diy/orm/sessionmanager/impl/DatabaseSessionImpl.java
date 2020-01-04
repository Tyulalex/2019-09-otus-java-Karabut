package diy.orm.sessionmanager.impl;

import diy.orm.sessionmanager.DatabaseSession;

import java.sql.Connection;

public class DatabaseSessionImpl implements DatabaseSession {

    public final Connection connection;


    public DatabaseSessionImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Connection getConnection() {
        return connection;
    }
}
