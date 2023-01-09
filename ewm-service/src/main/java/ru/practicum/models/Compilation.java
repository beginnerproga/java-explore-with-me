package ru.practicum.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {
    @ManyToMany
    @JoinTable(name = "compilations_events",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    private List<Event> events;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "pinned")
    private Boolean pinned;

    {
        events = new ArrayList<>();
    }

}
