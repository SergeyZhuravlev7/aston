package ru.zhuravlev.Task2.services;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ru.zhuravlev.Task2.entitys.User;
import ru.zhuravlev.Task2.util.ConsoleUI;
import ru.zhuravlev.Task2.util.DAOException;

import java.util.List;

import static java.time.LocalDateTime.now;

public class CommandHandler {

    private final UserService userService = new UserService();
    private final Logger log = (Logger) LoggerFactory.getLogger(CommandHandler.class);
    private final ConsoleUI console = new ConsoleUI();
    private final UserInputService userInputService = new UserInputService();


    public void start() {
        console.printLine();
        console.print("Welcome in UserService application!");
        boolean running = true;

        while (running) {
            console.printLine();
            console.print("""
                    Choose your command:
                    Save - create new user and save
                    Read - find user by email,name or id
                    Update - update user with selected id
                    Delete - delete user with selected id
                    Exit - for stop application""");
            console.printLine();

            String command = console.nextLine();
            switch (command) {
                case "Save" -> handleSave();
                case "Read" -> handleRead();
                case "Update" -> handleUpdate();
                case "Delete" -> handleDelete();
                case "Exit" -> {
                    running = false;
                    log.info("Application stopped.");
                    console.printLine();
                    console.print("Goodbye!");
                    console.printLine();
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
            console.printLine();
            console.print("User created successfully!");
            console.printLine();
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
        }
    }

    private void handleRead() {
        Object result = null;
        try {
            result = userInputService.handleRead(userService);
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
        }
        if (result != null) {
            console.printLine();
            console.print("Lets see what im can find.");
            console.print("\n");
            if (result instanceof User) console.print(result.toString());
            else {
                List<User> users = (List<User>) result;
                for (User user : users) {
                    console.print(user.toString());
                }
            }
            console.printLine();
        }
        else {
            log.warn("User not existing.");
            console.printLine();
            console.print("Cant find this user. Try again with corrected input.");
            console.printLine();
        }
    }

    private void handleUpdate() {
        User user = userInputService.handleUpdate();
        if (user == null) return;
        try {
            userService.update(user,user.getId());
            log.info("User updated successfully!");
            console.printLine();
            console.print("User updated successfully!");
            console.printLine();
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
            console.printLine();
            console.print("Sorry, but user with id " + user.getId() + " was not found!");
            console.printLine();
        }
    }

    private void handleDelete() {
        long id = userInputService.getId();
        try {
            userService.delete(id);
            log.info("User deleted successfully!");
            console.printLine();
            console.print("User deleted successfully!");
            console.printLine();
        } catch (DAOException ex) {
            log.warn(ex.getMessage());
            console.printLine();
            console.print("Sorry, but user with id " + id + " was not found!");
            console.printLine();
        }
    }
}
