package com.example.trivia_backend.dto.OpenTriviaAPI;

import java.util.List;

public record OpenTriviaQuestionDTO(
        String question,
        String correct_answer,
        List<String> incorrect_answers,
        String category,
        String type,
        String difficulty
) {}