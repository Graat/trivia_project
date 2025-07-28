package com.example.trivia_backend.services;

import com.example.trivia_backend.models.Settings;
import org.junit.jupiter.api.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SettingsServiceTest {

    private Path tempFile;

    @AfterEach
    void cleanup() throws IOException {
        if (tempFile != null) {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void loadsSettingsFromFile_whenFileExists() throws IOException {
        String json = """
            {
              "questionsPerRound": 7,
              "maxLives": 4,
              "fetchBatchSize": 12,
              "categorySelectionSize": 5
            }
        """;

        // Create temp json file, load it into the SettingsService and test if it's loaded in correctly.
        tempFile = Files.createTempFile("Settings", ".json");
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write(json);
        }
        SettingsService service = new SettingsService(tempFile.toString());

        Settings settings = service.getSettings();
        assertEquals(7, settings.questionsPerRound());
        assertEquals(4, settings.maxLives());
        assertEquals(12, settings.fetchBatchSize());
        assertEquals(5, settings.categorySelectionSize());
    }

    @Test
    void usesDefaultSettings_whenFileIsMissing() {
        // Attempt to load in a non-existent json file.
        // Then let the SettingsService assign default setting values and check them.
        SettingsService service = new SettingsService("non_existent_path.json");

        Settings settings = service.getSettings();
        assertEquals(4, settings.questionsPerRound());
        assertEquals(5, settings.maxLives());
        assertEquals(10, settings.fetchBatchSize());
        assertEquals(3, settings.categorySelectionSize());
    }
}