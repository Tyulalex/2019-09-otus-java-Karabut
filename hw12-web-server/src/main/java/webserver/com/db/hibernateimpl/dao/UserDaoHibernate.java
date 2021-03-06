package webserver.com.db.hibernateimpl.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import webserver.com.db.api.dao.UserDao;
import webserver.com.db.api.dao.UserDaoException;
import webserver.com.db.api.model.RoleMapping;
import webserver.com.db.api.model.User;
import webserver.com.db.api.sessionmanager.SessionManager;
import webserver.com.db.hibernateimpl.sessionmanager.DatabaseSessionHibernate;
import webserver.com.db.hibernateimpl.sessionmanager.SessionManagerHibernate;

import java.util.List;
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

    @Override
    public List<User> getUsersList() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            String queryString = String.format("from User as u where u.role='%s'", RoleMapping.WEBUSER);

            return hibernateSession.createQuery(queryString, User.class).list();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return Optional.ofNullable(currentSession
                    .getHibernateSession().byNaturalId(User.class).using("login", login).load());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }
}
