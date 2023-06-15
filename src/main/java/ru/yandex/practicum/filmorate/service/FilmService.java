package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    @Qualifier("filmStorage")
    private final Storage filmStorage;
    @Autowired
    @Qualifier("userStorage")
    private final Storage userStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    public FilmService(Storage filmStorage, Storage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Map<Integer, Film> findFilms() {
        log.info("Количество фильмов в фильмотеке: {}", filmStorage.getList().size());
        return filmStorage.findAll();
    }

    public Film addFilm(Film film) {
        log.info("Количество добавленных фильмов: {}", filmStorage.getList().size());
        log.info("Добавление фильма" + film);
        return (Film) filmStorage.create(film);
    }

    public Film putFilm(Film film) {
        log.info("Обновление фильма " + film);
        return (Film) filmStorage.put(film);
    }

    public Integer addLikeFilm(Integer filmId, Integer userId) {
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("Указанный ID не найден");
        }
        Film film = (Film) filmStorage.getById(filmId);
        film.addLike(userId);
        log.info("Добавление Like к фильму " + filmId);
        return userId;
    }


    public Integer deleteLikeFilm(Integer filmId, Integer userId) {
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("Указанный ID не найден");
        }
        Film film = (Film) filmStorage.getById(filmId);
        film.deleteLike(userId);
        log.info("Удаление Like к фильму " + filmId);
        return userId;
    }

    public List<Film> getTopFilms(Integer count) {
        List<Film> popular;
        List<Film> filmList = (List<Film>) filmStorage.getList();
        if (filmStorage.getList().size() <= 1) {
            popular = (List<Film>) filmStorage.getList();
        } else {
            popular = filmList.stream().sorted((film1, film2) -> {

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
        return (Film) filmStorage.getById(filmId);
    }
}
