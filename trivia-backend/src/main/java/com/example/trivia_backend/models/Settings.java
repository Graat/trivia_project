package com.example.trivia_backend.models;

public record Settings(
        int questionsPerRound,
        int maxLives,
        int fetchBatchSize,
        int categorySelectionSize
) {}