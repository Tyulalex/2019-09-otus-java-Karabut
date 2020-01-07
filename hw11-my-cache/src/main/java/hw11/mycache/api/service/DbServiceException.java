package hw11.mycache.api.service;

public class DbServiceException extends RuntimeException {

    public DbServiceException(Exception ex) {
        super(ex);
    }
}
