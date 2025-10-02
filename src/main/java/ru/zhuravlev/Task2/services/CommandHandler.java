package ru.zhuravlev.Task2.services;

import ch.qos.logback.classic.Logger;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import ru.zhuravlev.Task2.daos.UserDAOImpl;
import ru.zhuravlev.Task2.entitys.User;
import ru.zhuravlev.Task2.util.ConsoleUI;
import ru.zhuravlev.Task2.util.DAOException;
import ru.zhuravlev.Task2.util.HibernateConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.time.LocalDateTime.now;

public class CommandHandler {

    private final UserService userService;
    private final Logger log = (Logger) LoggerFactory.getLogger(CommandHandler.class);
    private final ConsoleUI console;
    private final UserInputService userInputService;

    public CommandHandler(UserService userService,ConsoleUI console, UserInputService userInputService) {
        this.userService = userService;
        this.console = console;
        this.userInputService = userInputService;
    }

    public void start() {
        console.printWithLineBreak("Welcome in UserService application!");
        boolean running = true;

        while (running) {
            console.printWithLineBreak("""
                    Choose your command:
                    Save - create new user and save
                    Find - find user by email,name or id
                    FindAll - show all users
                    Update - update user with selected id
                    Delete - delete user with selected id
                    Exit - for stop application""");
            String command = console.nextLine();
            switch (command) {
                case "Save" -> handleSave();
                case "Find" -> handleFind();
                case "FindAll" -> handleFindAll();
                case "Update" -> handleUpdate();
                case "Delete" -> handleDelete();
                case "Exit" -> {
                    running = false;
                    log.info("Application stopped.");
                    console.printWithLineBreak("Goodbye!");
                }
                default -> log.warn("Invalid command: {}",command);
            }
        }
    }

    private void handleSave() {
        User user = userInputService.handleSave();
        if (user == null) return;
        try {
            user.setCreated_at(now());
            userService.saveUser(user);
            log.info("User created successfully!");
            console.printWithLineBreak("User created successfully!");
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
        }
    }

    private void handleFind() {
        Object result = null;
        try {
            result = userInputService.handleFind(userService);
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
        }
        if (result != null) {
            console.printWithLineBreak("Lets see what im can find.");
            if (result instanceof User) console.print(result.toString());
            else {
                List<User> userList = (List<User>) result;
                console.printList(userList);
            }
        } else {
            log.warn("User not existing.");
            console.printWithLineBreak("Cant find this user. Try again with corrected input.");
        }
    }

    private void handleUpdate() {
        User user = userInputService.handleUpdate();
        if (user == null) return;
        try {
            userService.update(user,user.getId());
            log.info("User updated successfully!");
            console.printWithLineBreak("User updated successfully!");
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
            console.printWithLineBreak("Sorry, but user with id " + user.getId() + " was not found!");
        }
    }

    private void handleDelete() {
        long id = userInputService.getId();
        try {
            userService.delete(id);
            log.info("User deleted successfully!");
            console.printWithLineBreak("User deleted successfully!");
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
            console.printWithLineBreak("Sorry, but user with id " + id + " was not found!");
        }
    }

    private void handleFindAll() {
        Collection<User> users = null;
        try {
            users = userService.findAll();
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
        }
        List<User> userList = new ArrayList<>(users);
        if (userList.isEmpty()) {
            console.printWithLineBreak("No users found!");
            return;
        }
        console.printWithLineBreak("Users found successfully!");
        console.printList(userList);
    }
}
