package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.*;

@Service
@Slf4j
public class FilmService {
    private final JdbcTemplate jdbcTemplate;
    private final Storage filmStorage;
    private final Storage userStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") Storage filmStorage,
                       @Qualifier("userDbStorage") Storage userStorage, LikeStorage likeStorage, JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.genreStorage = new GenreStorage(jdbcTemplate);
    }

    public List<Film> findFilms() {
        return filmStorage.findAll();
    }

    public Film addFilm(Film film) {
        return (Film) filmStorage.create(film);
    }

    public Film putFilm(Film film) {
        return (Film) filmStorage.put(film);
    }

    public Film getFilmById(long id) {
        return (Film) filmStorage.getById(id);
    }

    public List<Film> getTopFilms(Integer count) {
        Map<Long, Film> filmMap = new HashMap<>(likeStorage.getPopularFilms(count));
        List<Long> ids = new ArrayList<>(filmMap.keySet());
        String sql = genreStorage.getFilmGenresQuery(ids);

        jdbcTemplate.query(sql, rs -> {
            long filmId = rs.getLong("FILM_ID");
            Film film = filmMap.get(filmId);
            if (film != null) {
                int genreId = rs.getInt("GENRE_ID");
                String genreName = rs.getString("GENRE_NAME");
                Genre genre = new Genre(genreId, genreName);
                film.getGenres().add(Objects.requireNonNullElseGet(genre, Genre::new));
            }
        });
        return new ArrayList<>(filmMap.values());
    }

    public void addLikeFilm(long filmId, long userId) {
        filmStorage.getById(filmId);
        userStorage.getById(userId);
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, filmId);
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLikeFilm(long filmId, long userId) {
        filmStorage.getById(filmId);
        userStorage.getById(userId);
        try {
            likeStorage.deleteLike(filmId, userId);
            log.info("Пользователь с id: {} удалил лайк фильму с id: {}", userId, filmId);
        } catch (Exception e) {
            log.info("Лайк пользователя id={} не найден", userId);
        }
    }
}
