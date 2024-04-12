package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Ильфат", "Мухаметов", (byte) 26);
        userService.saveUser("Рустем", "Зиннатуллин", (byte) 27);
        userService.saveUser("Максим", "Филипов", (byte) 28);
        userService.saveUser("Артур", "Карпов", (byte) 29);

        List<User> userList = userService.getAllUsers();
        for (User user : userList) {
            System.out.println(user);
        }

        userService.removeUserById(2);

        userService.cleanUsersTable();

        userService.dropUsersTable();
    }
}