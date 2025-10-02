package ru.zhuravlev.Task2.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.zhuravlev.Task2.entitys.User;
import ru.zhuravlev.Task2.util.ConsoleUI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith (MockitoExtension.class)
class UserInputServiceTest {

    @Mock
    ConsoleUI console;

    @Mock
    ModelMapper modelMapper;

    @Mock
    UserService userService;

    @InjectMocks
    UserInputService userInputService;


    @Test
    void handleSaveShouldReturnUser() {
        when(console.nextLine())
                .thenReturn("John")   // name
                .thenReturn("john@example.com") // email
                .thenReturn("25");   // age

        User user = userInputService.handleSave();

        assertNotNull(user);
        assertEquals("John",user.getName());
        assertEquals("john@example.com",user.getEmail());
        assertEquals(25,user.getAge());

        verify(console).printWithLineBreak("Okay, let's create user. Enter Stop to cancel.");
        verify(console).printConstraints();
    }

    @Test
    void createFromInputShouldReturnNull() {
        when(console.nextLine())
                .thenReturn("Stop")   // name
                .thenReturn("john@example.com") // email
                .thenReturn("25");

        User user = userInputService.createFromInput();

        assertNull(user);
    }

    @Test
    void createFromInputShouldReturnNull2() {
        when(console.nextLine())
                .thenReturn("John")   // name
                .thenReturn("Stop") // email
                .thenReturn("25");

        User user = userInputService.createFromInput();

        assertNull(user);
    }

    @Test
    void createFromInputShouldReturnNull3() {
        when(console.nextLine())
                .thenReturn("John")   // name
                .thenReturn("john@example.com") // email
                .thenReturn("Stop");

        User user = userInputService.createFromInput();

        assertNull(user);
    }

    @Test
    void createFromInputShouldPrintErrorMessage() {
        when(console.nextLine())
                .thenReturn("John")   // name
                .thenReturn("john@example.com") // email
                .thenReturn("999");

        User user = userInputService.createFromInput();

        assertNull(user);
        verify(console).printWithLineBreak("User creation failed.");
    }

    @Test
    void handleFindByIdShouldReturnUser() {
        String id = "1";
        User user = new User();
        when(console.nextLine())
                .thenReturn("ID")
                .thenReturn(id);
        when(userService.readUser(Long.parseLong(id))).thenReturn(user);

        User expectedUser = (User) userInputService.handleFind(userService);

        assertEquals(expectedUser,user);
        verify(userService).readUser(Long.parseLong(id));
        verify(console).printWithLineBreak("""
                Choose field for search or enter Stop to cancel:
                ID - find by ID
                Name - find by name
                Email - find by email""");
    }

    @Test
    void handleFindByIdShouldReturnNull() {
        String id = "1";
        when(console.nextLine())
                .thenReturn("ID")
                .thenReturn(id);
        when(userService.readUser(Long.parseLong(id))).thenReturn(null);

        User expectedUser = (User) userInputService.handleFind(userService);

        assertNull(expectedUser);
        verify(userService).readUser(Long.parseLong(id));
        verify(console).printWithLineBreak("""
                Choose field for search or enter Stop to cancel:
                ID - find by ID
                Name - find by name
                Email - find by email""");
    }

    @Test
    void handleFindByNameShouldReturnUser() {
        User user = new User();
        when(console.nextLine())
                .thenReturn("Name")
                .thenReturn("John");
        when(userService.readUser("Name","John")).thenReturn(user);

        User expectedUser = (User) userInputService.handleFind(userService);

        assertEquals(expectedUser,user);
        verify(userService).readUser("Name","John");
        verify(console).printWithLineBreak("""
                Choose field for search or enter Stop to cancel:
                ID - find by ID
                Name - find by name
                Email - find by email""");
    }

    @Test
    void handleFindByNameShouldReturnNull() {
        when(console.nextLine())
                .thenReturn("Name")
                .thenReturn("John");
        when(userService.readUser("Name","John")).thenReturn(null);

        User expectedUser = (User) userInputService.handleFind(userService);

        assertNull(expectedUser);
        verify(userService).readUser("Name","John");
        verify(console).printWithLineBreak("""
                Choose field for search or enter Stop to cancel:
                ID - find by ID
                Name - find by name
                Email - find by email""");
    }

    @Test
    void handleFindByEmailShouldReturnUser() {
        User user = new User();
        when(console.nextLine())
                .thenReturn("Email")
                .thenReturn("John@example.com");
        when(userService.readUser("Email","John@example.com")).thenReturn(user);

        User expectedUser = (User) userInputService.handleFind(userService);

        assertEquals(expectedUser,user);
        verify(userService).readUser("Email","John@example.com");
        verify(console).printWithLineBreak("""
                Choose field for search or enter Stop to cancel:
                ID - find by ID
                Name - find by name
                Email - find by email""");
    }

    @Test
    void handleFindByStopShouldReturnNull() {
        when(console.nextLine())
                .thenReturn("Stop");

        User expectedUser = (User) userInputService.handleFind(userService);

        assertNull(expectedUser);
        verify(console).printWithLineBreak("""
                Choose field for search or enter Stop to cancel:
                ID - find by ID
                Name - find by name
                Email - find by email""");
    }

    @Test
    void handleFindByEmailShouldReturnNull() {
        when(console.nextLine())
                .thenReturn("Email")
                .thenReturn("John@example.com");
        when(userService.readUser("Email","John@example.com")).thenReturn(null);

        User expectedUser = (User) userInputService.handleFind(userService);

        assertNull(expectedUser);
        verify(userService).readUser("Email","John@example.com");
        verify(console).printWithLineBreak("""
                Choose field for search or enter Stop to cancel:
                ID - find by ID
                Name - find by name
                Email - find by email""");
    }

    @Test
    void getIdShouldReturnId() {
        String actualId = "20";
        when(console.nextLine()).thenReturn(actualId);

        Long expectedId = userInputService.getId();

        assertEquals(expectedId,Long.parseLong(actualId));
        verify(console).printWithLineBreak("Enter user ID");
    }

    @Test
    void getIdShouldPrintErrorMessageAndThenReturnId() {
        String actualId = "20";
        when(console.nextLine())
                .thenReturn("Not id")
                .thenReturn(actualId);

        Long expectedId = userInputService.getId();

        assertEquals(expectedId,Long.parseLong(actualId));
        verify(console, times(2)).printWithLineBreak("Enter user ID");
        verify(console).printWithLineBreak("Invalid user ID, please try again.");
    }

    @Test
    void handleUpdateShouldReturnUser() {
        String actualId = "20";
        when(console.nextLine())
                .thenReturn(actualId)
                .thenReturn("John")   // name
                .thenReturn("john@example.com") // email
                .thenReturn("20");

        User user = userInputService.handleUpdate();

        assertEquals(20,user.getId());
        assertEquals("John",user.getName());
        assertEquals("john@example.com",user.getEmail());
        assertEquals(20,user.getAge());
        verify(console).printWithLineBreak("Okay, lets update user. Enter Stop if you want to exit from save.");
    }

    @Test
    void handleUpdateShouldReturnNull() {
        when(console.nextLine())
                .thenReturn("0");

        User user = userInputService.handleUpdate();

        assertNull(user);
        verify(console).printWithLineBreak("Okay, lets update user. Enter Stop if you want to exit from save.");
    }
}