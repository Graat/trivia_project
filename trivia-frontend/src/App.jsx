import React, { useState } from 'react';
import StartPage from './components/StartPage.jsx';
import QuestionPage from './components/QuestionPage.jsx';

function App() {
  const [gameStarted, setGameStarted] = useState(false);
  const [difficulty, setDifficulty] = useState('EASY');
  const resetGame = () => { setGameStarted(false); };

  return (
    <div className="App">
      {!gameStarted ? (
        <StartPage
          difficulty={difficulty}
          setDifficulty={setDifficulty}
          onStart={() => setGameStarted(true)}
        />
      ) : (
        <QuestionPage onGameOver={resetGame} />
      )}
    </div>
  );
}
export default App;