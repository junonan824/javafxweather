package ru.akman.model;

import java.time.LocalDateTime;

public class WeatherData {
    private double temperature;
    private double humidity;
    private double windSpeed;
    private String condition;
    private String iconCode;
    private LocalDateTime timestamp;
    private String location;

    public WeatherData(double temperature, double humidity, double windSpeed, 
                      String condition, String iconCode, String location) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.condition = condition;
        this.iconCode = iconCode;
        this.location = location;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and setters
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public String getCondition() { return condition; }
    public String getIconCode() { return iconCode; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getLocation() { return location; }

    public double getTemperatureInFahrenheit() {
        return (temperature * 9/5) + 32;
    }
} 