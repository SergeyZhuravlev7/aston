package ru.zhuravlev.Task2;

import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import ru.zhuravlev.Task2.daos.UserDAOImpl;
import ru.zhuravlev.Task2.services.CommandHandler;
import ru.zhuravlev.Task2.services.UserInputService;
import ru.zhuravlev.Task2.services.UserService;
import ru.zhuravlev.Task2.util.ConsoleUI;
import ru.zhuravlev.Task2.util.HibernateConfig;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        SessionFactory factory = HibernateConfig.getSessionFactory();
        UserDAOImpl userDAO = new UserDAOImpl(factory);
        UserService userService = new UserService(userDAO);
        ConsoleUI console = new ConsoleUI();
        UserInputService userInputService = new UserInputService(console, new ModelMapper());
        CommandHandler commandHandler = new CommandHandler(userService, console, userInputService);
        commandHandler.start();
    }
}
