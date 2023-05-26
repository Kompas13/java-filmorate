package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Map<Integer, Film> findAllFilms();

    Film addFilm(Film film);

    Film putFilm(Film film);

    List<Film> getFilmList();

    Film getFilmById(Integer filmId);
}
