package diy.orm.dao;

public class ObjectDaoException extends RuntimeException {

    public ObjectDaoException(Exception ex) {
        super(ex);
    }
}
