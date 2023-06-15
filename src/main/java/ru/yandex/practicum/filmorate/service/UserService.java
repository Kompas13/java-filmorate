package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;

@Service
public class UserService {
    @Autowired
    @Qualifier("userStorage")
    private final Storage userStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    public UserService(Storage userStorage) {
        this.userStorage = userStorage;
    }


    public Map<Integer, User> findAllUsers() {
        log.info("Количество зарегистрированных: {}", userStorage.getList().size());
        return userStorage.findAll();
    }

    public User createUser(User user) {
        return (User) userStorage.create(user);
    }

    public User putUser(User user) {
        log.info("Обновление  пользователя " + user);
        return (User) userStorage.put(user);
    }

    public User getUserById(Integer userId) {
        return (User) userStorage.getById(userId);
    }

    public Collection<User> addUserFriend(Integer userId, Integer friendId) {
        if (userId < 1 || friendId < 1) {
            throw new NotFoundException("Некорректный ID");
        }
        User user1 = (User) userStorage.getById(userId);
        user1.addFriend(friendId);
        User user2 = (User) userStorage.getById(friendId);
        user2.addFriend(userId);
        Collection<User> friends = new ArrayList<>();
        for (Integer friend : user1.getFriends()) {
            friends.add((User) userStorage.getById(friend));
        }
        log.info("Добавление друзей пользователя " + userId);
        return friends;
    }

    //Удаление из друзей:(
    public Collection<User> deleteUserFriend(Integer userId, Integer friendId) {
        User user1 = (User) userStorage.getById(userId);
        user1.deleteFriend(friendId);
        User user2 = (User) userStorage.getById(friendId);
        user2.deleteFriend(userId);
        Collection<User> friends = new ArrayList<>();
        for (Integer friend : user1.getFriends()) {
            friends.add((User) userStorage.getById(friend));
        }
        log.info("Удаление друзей пользователя " + userId);
        return friends;
    }

    //Список пользователей, являющихся друзьями пользователя с ID
    public Collection<User> getUserFriends(Integer userId) {
        Collection<User> friends = new ArrayList<>();
        User user1 = (User) userStorage.getById(userId);
        for (Integer friend : user1.getFriends()) {
            friends.add((User) userStorage.getById(friend));
        }
        System.out.println(friends);
        log.info("Вывод друзей пользователя " + userId);
        return friends;
    }

    //Список друзей, общих с другим пользователем.
    public Collection<User> getCommonFriends(Integer userFirstId, Integer userSecondId) {
        User user1 = (User) userStorage.getById(userFirstId);
        User user2 = (User) userStorage.getById(userSecondId);
        Set<Integer> result = new HashSet(user1.getFriends());
        result.retainAll(user2.getFriends()); // Пересечение
        Collection<User> commonFriends = new ArrayList<>();
        for (Integer friendsIdResult : result) {
            commonFriends.add((User) userStorage.getById(friendsIdResult));
        }
        log.info("Вывод общих друзей пользователей " + userFirstId + " " + userSecondId);
        return commonFriends;
    }
}
