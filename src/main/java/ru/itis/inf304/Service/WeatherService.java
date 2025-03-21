package ru.itis.inf304.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.inf304.WeatherApiResponse;
import ru.itis.inf304.WeatherLog;

@Service
public class WeatherService {

    private static final String API_KEY = "bd5e378503939ddaee76f12ad7a97608";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather";

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherLog getWeather(String city) {
        String url = API_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
        WeatherApiResponse response = restTemplate.getForObject(url, WeatherApiResponse.class);

        if (response == null || response.getMain() == null || response.getWeather() == null || response.getWeather().isEmpty()) {
            throw new RuntimeException("Не удалось получить данные о погоде для города: " + city);
        }

        WeatherLog log = new WeatherLog();
        log.setCity(city);
        log.setTemperature(response.getMain().getTemp() + "°C");
        log.setDescription(response.getWeather().get(0).getDescription());

        return log;
    }
}