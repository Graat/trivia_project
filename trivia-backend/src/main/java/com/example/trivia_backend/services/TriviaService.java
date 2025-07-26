package com.example.trivia_backend.services;

import com.example.trivia_backend.dto.in.AnswerCheckDTO;
import com.example.trivia_backend.dto.out.QuestionDTO;
import com.example.trivia_backend.dto.out.AnswerResultDTO;
import com.example.trivia_backend.models.Difficulty;
import com.example.trivia_backend.models.GameSession;
import com.example.trivia_backend.models.Question;
import com.example.trivia_backend.models.Settings;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TriviaService {

    private final OpenTriviaComService triviaAPI;
    private final Settings settings;

    private GameSession session;

    public TriviaService(OpenTriviaComService triviaAPI, SettingsService settingsService) {
        this.triviaAPI = triviaAPI;
        this.settings = settingsService.getSettings();
    }

    public void startGame(Difficulty difficulty) {
        this.session = new GameSession(settings.maxLives(), difficulty);
    }

    // TODO: include the CategoryDTO
    public void fetchNextQuestions(){
        if (session == null || session.isGameOver()) {
            throw new IllegalStateException("No active game session.");
        }

        int desired = settings.questionsPerRound();
        int batchSize = settings.fetchBatchSize();
        int categoryId = 2; // TODO: adjust when CategoryDTO is given
        Difficulty difficulty = session.getDifficulty();

        List<Question> unseenQuestions = new ArrayList<>();

        int safetyLimit = 10;
        int loops = 0;
        while (unseenQuestions.size() < desired && loops < safetyLimit) {
            loops++;
            List<Question> fetched = triviaAPI.fetchQuestions(batchSize, categoryId, difficulty);

            // Filter out questions that have been seen before using their hash
            List<Question> newUnseen = fetched.stream()
                    .filter(q -> !session.hasSeen(q))
                    .toList();

            System.out.println("Fetched questions: " + fetched.size());
            System.out.println("Unseen questions: " + newUnseen.size());

            unseenQuestions.addAll(newUnseen);

            // Break to prevent infinite loop in case fetched would always return empty.
            if (fetched.isEmpty()) {
                break;
            }
        }

        if (unseenQuestions.size() < desired) {
            throw new RuntimeException("Not enough new trivia questions available.");
        }

        // Keep only the desired amount of questions
        List<Question> selected = unseenQuestions.subList(0, desired);
        session.setCurrentQuestions(selected);
    }

    public QuestionDTO getNextQuestion() {
        if (session == null || session.isGameOver()) {
            throw new IllegalStateException("No active game session.");
        }
        if (session.getCurrentQuestions().isEmpty()) {
            fetchNextQuestions(); // Lazy loading fallback
        }
        Question question = session.getCurrentQuestions().removeFirst();
        session.setCurrentQuestion(question);
        session.addQuestion(question);
        return QuestionDTO.from(question);
    }

    public AnswerResultDTO submitAnswer(AnswerCheckDTO answerDTO) {
        System.out.println("before session check");
        if (session == null) {
            throw new IllegalStateException("Game not started.");
        }
        System.out.println("before Id check");
        if (!session.getCurrentQuestion().id().toString().equals(answerDTO.questionId())) {
            throw new IllegalArgumentException("Invalid question ID");
        }
        System.out.println("Hello, world!");
        boolean isCorrect = session.checkAnswer(answerDTO.answer());
        if (isCorrect) {
            session.increaseScore(1);
        }
        else{
            session.decreaseLives();
        }
        return AnswerResultDTO.from(session, isCorrect);
    }
}