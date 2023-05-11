package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectFilmIdException extends RuntimeException {
    public IncorrectFilmIdException(String s) {
        super(s);
    }
}
