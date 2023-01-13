package ru.practicum.client.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatisticClient;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
class Hit {
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}

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
    public Long getStats(long eventId) {
        Map<String, String> data = new HashMap<>();
        data.put("start", "2023-01-10+11:30:35");
        data.put("end", "2050-01-01+12:00:00");
        data.put("uris", "/events/" + eventId);
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
        if (json.length() != 0) {
            return json.getJSONObject(0).getLong("hits");
        } else return 0L;
    }

}


