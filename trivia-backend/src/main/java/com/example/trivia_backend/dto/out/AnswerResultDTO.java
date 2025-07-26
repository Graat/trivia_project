package com.example.trivia_backend.dto.out;

import com.example.trivia_backend.models.GameSession;

public record AnswerResultDTO(
        int score,
        int livesRemaining,
        boolean correct,
        boolean gameOver
) {
    public static AnswerResultDTO from(GameSession session, boolean correct) {
        return new AnswerResultDTO(
                session.getScore(),
                session.getLivesRemaining(),
                correct,
                session.isGameOver()
        );
    }
}