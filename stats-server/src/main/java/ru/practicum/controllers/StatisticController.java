package ru.practicum.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.models.ViewStats;
import ru.practicum.servers.StatisticServer;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@Slf4j
public class StatisticController {
    private final StatisticServer statisticServer;

    @Autowired
    public StatisticController(StatisticServer statisticServer) {
        this.statisticServer = statisticServer;
    }

    @PostMapping("/hit")
    public void hit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        statisticServer.addEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(name = "start") String start,
                                    @RequestParam(name = "end") String end,
                                    @RequestParam(name = "uris", required = false) List<String> uris,
                                    @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        return statisticServer.getStats(start, end, uris, unique);
    }

    @GetMapping("/stats/ip")
    public ViewStats getEndpointHitsByApi(@RequestParam(name = "ip") String ip,
                                          @RequestParam(name = "uri") String uri) {
        return statisticServer.getStatsByUriAndIp(uri, ip);
    }

}
