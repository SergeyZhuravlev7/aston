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
        console.printWithLineBreak("Okay, let's create user. Enter Stop to cancel.");
        console.printConstraints();
        return createFromInput();
    }

    User createFromInput() {
        UserDTO userDTO = new UserDTO();
        console.printWithLineBreak("Enter user name");
        String name = console.nextLine();
        if (name.equals("Stop")) return null;
        userDTO.setName(name);

        console.printWithLineBreak("Enter user email");
        String email = console.nextLine();
        if (email.equals("Stop")) return null;
        userDTO.setEmail(email);

        while (true) {
            console.printWithLineBreak("Enter user age");
            String age = console.nextLine();
            if (age.equals("Stop")) return null;
            try {
                userDTO.setAge(Integer.parseInt(age));
                break;
            } catch (NumberFormatException e) {
                log.warn("Invalid age: {}",age);
                console.printWithLineBreak("Please enter a valid number for age.");
            }
        }
        if (! validateUser(userDTO)) {
            log.warn("User validation failed: {}",userDTO);
            console.printWithLineBreak("User creation failed.");
            return null;
        }

        return modelMapper.map(userDTO, User.class);
    }

    Object handleFind(UserService userService) {
        console.printWithLineBreak("""
                Choose field for search or enter Stop to cancel:
                ID - find by ID
                Name - find by name
                Email - find by email""");
        String field = console.nextLine();
        return switch (field) {
            case "ID" -> {
                long id = getId();
                yield (id > 0) ? userService.readUser(id) : null;
            }
            case "Name" -> {
                console.printWithLineBreak("Enter user name");
                yield userService.readUser("Name",console.nextLine());
            }
            case "Email" -> {
                console.printWithLineBreak("Enter user email");
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
            console.printWithLineBreak("Enter user ID");
            String id = console.nextLine();
            if (id.equals("Stop")) {
                break;
            }
            try {
                longId = Long.parseLong(id);
            } catch (Exception e) {
                log.warn("User id should be a number, but id = {}",id);
                console.printWithLineBreak("Invalid user ID, please try again.");
                continue;
            }
            isOver = true;
        } while (! isOver);
        return longId;
    }

    User handleUpdate() {
        console.printWithLineBreak("Okay, lets update user. Enter Stop if you want to exit from save.");
        console.printConstraints();
        long id = getId();
        if (id == 0) return null;
        User user = createFromInput();
        if (user == null) return null;
        user.setId(id);
        return user;
    }
}
