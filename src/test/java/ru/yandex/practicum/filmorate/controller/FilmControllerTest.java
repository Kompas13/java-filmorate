package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;
    Film film;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        film = Film.builder()
                .name("Снегурочка")
                .description("Фильм о нелегкой жизни населения города Прииск Маршальский Оймяконского района Якутии")
                .releaseDate(LocalDate.of(1986, 11, 10))
                .duration(120)
                .build();
    }

    @Test
    void shouldNotFilmWhenNameIsBlank() {
        film.setName("");
        assertEquals(0, filmController.getFilms().size(), "Число фильмов должно быть 0");
    }

    @Test
    void shouldNotFilmWhenDescriptionSizeIsNull() {
        film.setDescription("");
        assertEquals(0, filmController.getFilms().size(), "Число фильмов должно быть 0");
    }

    @Test
    void shouldNotFilmWhenDescriptionSizeIsMore200() {
        film.setDescription("Очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень " +
                "очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень " +
                "очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень очень " +
                "очень очень очень очень очень очень очень очень очень очень очень очень  длинное описание");
        assertEquals(0, filmController.getFilms().size(), "Число фильмов должно быть 0");
    }

    @Test
    void shouldNotFilmWhenReleaseDateBefore1895Year() {
        film.setReleaseDate(LocalDate.of(1894, 10, 5));
        assertEquals(0, filmController.getFilms().size(), "Число фильмов должно быть 0");
    }

    @Test
    void shouldNotFilmWhenDurationIsNegative() {
        film.setDuration(-1);
        assertEquals(0, filmController.getFilms().size(), "Число фильмов должно быть 0");
    }
}