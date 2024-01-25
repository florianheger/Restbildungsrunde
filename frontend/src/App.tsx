import { Route, Routes } from 'react-router-dom';
import Users from './pages/Users/Users';


function App() {
	return (
		<Routes>
      <Route path="/" element={<Users/>}/>
		</Routes>
	);
}

export default App;
