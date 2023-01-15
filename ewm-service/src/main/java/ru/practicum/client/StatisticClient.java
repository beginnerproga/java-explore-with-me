package ru.practicum.client;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface StatisticClient {
    void addEndpointHit(HttpServletRequest request);

    Map<Long,Long> getStats(List<Long> eventIds);

}
