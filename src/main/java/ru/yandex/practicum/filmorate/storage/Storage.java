package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Map;

public interface Storage<T> {

    Map<Integer, T> findAll();

    T create(T element);

    T put(T element);

    T getById(Integer elementId);

    Collection<T> getList();

    Boolean contains(Integer elementId);
}
