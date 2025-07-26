package com.example.trivia_backend.dto.in;

import com.example.trivia_backend.models.Difficulty;

public record StartGameDTO(String difficulty)
{
    public Difficulty getDifficulty() {
        try {
            return Difficulty.valueOf(difficulty.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return Difficulty.MEDIUM; // default fallback
        }
    }
}