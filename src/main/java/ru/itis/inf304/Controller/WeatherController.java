package ru.itis.inf304.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.inf304.Service.WeatherService;
import ru.itis.inf304.WeatherLogRepository;
import ru.itis.inf304.WeatherLog;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherLogRepository weatherLogRepository;

    @GetMapping("/city/{city}")
    public ResponseEntity<WeatherLog> getWeather(@PathVariable("city") String city) {
        WeatherLog weatherLog = weatherService.getWeather(city);
        weatherLogRepository.save(weatherLog);
        return ResponseEntity.ok(weatherLog);
    }

    @PostMapping
    public ResponseEntity<WeatherLog> createLog(@RequestBody WeatherLog log) {
        WeatherLog savedLog = weatherLogRepository.save(log);
        return ResponseEntity.ok(savedLog);
    }

    @GetMapping
    public ResponseEntity<List<WeatherLog>> getAllLogs() {
        return ResponseEntity.ok(weatherLogRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherLog> getLogById(@PathVariable("id") Long id) {
        return weatherLogRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeatherLog> updateLog(@PathVariable("id") Long id, @RequestBody WeatherLog updatedLog) {
        return weatherLogRepository.findById(id)
                .map(existingLog -> {
                    existingLog.setCity(updatedLog.getCity());
                    existingLog.setTemperature(updatedLog.getTemperature());
                    existingLog.setDescription(updatedLog.getDescription());

                    WeatherLog savedLog = weatherLogRepository.save(existingLog);
                    return ResponseEntity.ok(savedLog);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable("id") Long id) {
        weatherLogRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}