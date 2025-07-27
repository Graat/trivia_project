import React, { useEffect, useState } from 'react';
import api from '../api/triviaApi.js';
import './QuestionScreen.css';
import DOMPurify from 'dompurify';


import TriviaTime from '../assets/triviatime.svg';



export default function QuestionScreen() {
  const [question, setQuestion] = useState(null);
  const [selectedAnswer, setSelectedAnswer] = useState(null);
  const [correctAnswer, setCorrectAnswer] = useState(null);
  const [score, setScore] = useState(0);
  const [lives, setLives] = useState(5);
  const [feedback, setFeedback] = useState('');
  const [showNextButton, setShowNextButton] = useState(false);
  const sanitizer = DOMPurify.sanitize;
  const renderHtml = (htmlString) => (<span dangerouslySetInnerHTML={{ __html: sanitizer(htmlString) }} />);

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
  <div style={{
        position: 'absolute', left: '50%', top: '50%',
        transform: 'translate(-50%, -50%)'
      }}>
    <div className="scorebar">
      <p className='question-text'>Score: {score} | Lives: {lives}</p>
    </div>

    <div className="content-box">
      <img src={TriviaTime} alt="Trivia Time" className="triviatime" />
    <h2 className='question-text'>{renderHtml(question.question)}</h2>

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
                {renderHtml(ans)}
              </button>
            </li>
          );
        })}
      </ul>

      {/* Reserve height to prevent jumping */}
      <div className="feedback-area">
        <p className='question-text'>{feedback || '\u00A0'}</p>
        {showNextButton && <button onClick={fetchQuestion}>Next Question</button>}
      </div>
    </div>
  </div>
);
}
