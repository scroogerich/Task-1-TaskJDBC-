package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private final EntityManagerFactory entityManagerFactory;

    public UserDaoHibernateImpl() {
        entityManagerFactory = Persistence.createEntityManagerFactory("UserPersistenceUnit");
    }

    @Override
    public void createUsersTable() {
        // Метод для создания таблицы пользователей не требуется, так как Hibernate автоматически создаст таблицу на основе сущности User.
    }

    @Override
    public void dropUsersTable() {
        entityManagerFactory.close(); // Закрываем фабрику сессий, что приведет к удалению таблицы
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        User user = new User(name, lastName, age);
        entityManager.persist(user);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void removeUserById(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public List<User> getAllUsers() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<User> userList = entityManager.createQuery("FROM User", User.class).getResultList();
        entityManager.close();
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.createQuery("DELETE FROM User").executeUpdate();

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}