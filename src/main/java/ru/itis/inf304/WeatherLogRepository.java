package ru.itis.inf304;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class WeatherLogRepository {

    private final List<WeatherLog> logs = new ArrayList<>();
    private long nextId = 1;

    public List<WeatherLog> findAll() {
        return logs;
    }

    public Optional<WeatherLog> findById(Long id) {
        return logs.stream().filter(log -> log.getId().equals(id)).findFirst();
    }

    public WeatherLog save(WeatherLog log) {
        if (log.getId() == null) {
            log.setId(nextId++);
        }
        logs.removeIf(l -> l.getId().equals(log.getId()));
        logs.add(log);
        return log;
    }

    public void deleteById(Long id) {
        logs.removeIf(log -> log.getId().equals(id));
    }
}
