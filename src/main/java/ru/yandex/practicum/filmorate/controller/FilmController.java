package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")

public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmService.findFilms().values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {

        return filmService.putFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Integer addLikeFilm(@PathVariable(name = "filmId") Integer filmId, @PathVariable(name = "userId") Integer userId) {
        return filmService.addLikeFilm(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Integer deleteLikeFilm(@PathVariable(name = "filmId") Integer filmId, @PathVariable(name = "userId") Integer userId) {
        return filmService.deleteLikeFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable(name = "filmId") Integer filmId) {
        return filmService.getFilmById(filmId);
    }
}
