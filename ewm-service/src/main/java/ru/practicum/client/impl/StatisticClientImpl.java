package ru.practicum.client.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatisticClient;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class StatisticClientImpl implements StatisticClient {
    private final String app = "ewm-service"; //Название сервиса
    private final String url;//URI сервиса

    public StatisticClientImpl(@Value("${explore-with-me-stats-service.url}") String url) {
        this.url = url;
    }

    @Override
    @SneakyThrows
    public void addEndpointHit(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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

    @Data
    @AllArgsConstructor
    private static class Hit {
        private String app;
        private String uri;
        private String ip;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime timestamp;
    }

}
