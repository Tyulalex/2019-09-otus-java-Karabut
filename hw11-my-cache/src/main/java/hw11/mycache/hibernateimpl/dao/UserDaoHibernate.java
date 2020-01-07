package hw11.mycache.hibernateimpl.dao;

import hw11.mycache.api.dao.UserDao;
import hw11.mycache.api.dao.UserDaoException;
import hw11.mycache.api.model.User;
import hw11.mycache.api.sessionmanager.SessionManager;
import hw11.mycache.hibernateimpl.sessionmanager.DatabaseSessionHibernate;
import hw11.mycache.hibernateimpl.sessionmanager.SessionManagerHibernate;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.Optional;

@Slf4j
public class UserDaoHibernate implements UserDao {

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long saveUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
            }
            return user.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
