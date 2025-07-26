import React, { useEffect, useState } from 'react';
import api from '../api/triviaApi.js';

export default function QuestionScreen() {
  const [question, setQuestion] = useState(null);
  const [score, setScore] = useState(0);
  const [lives, setLives] = useState(5);
  const [feedback, setFeedback] = useState('');
  const [showNextButton, setShowNextButton] = useState(false);

  const fetchQuestion = async () => {
    const res = await api.get('/game/question');
    setQuestion(res.data);
    setFeedback('');
    setShowNextButton(false);
  };

  const submitAnswer = async (answer) => {
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

    // Disable answer buttons, show "Next Question"
    setShowNextButton(true);
  };

  useEffect(() => {
    fetchQuestion();
  }, []);

  if (!question) return <div>Loading...</div>;

  return (
    <div>
      <h1>Trivia Time! :D</h1>
      <h2>{question.question}</h2>
      <ul>
        {question.answers.map((ans) => (
          <li key={ans}>
            <button
              onClick={() => submitAnswer(ans)}
              disabled={showNextButton} // disable while feedback is shown
            >
              {ans}
            </button>
          </li>
        ))}
      </ul>

      {feedback && <p style={{ fontWeight: 'bold' }}>{feedback}</p>}
      {showNextButton && <button onClick={fetchQuestion}>Next Question</button>}

      <p>Score: {score} | Lives: {lives}</p>
    </div>
  );
}
