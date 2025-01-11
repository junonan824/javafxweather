/*
  JavaFX Boilerplate Project

  https://github.com/akman/javafx-boilerplate

  MIT License (MIT)

  Copyright (C) 2019 - 2024 Alexander Kapitman <akman.ru@gmail.com>

  Permission is hereby granted, free of charge, to any person obtaining
  a copy of this software and associated documentation files (the "Software"),
  to deal in the Software without restriction, including without limitation
  the rights to use, copy, modify, merge, publish, distribute, sublicense,
  and/or sell copies of the Software, and to permit persons to whom
  the Software is furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included
  in all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL
  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
  DEALINGS IN THE SOFTWARE.
*/

package ru.akman.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import ru.akman.model.WeatherData;
import ru.akman.service.WeatherService;

public class PrimaryController {
    @FXML private TextField locationInput;
    @FXML private Label temperatureLabel;
    @FXML private Label humidityLabel;
    @FXML private Label windSpeedLabel;
    @FXML private Label conditionLabel;
    @FXML private ImageView weatherIcon;
    @FXML private ToggleButton unitToggle;
    @FXML private ListView<String> searchHistory;
    @FXML private AnchorPane backgroundPane;

    private WeatherService weatherService;
    private boolean isCelsius = true;

    @FXML
    public void initialize() {
        try {
            weatherService = new WeatherService();
            setupEventHandlers();
            updateBackgroundForTimeOfDay();
        } catch (Exception e) {
            showError("Failed to initialize weather service");
        }
    }

    private void setupEventHandlers() {
        locationInput.setOnAction(e -> fetchWeather());
        unitToggle.setOnAction(e -> {
            isCelsius = !isCelsius;  // Toggle between Celsius and Fahrenheit
            updateTemperatureDisplay();
        });
    }

    @FXML
    private void fetchWeather() {
        String location = locationInput.getText();
        if (location == null || location.trim().isEmpty()) {
            showError("Please enter a location");
            return;
        }

        try {
            WeatherData weather = weatherService.getWeatherForCity(location);
            updateDisplay(weather);
            addToHistory(location);
        } catch (Exception e) {
            showError("Failed to fetch weather data: " + e.getMessage());
        }
    }

    private void updateTemperatureDisplay() {
        if (weatherService == null) return;
        
        try {
            String location = locationInput.getText();
            if (location != null && !location.trim().isEmpty()) {
                WeatherData weather = weatherService.getWeatherForCity(location);
                updateDisplay(weather);
            }
        } catch (Exception e) {
            showError("Failed to update temperature display: " + e.getMessage());
        }
    }

    private void updateDisplay(WeatherData weather) {
        double temp = isCelsius ? weather.getTemperature() : weather.getTemperatureInFahrenheit();
        temperatureLabel.setText(String.format("%.1f°%s", temp, isCelsius ? "C" : "F"));
        humidityLabel.setText(String.format("Humidity: %.1f%%", weather.getHumidity()));
        windSpeedLabel.setText(String.format("Wind: %.1f km/h", weather.getWindSpeed()));
        conditionLabel.setText(weather.getCondition());
        // Update weather icon - implementation needed
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    private void addToHistory(String location) {
        searchHistory.getItems().add(0, 
            String.format("%s - %s", location, java.time.LocalTime.now()));
    }

    private void updateBackgroundForTimeOfDay() {
        int hour = java.time.LocalTime.now().getHour();
        String timeOfDay = (hour >= 6 && hour < 18) ? "day" : "night";
        backgroundPane.getStyleClass().setAll("background-" + timeOfDay);
    }
}
