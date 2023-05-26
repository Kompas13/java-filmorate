package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {
    Map<Integer, User> findAllUsers();

    User createUser(User user);

    User putUser(User user);

    User getUserById(Integer userId);

    List<User> getUserList();
}
