package webserver.com.db.api.sessionmanager;

public class SessionManagerException extends RuntimeException {

    public SessionManagerException(Exception ex) {
        super(ex);
    }

    public SessionManagerException(String msg) {
        super(msg);
    }
}
