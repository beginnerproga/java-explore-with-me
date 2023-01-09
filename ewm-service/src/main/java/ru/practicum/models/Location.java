package ru.practicum.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@Embeddable
public class Location {
    private Float lat;
    private Float lon;
}
