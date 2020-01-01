package diy.orm.sessionmanager;

import java.sql.Connection;

public interface DatabaseSession {

    Connection getConnection();
}
