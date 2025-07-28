package com.example.trivia_backend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.trivia_backend.dto.in.AnswerCheckDTO;
import com.example.trivia_backend.dto.out.AnswerResultDTO;
import com.example.trivia_backend.dto.out.QuestionDTO;
import com.example.trivia_backend.models.Difficulty;
import com.example.trivia_backend.models.Question;
import com.example.trivia_backend.models.Settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

class TriviaServiceTest {

    OpenTriviaComService mockTriviaAPI;
    SettingsService mockSettingsService;
    Settings settings;
    TriviaService service;

    @BeforeEach
    void setUp() {
        mockTriviaAPI = mock(OpenTriviaComService.class);
        settings = new Settings(4, 3, 10, 3); // questionsPerRound=4, maxLives=3, fetchBatchSize=10, categorySelectionSize=3
        mockSettingsService = mock(SettingsService.class);
        when(mockSettingsService.getSettings()).thenReturn(settings);

        service = new TriviaService(mockTriviaAPI, mockSettingsService);
    }

    @Test
    void testStartGame() {
        // No exception means session started
        service.startGame(Difficulty.EASY);
    }

    @Test
    void testFetchNextQuestions_success() {
        service.startGame(Difficulty.EASY);

        // Create 5 questions
        List<Question> mockQuestions = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> new Question(
                        UUID.randomUUID(),
                        "question" + i,
                        "correct",
                        List.of("wrong1", "wrong2", "wrong3"),
                        "category",
                        Difficulty.EASY.name().toLowerCase()
                )).toList();

        when(mockTriviaAPI.fetchQuestions(anyInt(), anyInt(), eq(Difficulty.EASY)))
                .thenReturn(mockQuestions);

        service.fetchNextQuestions();
        // If no exception thrown then success

        // Verify the mock was called at least once
        verify(mockTriviaAPI, atLeastOnce()).fetchQuestions(anyInt(), anyInt(), eq(Difficulty.EASY));
    }

    @Test
    void testFetchNextQuestions_notEnoughQuestions_throws() {
        service.startGame(Difficulty.EASY);

        // Always returns an empty list
        when(mockTriviaAPI.fetchQuestions(anyInt(), anyInt(), eq(Difficulty.EASY)))
                .thenReturn(Collections.emptyList());

        // Check if this throws an exception
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.fetchNextQuestions());
        assertTrue(ex.getMessage().contains("Not enough new trivia questions"));
    }

    @Test
    void testGetNextQuestion_returnsQuestion() {
        service.startGame(Difficulty.EASY);

        List<Question> mockQuestions = IntStream.rangeClosed(1, 4)
                .mapToObj(i -> new Question(
                        UUID.randomUUID(),
                        "question" + i,
                        "correct",
                        List.of("wrong1", "wrong2", "wrong3", "correct"),
                        "category",
                        Difficulty.EASY.name().toLowerCase()
                )).toList();

        when(mockTriviaAPI.fetchQuestions(anyInt(), anyInt(), eq(Difficulty.EASY)))
                .thenReturn(mockQuestions);

        // getNextQuestion triggers lazy loading if no questions
        QuestionDTO questionDTO = service.getNextQuestion();

        // Check if questionDTO is as expected
        assertNotNull(questionDTO);
        assertTrue(questionDTO.question().startsWith("question"));
        assertTrue(questionDTO.answers().contains("correct"));
        assertEquals("category", questionDTO.category());
        assertEquals(Difficulty.EASY.name().toLowerCase(), questionDTO.difficulty());
    }

    @Test
    void testSubmitAnswer_correctAndIncorrect() {
        service.startGame(Difficulty.EASY);

        // Create a question
        UUID qid = UUID.randomUUID();
        Question question = new Question(
                qid,
                "question",
                "correct",
                List.of("wrong1", "wrong2", "wrong3", "correct"),
                "category",
                Difficulty.EASY.name().toLowerCase()
        );

        // Manually set current questions & current question in session via fetchNextQuestions
        when(mockTriviaAPI.fetchQuestions(anyInt(), anyInt(), eq(Difficulty.EASY)))
                .thenReturn(List.of(question));
        service.fetchNextQuestions();

        // Get next question to move it to currentQuestion in session
        QuestionDTO questionDTO = service.getNextQuestion();

        // Submit correct answer
        AnswerCheckDTO correctAnswer = new AnswerCheckDTO(qid.toString(), "correct");
        AnswerResultDTO resultCorrect = service.submitAnswer(correctAnswer);
        assertTrue(resultCorrect.correct());
        assertEquals(3, resultCorrect.livesRemaining()); // Lives start at 3, no loss on correct
        assertEquals(1, resultCorrect.score());    // Score increased by 1

        // Submit incorrect answer (lives should decrease)
        AnswerCheckDTO wrongAnswer = new AnswerCheckDTO(qid.toString(), "wrong1");
        AnswerResultDTO resultWrong = service.submitAnswer(wrongAnswer);
        assertFalse(resultWrong.correct());
        assertEquals(2, resultWrong.livesRemaining()); // One life lost
        assertEquals(1, resultWrong.score());    // Score remains same
    }

    @Test
    void testSubmitAnswer_invalidQuestionId_throws() {
        service.startGame(Difficulty.EASY);

        // Create a question
        Question question = new Question(
                UUID.randomUUID(),
                "Test Question",
                "correct",
                List.of("wrong1", "wrong2", "wrong3"),
                "category",
                Difficulty.EASY.name().toLowerCase()
        );

        when(mockTriviaAPI.fetchQuestions(anyInt(), anyInt(), eq(Difficulty.EASY)))
                .thenReturn(List.of(question));
        service.fetchNextQuestions();
        service.getNextQuestion();

        // Submit with mismatched questionId
        AnswerCheckDTO invalidAnswer = new AnswerCheckDTO(UUID.randomUUID().toString(), "correct");

        // Check if throws an exception
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.submitAnswer(invalidAnswer));
        assertTrue(ex.getMessage().contains("Invalid question ID"));
    }
}