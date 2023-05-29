package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Getter
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, User> users = new HashMap<>();

    public Map<Integer, User> findAllUsers() {
        log.info("Получение списка пользователей");
        log.info("Количество зарегистрированных пользователей: {}", users.size());
        Map<Integer, User> usersForReturn = users;
        return usersForReturn;
    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован (post - create User).");
        }

        if (user.getId() == 0) {
            user.setId(id++);
        }
        users.put(user.getId(), user);
        log.info("Количество зарегистрированных пользователей: {}", users.size());
        log.info("Добавление пользователя" + user);
        return user;
    }

    @Override
    public User putUser(User user) {
        validateUser(user);
        if (!users.containsKey(user.getId()) || user.getId() < 0) {
            throw new ValidationException("Неверный ID (put User).");
        }
        users.put(user.getId(), user);
        log.info("Добавление пользователя" + user);
        return user;
    }

    void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Адрес электронной почты не может быть пустым (put User).");
        }
    }

    @Override
    public User getUserById(Integer userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Указанный ID не найден" + userId);
        }
        return users.get(userId);
    }

    @Override
    public List<User> getUserList() {
        return new ArrayList<>(users.values());
    }
}
