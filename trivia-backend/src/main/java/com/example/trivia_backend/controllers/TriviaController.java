package com.example.trivia_backend.controllers;

import com.example.trivia_backend.dto.in.AnswerCheckDTO;
import com.example.trivia_backend.dto.in.StartGameDTO;
import com.example.trivia_backend.dto.out.AnswerResultDTO;
import com.example.trivia_backend.dto.out.QuestionDTO;
import com.example.trivia_backend.models.Difficulty;
import com.example.trivia_backend.services.TriviaService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/game")
public class TriviaController {

    private final TriviaService triviaService;

    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @PostMapping("/start")
    public void startGame(@RequestBody StartGameDTO dto) {
        Difficulty difficulty = dto.getDifficulty();
        triviaService.startGame(difficulty);
    }

    @GetMapping("/question")
    public QuestionDTO getNextQuestion() {
        return triviaService.getNextQuestion();
    }

    @PostMapping("/answer")
    public AnswerResultDTO submitAnswer(@RequestBody AnswerCheckDTO dto) {
        return triviaService.submitAnswer(dto);
    }
}