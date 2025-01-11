package ru.akman.service;

import ru.akman.model.WeatherData;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class WeatherService {
    private final String apiKey;
    private final HttpClient client;
    private static final String API_BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public WeatherService() throws IOException {
        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/config.properties"));
        this.apiKey = props.getProperty("weather.api.key");
        this.client = HttpClient.newHttpClient();
    }

    public WeatherData getWeatherForCity(String city) throws IOException, InterruptedException {
        String url = String.format("%s?q=%s&appid=%s&units=metric", API_BASE_URL, city, apiKey);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();

        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());

        // Parse JSON response and create WeatherData object
        // This is a simplified version - you'll need to add proper JSON parsing
        return new WeatherData(20.0, 65.0, 5.0, "Clear", "01d", city);
    }
} 