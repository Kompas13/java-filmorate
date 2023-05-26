package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Map<Integer, Film> findFilms() {
        log.info("Количество фильмов в фильмотеке: {}", filmStorage.getFilmList().size());
        return filmStorage.findAllFilms();
    }

    public Film addFilm(Film film) {
        log.info("Количество добавленных фильмов: {}", filmStorage.getFilmList().size());
        log.info("Добавление фильма" + film);
        return filmStorage.addFilm(film);
    }

    public Film putFilm(Film film) {
        log.info("Обновление фильма " + film);
        return filmStorage.putFilm(film);
    }

    public Integer addLikeFilm(Integer filmId, Integer userId) {
        if (filmId < 1 || userId < 1) {
            throw new ValidationException("Некорректный ID");
        }
        filmStorage.getFilmById(filmId).getLikesUser().add(userId);
        log.info("Добавление Like к фильму " + filmId);
        return userId;
    }


    public Integer deleteLikeFilm(Integer filmId, Integer userId) {
        if (filmId < 1 || userId < 1) {
            throw new ValidationException("Некорректный ID");
        }
        filmStorage.getFilmById(filmId).getLikesUser().remove(userId);
        log.info("Удаление Like к фильму " + filmId);
        return userId;
    }

    public List<Film> getTopFilms(Integer count) {
        List<Film> popular;
        if (filmStorage.getFilmList().size() <= 1) {
            popular = filmStorage.getFilmList();
        } else {
            popular = filmStorage.getFilmList().stream().sorted((film1, film2) -> {
                if (film1.getLikesUser().size() == film2.getLikesUser().size())
                    return 0;
                else if (film1.getLikesUser().size() > film2.getLikesUser().size()) {
                    return -1;//обратная сортировка от популярного
                } else {
                    return 1;
                }
            })
                    .limit(count).collect(Collectors.toList());
        }
        log.info("Популярные фильмы: {}", popular);
        return popular;
    }

    public Film getFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }
}
