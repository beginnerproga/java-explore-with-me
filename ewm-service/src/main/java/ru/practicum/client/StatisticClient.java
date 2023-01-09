package ru.practicum.client;

import javax.servlet.http.HttpServletRequest;

public interface StatisticClient {
    void addEndpointHit(HttpServletRequest request);

}
