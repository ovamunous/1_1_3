package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS user ("
                    + "`id` INT NOT NULL AUTO_INCREMENT,"
                    + "`name` VARCHAR(45) NOT NULL,"
                    + "`lastName` VARCHAR(45) NOT NULL,"
                    + "`age` INT(3) NOT NULL,"
                    + "PRIMARY KEY (`id`));");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("Drop table user");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "START TRANSACTION; INSERT INTO user (name, lastName, age) VALUES (?, ?, ?); COMMIT";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                preparedStatement.executeUpdate("ROLLBACK");
            }
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        String sql = "START TRANSACTION ;DELETE FROM user WHERE id = ?; COMMIT";
        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                preparedStatement.executeUpdate("ROLLBACK");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM user");
            while (rs.next()) {
                users.add(new User(rs.getString("name"),
                        rs.getString("lastName"),
                        rs.getByte("age")));
            }
            users.forEach(System.out::println);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {
            try {
                statement.executeUpdate("START TRANSACTION; truncate user; COMMIT ");
            } catch (SQLException e) {
                statement.executeUpdate("ROLLBACK");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
