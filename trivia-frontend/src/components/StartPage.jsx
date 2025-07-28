import React, { useState } from 'react';
import api from '../api/triviaApi';
import './StartPage.css';
import './SharedStyles.css';

export default function StartPage({ difficulty, setDifficulty, onStart }) {

  const startGame = async () => {
    try {
      await api.post('game/start', { difficulty });
      onStart();
    } catch (error) {
      console.error('Failed to start game:', error);
    }
  };

  return (
    <div className='center-page'>
      <div className='content-box'>
        <h1 className='text'>Welcome to Trivia Time!</h1>

        <div className="start-button">
          <button onClick={startGame}>Start</button>
        </div>

        <div className="difficulty-buttons">
          {['EASY', 'MEDIUM', 'HARD'].map((level) => {
            const formatted = level.charAt(0) + level.slice(1).toLowerCase(); // PascalCase
            return (
              <button
                key={level}
                onClick={() => setDifficulty(level)}
                className={difficulty === level ? 'selected' : ''}
              >
                {formatted}
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
}
