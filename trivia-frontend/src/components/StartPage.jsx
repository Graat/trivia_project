import React, { useState } from 'react';
import api from '../api/triviaApi';
import './StartPage.css';
import './SharedStyles.css';
import TriviaTime from '../assets/triviatime.svg';

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
        <h1 className='text'>Welcome to</h1>
        <img src={TriviaTime} alt="Trivia Time" className="triviatime" />
        <div className="start-button">
          <button onClick={startGame}>Start</button>
        </div>

        <div className="difficulty-buttons">
          {['EASY', 'MEDIUM', 'HARD'].map((level) => {
            const formatted = level.charAt(0) + level.slice(1).toLowerCase();
            return (
              <button
                key={level}
                onClick={() => setDifficulty(level)}
                className={difficulty === level ? 'selected' : ''}>
                {formatted}
              </button>
            );
          })}
        </div>
      </div>
    </div>
  );
}
