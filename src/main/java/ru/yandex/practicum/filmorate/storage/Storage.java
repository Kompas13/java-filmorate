package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface Storage<T> {

    List<T> findAll();

    T create(T element);

    T put(T element);

    T getById(long element);

}
