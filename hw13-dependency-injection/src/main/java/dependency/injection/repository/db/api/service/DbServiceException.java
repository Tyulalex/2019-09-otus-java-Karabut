package dependency.injection.repository.db.api.service;

public class DbServiceException extends RuntimeException {

    public DbServiceException(Exception ex) {
        super(ex);
    }
}
