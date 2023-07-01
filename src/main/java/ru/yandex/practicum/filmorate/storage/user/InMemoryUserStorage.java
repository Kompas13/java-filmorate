package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;

@Component(value = "userStorage")
@Getter
public class InMemoryUserStorage implements Storage<User> {
    private int id = 1;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private Map<Long, User> users = new HashMap<>();

    public List<User> findAll() {
        log.info("Получение списка пользователей");
        log.info("Количество зарегистрированных пользователей: {}", users.size());
        List<User> usersForReturn = new ArrayList<>();
        for (User value : users.values()) {
            usersForReturn.add(value);
        }
        return usersForReturn;
    }

    @Override
    public User create(User user) {
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
    public User put(User user) {
        validateUser(user);
        if (!users.containsKey(user.getId()) || user.getId() < 0) {
            throw new NotFoundException("Неверный ID (put User).");
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
    public User getById(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Указанный ID не найден" + userId);
        }
        return users.get(userId);
    }


    public Collection<User> getList() {
        return new ArrayList<>(users.values());
    }


    public Boolean contains(Integer userId) {
        return users.containsKey(userId);
    }
}
