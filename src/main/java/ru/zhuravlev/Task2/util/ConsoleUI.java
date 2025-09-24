package ru.zhuravlev.Task2.util;

import java.util.Scanner;

public class ConsoleUI {

    private static Scanner sc = new Scanner(System.in);

    public String nextLine() {
        return sc.nextLine();
    }

    public void print(String message) {
        System.out.println(message);
    }

    public void printConstraints() {
        System.out.println("User name should not be blank or empty.");
        System.out.println("User email should not be blank or empty and should contains '@'.");
        System.out.println("User age should be a number and should be between 18 and 100 years old.");
    }

    public void printLine() {
        System.out.println("______________________________________");
    }
}
