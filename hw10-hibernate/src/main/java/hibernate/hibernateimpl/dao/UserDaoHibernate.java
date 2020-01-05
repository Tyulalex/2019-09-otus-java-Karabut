package hibernate.hibernateimpl.dao;

import hibernate.api.dao.UserDao;
import hibernate.api.dao.UserDaoException;
import hibernate.api.model.User;
import hibernate.api.sessionmanager.SessionManager;
import hibernate.hibernateimpl.sessionmanager.DatabaseSessionHibernate;
import hibernate.hibernateimpl.sessionmanager.SessionManagerHibernate;
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
