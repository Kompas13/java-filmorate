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

    }

    @Test
    void shouldNotFilmWhenLoginIsBlank() {
        user.setLogin("");
        assertEquals(0, userController.getUsers().size(), "Число пользователей должно быть 0");
    }

    @Test
    void shouldNotFilmWhenLoginHasSpace() {
        user.setLogin("Charlize Theron");
        assertEquals(0, userController.getUsers().size(), "Число пользователей должно быть 0");
    }

    @Test
    void shouldNotFilmWhenEmailIsNotCorrect() {
        user.setEmail("rtyryret.ru");
        assertEquals(0, userController.getUsers().size(), "Число пользователей должно быть 0");
    }

}