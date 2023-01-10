package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.util.Create;
import ru.practicum.util.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    @NotNull(groups = {Update.class})
    private Long id;
    @NotBlank(groups = {Create.class})
    @Size(max = 100, groups = {Create.class})
    private String title;
    @NotNull(groups = {Create.class})
    private Boolean pinned = false;
    @NotNull(groups = {Create.class})
    private List<Long> events = new ArrayList<>();
}
