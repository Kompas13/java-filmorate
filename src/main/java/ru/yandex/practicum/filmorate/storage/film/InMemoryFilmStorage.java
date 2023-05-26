package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int id = 1;
    LocalDate date = LocalDate.of(1895, 12, 28);
    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public Map<Integer, Film> findAllFilms() {
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        validateFilm(film);
        if (film.getId() == 0) {
            film.setId(id++);
        }
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film putFilm(Film film) {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Неверный ID.");
        }
        films.put(film.getId(), film);

        return film;
    }

    void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getReleaseDate().isBefore(date)) {
            throw new IncorrectParameterException("Дата выхода фильма ранее 28.12.1895.");
        }
    }

    @Override
    public List<Film> getFilmList() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer filmId) {
        if (!films.containsKey(filmId)) {
            throw new ValidationException("Указанный ID не найден");
        }
        return films.get(filmId);
    }
}
