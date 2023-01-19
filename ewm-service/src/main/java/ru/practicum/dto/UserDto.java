package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.util.Create;
import ru.practicum.util.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull(groups = Update.class)
    private Long id;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    @Size(min = 5, max = 50, groups = {Create.class, Update.class})
    private String name;
    @NotNull(groups = {Create.class})
    @Email(groups = {Create.class})
    @Size(min = 5, max = 200, groups = {Create.class, Update.class})
    private String email;
    private Float rating;

}
