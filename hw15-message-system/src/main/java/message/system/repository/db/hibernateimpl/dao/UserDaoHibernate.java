package message.system.repository.db.hibernateimpl.dao;

import lombok.extern.slf4j.Slf4j;
import message.system.repository.db.api.dao.UserDao;
import message.system.repository.db.api.dao.UserDaoException;
import message.system.repository.db.api.model.RoleMapping;
import message.system.repository.db.api.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserDaoHibernate implements UserDao {

    private final SessionFactory sessionFactory;


    public UserDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.getSession().find(User.class, id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            if (user.getId() > 0) {
                session.merge(user);
            } else {
                session.persist(user);
            }
            transaction.commit();
            return user.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public List<User> getUsersList() {
        try (Session session = sessionFactory.openSession()) {
            String queryString = String.format("from User as u where u.role='%s'", RoleMapping.WEBUSER);

            return session.createQuery(queryString, User.class).list();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.byNaturalId(User.class).using("login", login).load());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }
}
