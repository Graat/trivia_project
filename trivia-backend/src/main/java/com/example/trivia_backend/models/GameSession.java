package com.example.trivia_backend.models;
import com.example.trivia_backend.utils.HashUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class GameSession {
    private final UUID sessionId;
    private int score;
    private int livesRemaining;
    private Question currentQuestion;
    private List<Question> currentQuestions;
    private final HashSet<String> askedQuestions;
    private final Difficulty difficulty;

    public GameSession(int startingLives, Difficulty difficulty) {
        this.sessionId = UUID.randomUUID();
        this.score = 0;
        this.livesRemaining = startingLives;
        this.currentQuestion = null;
        this.currentQuestions = new ArrayList<>();
        this.askedQuestions = new HashSet<String>();
        this.difficulty = difficulty;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public int getScore() {
        return score;
    }

    public int getLivesRemaining() {
        return livesRemaining;
    }

    public Question getCurrentQuestion(){
        return currentQuestion;
    }

    public List<Question> getCurrentQuestions(){
        return currentQuestions;
    }

    public Difficulty getDifficulty(){
        return difficulty;
    }

    public void setCurrentQuestion(Question question){
        currentQuestion = question;
    }

    public void setCurrentQuestions(List<Question> questions){
        currentQuestions = questions;
    }

    public void increaseScore(int increment){
        score += increment;
    }

    public void decreaseLives() {
        decreaseLives(1); // default to 1
    }

    public void decreaseLives(int decrement) {
        livesRemaining -= decrement;
    }

    public boolean hasSeen(Question q) {
        return askedQuestions.contains(HashUtils.hashQuestion(q));
    }

    public void addQuestion(Question q) {
        askedQuestions.add(HashUtils.hashQuestion(q));
    }

    public boolean checkAnswer(String answer) {
        return currentQuestion.checkAnswer(answer);
    }

    public boolean isGameOver() {
        return livesRemaining <= 0;
    }
}
