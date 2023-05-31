package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(value = "filmStorage")
public class InMemoryFilmStorage implements Storage<Film> {

    private int id = 1;
    LocalDate date = LocalDate.of(1895, 12, 28);
    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public Map<Integer, Film> findAll() {
        Map<Integer, Film> filmsForReturn = films;
        return filmsForReturn;
    }

    @Override
    public Film create(Film film) {
        validateFilm(film);
        if (film.getId() == 0) {
            film.setId(id++);
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film put(Film film) {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Неверный ID.");
        }
        films.put(film.getId(), film);
        return film;
    }

    void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getReleaseDate().isBefore(date)) {
            throw new ValidationException("Дата выхода фильма ранее 28.12.1895.");
        }
    }

    @Override
    public List<Film> getList() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(Integer filmId) {
        if (filmId < 1) {
            throw new ValidationException("Некорректный ID");
        }
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Указанный ID не найден");
        }
        return films.get(filmId);
    }

    @Override
    public Boolean contains(Integer filmId) {
        return films.containsKey(filmId);
    }
}
