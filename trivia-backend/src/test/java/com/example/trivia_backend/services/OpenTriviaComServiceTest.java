package com.example.trivia_backend.services;

import com.example.trivia_backend.dto.OpenTriviaAPI.OpenTriviaQuestionDTO;
import com.example.trivia_backend.dto.OpenTriviaAPI.OpenTriviaResponseDTO;
import com.example.trivia_backend.models.Difficulty;
import com.example.trivia_backend.models.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OpenTriviaComServiceTest {

    private RestTemplate restTemplate;
    private OpenTriviaComService service;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        service = new OpenTriviaComService(restTemplate);
    }

    @Test
    void fetchQuestions_successfulResponse_returnsMappedQuestions() {
        OpenTriviaQuestionDTO mockDto = new OpenTriviaQuestionDTO(
                "question",
                "correct",
                List.of("wrong1", "wrong2", "wrong3"),
                "category",
                "multiple",
                "easy"
        );


        OpenTriviaResponseDTO response = new OpenTriviaResponseDTO(0, List.of(mockDto));

        when(restTemplate.getForObject(anyString(), eq(OpenTriviaResponseDTO.class)))
                .thenReturn(response);

        List<Question> questions = service.fetchQuestions(1, 9, Difficulty.EASY);

        assertEquals(1, questions.size());
        assertEquals("question", questions.getFirst().question());
    }

    @Test
    void fetchQuestions_nullResponse_throwsException() {
        when(restTemplate.getForObject(anyString(), eq(OpenTriviaResponseDTO.class)))
                .thenReturn(null);

        assertThrows(RuntimeException.class, () ->
                service.fetchQuestions(1, 9, Difficulty.MEDIUM));
    }

    @Test
    void fetchQuestions_nullResults_throwsException() {
        when(restTemplate.getForObject(anyString(), eq(OpenTriviaResponseDTO.class)))
                .thenReturn(new OpenTriviaResponseDTO(0, null));

        assertThrows(RuntimeException.class, () ->
                service.fetchQuestions(1, 9, Difficulty.HARD));
    }
}
