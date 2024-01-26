import { Route, Routes } from 'react-router-dom';
import SharedLayout from './components/SharedLayout/SharedLayout';
import UsersList from './components/UserList/UserList';

function App() {
	return (
		<Routes>
			<Route path='/' element={<SharedLayout />}>
				<Route index element={<UsersList />} />
			</Route>
		</Routes>
	);
}

export default App;
