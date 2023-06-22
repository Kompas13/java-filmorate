package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllUser() {
        return userService.findAllUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        return userService.createUser(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {

        return userService.putUser(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable(name = "userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addUserFriend(@PathVariable(name = "id") Long userId, @PathVariable(name = "friendId") Long friendId) {
        userService.addUserFriend(userId, friendId);
    }

    //Удаление из друзей:(
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteUserFriend(@PathVariable(name = "id") Long userId, @PathVariable(name = "friendId") Long friendId) {
        userService.deleteUserFriend(userId, friendId);
    }

    //Список пользователей, являющихся друзьями пользователя с ID
    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable(name = "id") Long userId) {
        return userService.getUserFriends(userId);
    }

    //Список друзей, общих с другим пользователем.
    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable(name = "id") Long userFirstId, @PathVariable(name = "otherId") Long userSecondId) {
        return userService.getCommonFriends(userFirstId, userSecondId);
    }
}
