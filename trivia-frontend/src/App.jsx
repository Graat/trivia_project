import React, { useState } from 'react';
import StartScreen from './components/StartScreen';
import QuestionScreen from './components/QuestionScreen';

function App() {
  const [gameStarted, setGameStarted] = useState(false);
  const [difficulty, setDifficulty] = useState('easy');

  return (
    <div className="App">
      {!gameStarted ? (
        <StartScreen
          difficulty={difficulty}
          setDifficulty={setDifficulty}
          onStart={() => setGameStarted(true)}
        />
      ) : (
        <QuestionScreen />
      )}
    </div>
  );
}

export default App;