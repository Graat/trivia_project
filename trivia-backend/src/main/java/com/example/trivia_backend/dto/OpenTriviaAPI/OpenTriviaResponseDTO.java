package com.example.trivia_backend.dto.OpenTriviaAPI;

import java.util.List;

public record OpenTriviaResponseDTO(
        int response_code,
        List<OpenTriviaQuestionDTO> results
) {}