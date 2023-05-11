package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectUserIdException;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Data
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUser() {
        log.debug("Получение списка пользователей");
        log.debug("Количество зарегистрированных пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым.");
        }
        if (users.containsKey(user.getId())) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }

        if (user.getId() == 0) {
            user.setId(id++);
        }
        users.put(user.getId(), user);
        log.debug("Количество зарегистрированных пользователей: {}", users.size());
        log.debug("Добавление пользователя" + user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidEmailException("Адрес электронной почты не может быть пустым.");
        }
        if (!users.containsKey(user.getId())) {
            throw new IncorrectUserIdException("Неверный ID.");
        }
        users.put(user.getId(), user);
        log.debug("Добавление пользователя" + user);
        return user;
    }
}
