package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.util.Create;
import ru.practicum.util.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    @NotNull(groups = Update.class)
    private Long id;
    @NotNull(groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class, Update.class})
    @Size(min = 5, max = 50, groups = {Create.class, Update.class})
    private String name;
}