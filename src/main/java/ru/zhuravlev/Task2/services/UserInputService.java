package ru.zhuravlev.Task2.services;

import ch.qos.logback.classic.Logger;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import ru.zhuravlev.Task2.dtos.UserDTO;
import ru.zhuravlev.Task2.entitys.User;
import ru.zhuravlev.Task2.util.ConsoleUI;

import static ru.zhuravlev.Task2.util.UserDTOValidator.validateUser;

class UserInputService {

    private final ConsoleUI console = new ConsoleUI();
    private final Logger log = (Logger) LoggerFactory.getLogger(UserInputService.class);
    ModelMapper modelMapper = new ModelMapper();

    User handleSave() {
        console.printLine();
        console.print("Okay, let's create user. Enter Stop to cancel.");
        console.printConstraints();
        console.printLine();
        return createFromInput();
    }

    User createFromInput() {
        UserDTO userDTO = new UserDTO();

        console.printLine();
        console.print("Enter user name");
        console.printLine();
        String name = console.nextLine();
        if (name.equals("Stop")) return null;
        userDTO.setName(name);

        console.printLine();
        console.print("Enter user email");
        console.printLine();
        String email = console.nextLine();
        if (email.equals("Stop")) return null;
        userDTO.setEmail(email);

        while (true) {
            console.printLine();
            console.print("Enter user age");
            console.printLine();
            String age = console.nextLine();
            if (age.equals("Stop")) return null;
            try {
                userDTO.setAge(Integer.parseInt(age));
                break;
            } catch (NumberFormatException e) {
                log.warn("Invalid age: {}",age);
                console.printLine();
                console.print("Please enter a valid number for age.");
                console.printLine();
            }
        }
        if (! validateUser(userDTO)) {
            log.warn("User validation failed: {}",userDTO);
            console.printLine();
            console.print("User creation failed.");
            console.printLine();
            return null;
        }

        return modelMapper.map(userDTO, User.class);
    }

    Object handleRead(UserService userService) {
        console.printLine();
        console.print("""
                Choose field for search or enter Stop to cancel:
                ID - find by ID
                Name - find by name
                Email - find by email""");
        console.printLine();
        String field = console.nextLine();
        return switch (field) {
            case "ID" -> {
                long id = getId();
                yield (id > 0) ? userService.readUser(id) : null;
            }
            case "Name" -> {
                console.printLine();
                console.print("Enter user name");
                console.printLine();
                yield userService.readUser("Name",console.nextLine());
            }
            case "Email" -> {
                console.printLine();
                console.print("Enter user email");
                console.printLine();
                yield userService.readUser("Email",console.nextLine());
            }
            case "Stop" -> null;
            default -> {
                log.warn("Invalid read option: {}",field);
                yield null;
            }
        };
    }

    long getId() {
        long longId = 0;
        boolean isOver = false;
        do {
            console.printLine();
            console.print("Enter user ID");
            console.printLine();
            String id = console.nextLine();
            if (id.equals("Stop")) {
                break;
            }
            try {
                longId = Long.parseLong(id);
            } catch (Exception e) {
                log.warn("User id should be a number, but id = {}",id);
                continue;
            }
            isOver = true;
        } while (! isOver);
        return longId;
    }

    User handleUpdate() {
        console.printLine();
        console.print("Okay, lets update user. Enter Stop if you want to exit from save.");
        console.printConstraints();
        console.printLine();
        long id = getId();
        if (id == 0) return null;
        User user = createFromInput();
        if (user == null) return null;
        user.setId(id);
        return user;
    }
}
