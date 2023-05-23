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
                .name("Avatar")
                .description("In 2154, the natural resources of the Earth have been depleted. The Resources Development Administration (RDA) mines the valuable mineral unobtanium on Pandora, a moon in the Alpha Centauri star system. Pandora, whose atmosphere is inhospitable to humans, is inhabited by the Na'vi, 10-foot-tall (3.0 m), blue-skinned, sapient humanoids that live in harmony with nature. To explore Pandora, genetically matched human scientists use Na'vi-human hybrids called avatars.")
                .releaseDate(LocalDate.of(2009, 12, 10))
                .duration(162)
                .build();
        filmController.add(film);
    }

    @Test
    void addFilmInController() {
        assertEquals(1, filmController.getFilms().size(), "Amount of films must be 1");
    }

    @Test
    void putFilmInController() {
        film.setId(1);
        film.setDuration(161);
        filmController.put(film);
        assertEquals("{1=Film(name=Avatar, description=In 2154, the natural resources of the Earth have been depleted. The Resources Development " +
                        "Administration (RDA) mines the valuable mineral unobtanium on Pandora, a moon in the Alpha Centauri star system. Pandora, whose atmosphere " +
                        "is inhospitable to humans, is inhabited by the Na'vi, 10-foot-tall (3.0 m), blue-skinned, sapient humanoids that live in harmony with nature. " +
                        "To explore Pandora, genetically matched human scientists use Na'vi-human hybrids called avatars., releaseDate=2009-12-10, duration=161, id=1)}",
                filmController.getFilms().toString(), "Info about film is not correct");
    }

    @Test
    void findAllFilms() {
        assertEquals("[Film(name=Avatar, description=In 2154, the natural resources of the Earth have been depleted. " +
                        "The Resources Development Administration (RDA) mines the valuable mineral unobtanium on Pandora, a moon in " +
                        "the Alpha Centauri star system. Pandora, whose atmosphere is inhospitable to humans, is inhabited by the " +
                        "Na'vi, 10-foot-tall (3.0 m), blue-skinned, sapient humanoids that live in harmony with nature. To explore " +
                        "Pandora, genetically matched human scientists use Na'vi-human hybrids called avatars., releaseDate=2009-12-10, duration=162, id=1)]",
                filmController.findAll().toString(), "\"Info about film is not correct");
    }
}