package com.example.trivia_backend.controllers;

import com.example.trivia_backend.models.Difficulty;
import com.example.trivia_backend.services.TriviaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/debug")
public class DebugController {
    private final TriviaService triviaService;

    public DebugController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @GetMapping("/questionFormat")
    public String getTriviaQuestionsRaw() {
        String triviaUrl = "https://opentdb.com/api.php?amount=5&type=multiple";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(triviaUrl, String.class);

        return response;
    }

    @GetMapping("/question")
    public String getTriviaQuestionRaw() {
        triviaService.startGame(Difficulty.EASY);
        triviaService.fetchNextQuestions();
        String response = triviaService.getNextQuestion().toString();
        return response;
    }

}