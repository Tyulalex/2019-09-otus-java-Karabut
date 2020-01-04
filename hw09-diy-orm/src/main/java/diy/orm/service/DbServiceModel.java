package diy.orm.service;

import java.util.Optional;

public interface DbServiceModel<T> {

    void saveObject(T obj);

    Optional<T> getObject(long id, Class clazz);

    void updateObject(T obj);

}
