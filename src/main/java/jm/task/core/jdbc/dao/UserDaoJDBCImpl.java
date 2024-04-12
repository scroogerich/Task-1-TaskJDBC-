package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private static final Logger LOGGER = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public void createUsersTable() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS users (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(30) NOT NULL ,
                    lastName VARCHAR(60) NOT NULL ,
                    age SMALLINT NOT NULL);
                """;

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при создании таблицы пользователей", e);
        }
    }

    public void dropUsersTable() {
        String dropTableSQL = "DROP TABLE IF EXISTS users";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropTableSQL);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при удалении таблицы пользователей", e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String insertUserSQL = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при сохранении пользователя", e);
        }
    }

    public void removeUserById(long id) {
        String deleteUserSQL = "DELETE FROM users WHERE id = ?";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteUserSQL)) {
            preparedStatement.setLong(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                LOGGER.log(Level.INFO, "Пользователь с id={0} не найден.", id);
            } else {
                LOGGER.log(Level.INFO, "Пользователь с id={0} успешно удален.", id);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при удалении пользователя", e);
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String selectUsersSQL = "SELECT * FROM users";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectUsersSQL)) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                byte age = resultSet.getByte("age");

                User user = new User(id, name, lastName, age);
                userList.add(user);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении всех пользователей", e);
        }

        return userList;
    }

    public void cleanUsersTable() {
        String cleanTableSQL = "TRUNCATE TABLE users";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(cleanTableSQL);
            LOGGER.log(Level.INFO, "Таблица пользователей успешно очищена.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при очистке таблицы пользователей", e);
        }
    }
}