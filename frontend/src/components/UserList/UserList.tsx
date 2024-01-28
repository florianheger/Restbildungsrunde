import { useEffect, useState } from 'react';
import BigUsersList from '../Lists/BigUsersList/BigUsersList';
import MiniUsersList from '../Lists/MiniUsersLIst/MiniUsersList';
import { ListsWrapper, MiniUserListWrapper } from './UserList.styled';

function UsersList() {
	const [users, setUsers] = useState([]);

	const fetchUsers = async () => {
		const response = await fetch('http://localhost:8080/api/user');
		const data = await response.json();
		setUsers(data);
	};

	useEffect(() => {
		fetchUsers();
	}, []);

	if (users.length === 0) {
		return (
			<div>
				<p>Nobody was found, please add some users to the backend</p>
			</div>
		);
	}

	return (
		<ListsWrapper>
			<BigUsersList arrData={users.slice(0, 6)} />

			{users.length > 6 && (
				<MiniUserListWrapper>
					<MiniUsersList arrData={users.slice(6)} />
				</MiniUserListWrapper>
			)}
		</ListsWrapper>
	);
}
export default UsersList;
