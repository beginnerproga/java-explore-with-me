package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.EndpointHit;
import ru.practicum.models.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.models.ViewStats(e.app, e.uri, COUNT (e.ip)) " +
            "from EndpointHit e WHERE e.timestamp>= ?1 AND e.timestamp<= ?2 and e.uri in ?3 GROUP BY e.app, e.uri")
    List<ViewStats> findAll(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.models.ViewStats(e.app, e.uri, COUNT (DISTINCT e.ip)) from " +
            "EndpointHit e WHERE e.timestamp>= ?1 AND e.timestamp<= ?2 and e.uri in ?3 GROUP BY e.app, e.uri")
    List<ViewStats> findAllUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.models.ViewStats(e.app, e.uri, COUNT (DISTINCT e.ip)) from " +
            "EndpointHit e WHERE e.uri like :uri and e.ip like :ip GROUP BY e.app, e.uri")
    ViewStats findAllUniqueByUriAndIp(String uri, String ip);
}

