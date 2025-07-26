package com.example.trivia_backend.models;

public record Category(
        int id,
        String name,
        int totalQuestions
) {}
