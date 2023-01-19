package ru.practicum.servers.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.mappers.EndpointHitMapper;
import ru.practicum.models.EndpointHit;
import ru.practicum.models.ViewStats;
import ru.practicum.repositories.EndpointHitRepository;
import ru.practicum.servers.StatisticServer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class StatisticServerImpl implements StatisticServer {
    private final EndpointHitRepository endpointHitRepository;

    @Autowired
    public StatisticServerImpl(EndpointHitRepository endpointHitRepository) {
        this.endpointHitRepository = endpointHitRepository;
    }

    @Override
    public EndpointHitDto addEndpointHit(EndpointHitDto endpointHitDto) {
        log.info("Received request to add endpointHitDto: {}", endpointHitDto);
        EndpointHit endpointHit = EndpointHitMapper.toEndpointHit(endpointHitDto);
        endpointHitRepository.save(endpointHit);
        return EndpointHitMapper.toEndpointHitDto(endpointHit);
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {
        log.info("Received request to get stats");
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (unique)
            return endpointHitRepository.findAllUnique(startDate, endDate, uris);
        else
            return endpointHitRepository.findAll(startDate, endDate, uris);


    }

    @Override
    public ViewStats getStatsByUriAndIp(String uri, String ip) {
        log.info("Received request to get stats by uri and ip");
        return endpointHitRepository.findAllUniqueByUriAndIp(uri, ip);
    }


}
