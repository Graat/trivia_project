import React, { useState } from 'react';
import api from '../api/triviaApi';

export default function StartScreen({ difficulty, setDifficulty, onStart }) {

  const startGame = async () => {
    try {
      await api.post('game/start', { difficulty });
      onStart();
    } catch (error) {
      console.error('Failed to start game:', error);
    }
  };

  return (
    <div>
      <h1>Welcome to Trivia!</h1>
      <div>
        {['EASY', 'MEDIUM', 'HARD'].map((level) => (
          <button
            key={level}
            onClick={() => setDifficulty(level)}
            className={difficulty === level ? 'selected' : ''}
          >
            {level}
          </button>
        ))}
      </div>
      <button onClick={startGame}>Start</button>
    </div>
  );
}
