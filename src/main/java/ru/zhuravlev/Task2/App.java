package ru.zhuravlev.Task2;

import ru.zhuravlev.Task2.services.CommandHandler;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.start();
    }
}
