package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
        user = User.builder()
                .login("Lenin")
                .name("Vladimir")
                .email("nabronevike@vkomunism.su")
                .birthday(LocalDate.of(1870, 4, 22))
                .id(2)
                .build();
        userController.create(user);
    }

    @Test
    void createUserInController() {
        assertEquals(1, userController.getUsers().size(), "Amount of users must be 1");
    }

    @Test
    void putUserInController() {
        user.setEmail("qwrer@sdf.ru");
        user.setId(2);
        userController.put(user);
        assertEquals("{2=User(login=Lenin, name=Vladimir, id=2, email=qwrer@sdf.ru, birthday=1870-04-22)}", userController.getUsers().toString(), "Info about user is not correct");
    }

    @Test
    void findAllUser(){
        assertEquals("[User(login=Lenin, name=Vladimir, id=2, email=nabronevike@vkomunism.su, birthday=1870-04-22)]", userController.findAllUser().toString(), "Info about users is not correct");
    }

}