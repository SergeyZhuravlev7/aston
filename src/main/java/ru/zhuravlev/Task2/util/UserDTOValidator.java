package ru.zhuravlev.Task2.util;

import ru.zhuravlev.Task2.dtos.UserDTO;

public class UserDTOValidator {

    public static boolean validateUser(UserDTO user) {
        if (user == null) return false;
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) return false;
        if (user.getEmail() == null || user.getEmail().isBlank() || user.getEmail().isEmpty() || ! user.getEmail().contains("@"))
            return false;
        return user.getAge() > 17 && user.getAge() < 100;
    }
}
