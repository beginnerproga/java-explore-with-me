package ru.practicum.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.client.Hit;
import ru.practicum.client.StatisticClient;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticClientImpl implements StatisticClient {
    private final String url;//URI сервиса

    public StatisticClientImpl(@Value("${explore-with-me-stats-service.url}") String url) {
        this.url = url;
    }

    public static String toRequestParam(Map<String, String> data) {
        StringBuilder string = new StringBuilder("?");
        for (Map.Entry<String, String> a : data.entrySet()) {
            string.append(a.getKey()).append("=").append(a.getValue()).append("&");
        }
        string.delete(string.length() - 1, string.length());
        return string.toString();
    }

    @Override
    @SneakyThrows
    public void addEndpointHit(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //Название сервиса
        String app = "ewm-service";
        Hit hit = new Hit(
                app,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        String requestBody = objectMapper.writeValueAsString(hit);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url + "/hit"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    @Override
    @SneakyThrows
    public Map<Long, Long> getStats(List<Long> eventIds) {
        Map<String, String> data = new HashMap<>();
        data.put("start", "2023-01-10+11:30:35");
        data.put("end", "2050-01-01+12:00:00");
        StringBuilder uris = new StringBuilder();
        for (long eventId : eventIds) {
            uris.append("/events/").append(eventId).append(",");
        }
        uris.deleteCharAt(uris.lastIndexOf(","));

        data.put("uris", uris.toString());
        data.put("unique", "false");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url + "/stats" + toRequestParam(data)))
                .GET()
                .header("Accept", "application/json")
                .timeout(Duration.ofSeconds(10))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        JSONArray json = new JSONArray(response.body());
        HashMap<String, Long> answer = new HashMap<>();
        if (json.length() != 0) {
            for (Long ignored : eventIds) {
                answer.put(json.getJSONObject(0).getString("uri"),
                        json.getJSONObject(0).getLong("hits"));
            }
            Map<Long, Long> answerInLong = new HashMap<>();
            for (String a : answer.keySet()) {
                answerInLong.put(Long.parseLong(a.substring(8)), answer.get(a));
            }
            return answerInLong;
        } else
            return Collections.EMPTY_MAP;
    }

}


