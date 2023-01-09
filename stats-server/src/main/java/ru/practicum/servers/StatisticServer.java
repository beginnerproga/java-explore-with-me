package ru.practicum.servers;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.models.ViewStats;

import java.util.List;

public interface StatisticServer {
    EndpointHitDto addEndpointHit(EndpointHitDto endpointHitDto);

    List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique);

}
