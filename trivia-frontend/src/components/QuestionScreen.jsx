import React, { useEffect, useState } from 'react';
import api from '../api/triviaApi.js';
import './QuestionScreen.css';

export default function QuestionScreen() {
  const [question, setQuestion] = useState(null);
  const [selectedAnswer, setSelectedAnswer] = useState(null);
  const [correctAnswer, setCorrectAnswer] = useState(null);
  const [score, setScore] = useState(0);
  const [lives, setLives] = useState(5);
  const [feedback, setFeedback] = useState('');
  const [showNextButton, setShowNextButton] = useState(false);

  const fetchQuestion = async () => {
    const res = await api.get('/game/question');
    setQuestion(res.data);
    setFeedback('');
    setShowNextButton(false);
    setCorrectAnswer(null);
    setSelectedAnswer(null);
  };

const submitAnswer = async (answer) => {
  try {
    setSelectedAnswer(answer);
    const res = await api.post('/game/answer', {
      questionId: question.questionId,
      answer: answer,
    });

    if (res.data.correct) {
      setScore(res.data.score);
      setFeedback('✅ Correct!');
    } else {
      setLives(res.data.livesRemaining);
      setFeedback('❌ Wrong!');
    }

    setCorrectAnswer(res.data.correctAnswer);
    setShowNextButton(true);
  } catch (err) {
    console.error("Error submitting answer:", err);
    setFeedback('⚠️ Failed to submit answer. ' + err);
  }
};

  useEffect(() => {
    fetchQuestion();
  }, []);

  if (!question) return <div>Loading...</div>;

return (
  <div className="container">
    <div className="scorebar">
      <p>Score: {score} | Lives: {lives}</p>
    </div>

    <h1>Trivia Time! :D</h1>
    <h2>{question.question}</h2>
    <ul>
      {question.answers.map((ans) => {
        let className = '';
        if (showNextButton) {
          if (ans === correctAnswer) className = 'correct';
          else if (ans === selectedAnswer) className = 'incorrect';
        }

        return (
          <li key={ans}>
            <button
              onClick={() => submitAnswer(ans)}
              disabled={showNextButton}
              className={className}
            >
              {ans}
            </button>
          </li>
        );
      })}
    </ul>

    <div className="feedback">
      <p style={{ fontWeight: 'bold', visibility: 'visible' }}>
        {feedback || '\u00A0'}
      </p>
    </div>

    {showNextButton && <button onClick={fetchQuestion}>Next Question</button>}
  </div>
  );
}
