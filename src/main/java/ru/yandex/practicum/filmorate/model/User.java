package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
public class User {
    @NotBlank
    @Pattern(regexp = "\\S*$")
    private String login;
    private String name;
    private int id;
    @NotBlank
    @Email
    private String email;
    @PastOrPresent
    private LocalDate birthday;

    public String getName() {
        if (name == null || name.isBlank()) {
            name = login;
        }
        return name;
    }
}
