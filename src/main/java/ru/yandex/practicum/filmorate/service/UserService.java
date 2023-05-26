package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Map<Integer, User> findAllUsers() {
        log.info("Количество зарегистрированных: {}", userStorage.getUserList().size());
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User putUser(User user) {
        log.info("Обновление  пользователя " + user);
        return userStorage.putUser(user);
    }

    public User getUserById(Integer userId) {
        return userStorage.getUserById(userId);
    }

    public Collection<User> addUserFriend(Integer userId, Integer friendId) {
        if (userId < 1 || friendId < 1) {
            throw new ValidationException("Некорректный ID");
        }
        userStorage.getUserById(userId).getFriends().add(friendId);
        userStorage.getUserById(friendId).getFriends().add(userId);
        Collection<User> friends = new ArrayList<>();
        for (Integer friend : userStorage.getUserById(userId).getFriends()) {
            friends.add(userStorage.getUserById(friend));
        }
        log.info("Добавление друзей пользователя " + userId);
        return friends;
    }

    //Удаление из друзей:(
    public Collection<User> deleteUserFriend(Integer userId, Integer friendId) {
        userStorage.getUserById(userId).getFriends().remove(friendId);
        userStorage.getUserById(friendId).getFriends().remove(userId);
        Collection<User> friends = new ArrayList<>();
        for (Integer friend : userStorage.getUserById(userId).getFriends()) {
            friends.add(userStorage.getUserById(friend));
        }
        log.info("Удаление друзей пользователя " + userId);
        return friends;
    }

    //Список пользователей, являющихся друзьями пользователя с ID
    public Collection<User> getUserFriends(Integer userId) {
        Collection<User> friends = new ArrayList<>();
        for (Integer friend : userStorage.getUserById(userId).getFriends()) {
            friends.add(userStorage.getUserById(friend));
        }
        System.out.println(friends);
        log.info("Вывод друзей пользователя " + userId);
        return friends;
    }

    //Список друзей, общих с другим пользователем.
    public Collection<User> getCommonFriends(Integer userFirstId, Integer userSecondId) {
        Set<Integer> result = new HashSet<>(userStorage.getUserById(userFirstId).getFriends());
        result.retainAll(userStorage.getUserById(userSecondId).getFriends()); // Пересечение
        Collection<User> commonFriends = new ArrayList<>();
        for (Integer friendsIdResult : result) {
            commonFriends.add(userStorage.getUserById(friendsIdResult));
        }
        log.info("Вывод общих друзей пользователей " + userFirstId + " " + userSecondId);
        return commonFriends;
    }
}
