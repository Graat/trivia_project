package com.example.trivia_backend.dto.in;

public record AnswerCheckDTO(
        String questionId,
        String answer
) {}