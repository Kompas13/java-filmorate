package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectUserIdException extends RuntimeException {
    public IncorrectUserIdException(String s) {
        super(s);
    }
}
