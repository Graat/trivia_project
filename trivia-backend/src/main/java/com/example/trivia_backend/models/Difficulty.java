package com.example.trivia_backend.models;

public enum Difficulty {
    EASY,
    MEDIUM,
    HARD;

    public String toApiValue() {
        return this.name().toLowerCase(); // For URL usage
    }
}