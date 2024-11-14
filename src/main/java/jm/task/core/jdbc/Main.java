package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Vova", "asd", (byte) 10);
        userService.saveUser("Petr", "asd", (byte) 20);
        userService.saveUser("Vasya", "asd", (byte) 30);
        userService.saveUser("Nikita", "asd", (byte) 40);
        userService.getAllUsers();
        userService.removeUserById(1);
        userService.cleanUsersTable();
        userService.dropUsersTable();
        userService.createUsersTable();
        Util.closeConnection(userService.getConnection());
    }
}
