import { useEffect, useState } from 'react';
import {
	UserPlaceInfoWrapper,
	UserAvatarNameWrapper,
	UserAvatarWrapper,
	UserAvatar,
} from './UserList.styled';
import { userAvatar } from '../../constants/userAvatarURL';

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

	return (
		<ul>
			{users.map(({ username, points, id }, idx) => (
				<UserPlaceInfoWrapper key={id}>
						<p>{`\u0023${idx + 1}`}</p>
						<UserAvatarNameWrapper>
							<UserAvatarWrapper>
								<UserAvatar src={userAvatar} alt='user avatar' width={23} />
							</UserAvatarWrapper>
							<p>{username}</p>
						</UserAvatarNameWrapper>
					<p>{points}</p>
				</UserPlaceInfoWrapper>
			))}
		</ul>
	);
}
export default UsersList;
