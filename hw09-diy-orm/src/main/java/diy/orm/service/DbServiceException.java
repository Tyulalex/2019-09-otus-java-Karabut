package diy.orm.service;

public class DbServiceException extends RuntimeException {

    public DbServiceException(Exception ex) {
        super(ex);
    }
}
