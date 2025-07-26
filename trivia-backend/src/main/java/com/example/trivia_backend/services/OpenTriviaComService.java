package com.example.trivia_backend.services;

import com.example.trivia_backend.dto.OpenTriviaAPI.OpenTriviaQuestionDTO;
import com.example.trivia_backend.dto.OpenTriviaAPI.OpenTriviaResponseDTO;
import com.example.trivia_backend.models.Difficulty;
import com.example.trivia_backend.models.Question;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OpenTriviaComService {

    private final RestTemplate restTemplate;

    // Inject base URL from application.properties or default
    @Value("${opentdb.api.url:https://opentdb.com/api.php}")
    private String baseUrl;

    public OpenTriviaComService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // fetch an amount of
    public List<Question> fetchQuestions(int amount, int categoryId, Difficulty difficulty) {
//        String url = String.format("%s?amount=%d&category=%d&difficulty=%s&type=multiple",
//                baseUrl, amount, categoryId, difficulty.name().toLowerCase());

        String url = String.format("%s?amount=%d&&difficulty=%s&type=multiple",
                baseUrl, amount, difficulty.name().toLowerCase());

        OpenTriviaResponseDTO response = restTemplate.getForObject(url, OpenTriviaResponseDTO.class);

        if (response == null || response.results() == null) {
            throw new RuntimeException("Failed to fetch trivia questions.");
        }

        return response.results().stream()
                .map(OpenTriviaComService::mapToQuestion)
                .toList();
    }

    private static Question mapToQuestion(OpenTriviaQuestionDTO dto) {
        return Question.fromDTO(dto);
    }
}