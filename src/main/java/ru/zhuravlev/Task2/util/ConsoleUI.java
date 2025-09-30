package ru.zhuravlev.Task2.util;

import ru.zhuravlev.Task2.entitys.User;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private static final Scanner sc = new Scanner(System.in);

    public String nextLine() {
        return sc.nextLine();
    }

    public void printWithLineBreak(String message) {
        printLine();
        System.out.println(message);
        printLine();
    }

    public void print(String message) {
        System.out.println(message);
    }

    public void printConstraints() {
        System.out.println("User name should not be blank or empty.");
        System.out.println("User email should not be blank or empty and should contains '@'.");
        System.out.println("User age should be a number and should be between 18 and 100 years old.");
        printLine();
    }

    public void printLine() {
        System.out.println("______________________________________");
    }

    public void printList(List<User> users) {
        for (User user : users) {
            print(user.toString());
        }
        printLine();
    }
}
