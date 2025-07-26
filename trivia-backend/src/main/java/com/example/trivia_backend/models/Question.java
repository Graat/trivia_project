package com.example.trivia_backend.models;

import com.example.trivia_backend.dto.OpenTriviaAPI.OpenTriviaQuestionDTO;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Collections;

public record Question(
        UUID id,
        String question,
        String correctAnswer,
        List<String> answers,
        String category,
        String difficulty
) {

    public static Question fromDTO(OpenTriviaQuestionDTO dto) {
        List<String> allAnswers = new ArrayList<>(dto.incorrect_answers());
        allAnswers.add(dto.correct_answer());
        Collections.shuffle(allAnswers);

        return new Question(
                UUID.randomUUID(),
                dto.question(),
                dto.correct_answer(),
                List.copyOf(allAnswers), // Ensure immutability
                dto.category(),
                dto.difficulty()
        );
    }

    public boolean checkAnswer(String givenAnswer) {
        return correctAnswer.equals(givenAnswer);
    }
}
