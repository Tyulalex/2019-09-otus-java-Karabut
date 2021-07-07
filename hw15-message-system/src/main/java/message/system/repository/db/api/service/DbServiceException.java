package message.system.repository.db.api.service;

public class DbServiceException extends RuntimeException {

    public DbServiceException(Exception ex) {
        super(ex);
    }
}
