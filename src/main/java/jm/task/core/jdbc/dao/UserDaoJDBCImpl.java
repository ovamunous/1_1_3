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

    private Connection connection;
    private Statement statement;

    public UserDaoJDBCImpl() {
        try {
            this.connection = Util.getConnection();
            this.statement = connection.createStatement();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUsersTable() {
        try {
            statement.execute("CREATE TABLE IF NOT EXISTS user ("
                    + "`id` INT NOT NULL AUTO_INCREMENT,"
                    + "`name` VARCHAR(45) NOT NULL,"
                    + "`lastName` VARCHAR(45) NOT NULL,"
                    + "`age` INT(3) NOT NULL,"
                    + "PRIMARY KEY (`id`));");
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try {
            statement.execute("Drop table if exists user");
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO user (name, lastName, age) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            try {
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            }
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            try {
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
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
        try {
            statement.executeUpdate("truncate user");
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
