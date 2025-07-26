package com.example.trivia_backend.dto.out;

import java.util.List;
import com.example.trivia_backend.models.Question;

public record QuestionDTO(
        String questionId,
        String question,
        List<String> answers,
        String category,
        String difficulty
) {
    public static QuestionDTO from(Question q) {
        return new QuestionDTO(
                q.id().toString(),
                q.question(),
                q.answers(),
                q.category(),
                q.difficulty()
        );
    }
}