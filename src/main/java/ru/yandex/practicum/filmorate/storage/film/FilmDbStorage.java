package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
@Slf4j
public class FilmDbStorage implements Storage<Film> {
    private final RatingStorage ratingStorage;
    private final GenreStorage genreStorage;
    private final JdbcTemplate jdbcTemplate;
    LocalDate date = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmDbStorage(RatingStorage ratingStorage, GenreStorage genreStorage, JdbcTemplate jdbcTemplate) {
        this.ratingStorage = ratingStorage;
        this.genreStorage = genreStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, mr.RATING_ID, mr.NAME AS RATING_NAME, mr.DESCRIPTION AS RATING_DESCRIPTION " + "FROM FILM f " + "LEFT JOIN MPA_RATING mr ON f.RATING_ID = mr.RATING_ID";
        List<Film> filmList = jdbcTemplate.query(sql, FilmDbStorage::mapFilm);
        for (Film film : filmList) {
            List<Long> idList = new ArrayList<>(List.of(film.getId()));
            String genreSql = genreStorage.getFilmGenresQuery(idList);
            film.setGenres(new HashSet<>());
            jdbcTemplate.query(genreSql, rs -> {
                int genreId = rs.getInt("GENRE_ID");
                String genreName = rs.getString("GENRE_NAME");
                Genre genre = new Genre(genreId, genreName);
                film.getGenres().add(genre);
            });
        }
        return filmList;
    }

    Film mapFilm(ResultSet resultSet) throws SQLException {
        return Film.builder().id(resultSet.getLong("film_id")).name(resultSet.getString("name")).description(resultSet.getString("description")).releaseDate(resultSet.getDate("release_date").toLocalDate()).duration(resultSet.getInt("duration")).build();
    }


    @Override
    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(date)) {
            throw new ValidationException("Дата выхода фильма ранее 28.12.1895.");
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("film").usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(FilmtoMap(film)).longValue());
        film.setMpa(ratingStorage.getMPAById(film.getMpa().getId()));
        if (film.getGenres() != null) {
            genreStorage.updateFilmGenre(film);
        }
        log.info("Добавлен фильм: {} {}", film.getId(), film.getName());
        return film;
    }

    @Override
    public Film put(Film film) {
        if (!filmExists(film.getId())) {
            log.info("Фильм не найден");
            throw new NotFoundException("Фильм не найден");
        }
        String sql = "update film set " + "name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " + "where film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        film.setMpa(ratingStorage.getMPAById(film.getMpa().getId()));
        if (film.getGenres() != null) {
            Collection<Genre> genres = film.getGenres().stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toList());
            film.setGenres(new LinkedHashSet<>(genres));
            for (Genre genre : film.getGenres()) {
                genre.setName(genreStorage.getGenreById(genre.getId()).getName());
            }
        }
        genreStorage.updateFilmGenre(film);
        log.info("Фильм с id: {} обновлен", film.getId());
        return film;
    }

    @Override
    public Film getById(long id) {
        if (!filmExists(id)) {
            log.info("Фильм не найден");
            throw new NotFoundException("Фильм не найден");
        }
        String filmSql = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, mr.RATING_ID, mr.NAME AS RATING_NAME, mr.DESCRIPTION AS RATING_DESCRIPTION " + "FROM FILM f " + "LEFT JOIN MPA_RATING mr ON f.RATING_ID = mr.RATING_ID " + "WHERE f.FILM_ID = ?";
        Film film = jdbcTemplate.queryForObject(filmSql, FilmDbStorage::mapFilm, id);

        List<Long> idList = new ArrayList<>(List.of(film.getId()));
        String genreSql = genreStorage.getFilmGenresQuery(idList);
        film.setGenres(new HashSet<>());
        jdbcTemplate.query(genreSql, rs -> {
            int genreId = rs.getInt("GENRE_ID");
            String genreName = rs.getString("GENRE_NAME");
            Genre genre = new Genre(genreId, genreName);
            film.getGenres().add(genre);
        });

        return film;
    }

    public static Film mapFilm(ResultSet rs, int rowNum) throws SQLException {
        RatingMPA mpa = new RatingMPA(rs.getInt("RATING_ID"), rs.getString("RATING_NAME"), rs.getString("RATING_DESCRIPTION"));
        Set<Genre> genres = new HashSet<>();
        return Film.builder().id(rs.getLong("FILM_ID")).name(rs.getString("NAME")).description(rs.getString("DESCRIPTION")).releaseDate(rs.getDate("RELEASE_DATE").toLocalDate()).duration(rs.getInt("DURATION")).mpa(mpa).genres(genres).build();
    }

    private boolean filmExists(long filmId) {
        String sqlQuery = "SELECT COUNT(*) FROM FILM WHERE FILM_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
        return Optional.ofNullable(count).map(c -> c > 0).orElse(false);
    }

    private Map<String, Object> FilmtoMap(Film film) {
        Map<String, Object> films = new HashMap<>();
        films.put("name", film.getName());
        films.put("description", film.getDescription());
        films.put("release_date", film.getReleaseDate());
        films.put("duration", film.getDuration());
        films.put("rating_id", film.getMpa().getId());
        return films;
    }
}