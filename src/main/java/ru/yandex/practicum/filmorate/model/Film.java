package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Film {
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    @NotNull(message = "Не задана продолжительность фильма.")
    private int duration;
    @Builder.Default
    private int id;
    private final Set<Integer> likesUser = new HashSet<>();

    public void addLike(Integer userId) {
        likesUser.add(userId);
    }

    public void deleteLike(Integer userId) {
        likesUser.remove(userId);
    }

    public Set<Integer> getLikesUser() {
        return new HashSet<>(likesUser);
    }
}


