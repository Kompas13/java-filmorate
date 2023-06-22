package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final Storage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") Storage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public List<User> findAllUsers() {
        return userStorage.findAll();
    }

    public User createUser(User user) {
        return (User) userStorage.create(user);
    }

    public User putUser(User user) {
        return (User) userStorage.put(user);
    }

    public User getUserById(long id) {
        return (User) userStorage.getById(id);
    }

    public void addUserFriend(long userId, long friendId) {
        friendStorage.addFriend(userId, friendId);
        log.info("Пользователь с id: {} стал другом пользователя с id: {}", userId, friendId);
    }

    public void deleteUserFriend(long userId, long friendId) {
        friendStorage.deleteFriend(userId, friendId);
        log.info("Пользователь с id: {} удален из списка ваших друзей", friendId);
    }

    public List<User> getUserFriends(long userId) {
        return new ArrayList<>(friendStorage.getFriends(userId));
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        userStorage.getById(userId);
        userStorage.getById(otherId);

        return friendStorage.getCommonFriends(userId, otherId);
    }
}