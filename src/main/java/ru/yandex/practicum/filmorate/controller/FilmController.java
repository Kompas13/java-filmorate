package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectFilmIdException;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Data
@RequestMapping("/films")
public class FilmController {
    private int id = 1;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Количество фильмов в фильмотеке: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new InvalidFilmException("Название фильма не может быть пустым.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new InvalidFilmException("Дата выхода фильма ранее 28.12.1895.");
        }
        if (film.getId() == 0) {
            film.setId(id++);
        }
        films.put(film.getId(), film);
        log.debug("Количество добавленных фильмов: {}", films.size());
        log.debug("Добавление фильма" + film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new InvalidFilmException("Название фильма не может быть пустым.");
        }
        if (!films.containsKey(film.getId())) {
            throw new IncorrectFilmIdException("Неверный ID.");
        }
        films.put(film.getId(), film);
        log.debug("Добавление фильма" + film);
        return film;
    }
}