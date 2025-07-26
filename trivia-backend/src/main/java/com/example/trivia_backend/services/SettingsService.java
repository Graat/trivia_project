package com.example.trivia_backend.services;

import com.example.trivia_backend.models.Settings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class SettingsService {

    private final Settings settings;

    public SettingsService() {
        this.settings = loadSettingsFromFile("src/main/java/com/example/trivia_backend/resources/cache/Settings.json");
    }

    private Settings loadSettingsFromFile(String path) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(path), Settings.class);
        } catch (IOException e) {
            // TODO: Log a warning when settings file fails to load
            return getDefaultSettings();
        }
    }

    public Settings getSettings() {
        return settings;
    }

    private Settings getDefaultSettings() {
        return new Settings(4, 5, 10, 3);
    }
}