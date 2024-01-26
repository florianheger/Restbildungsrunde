import { useEffect, useState } from 'react';
import { userAvatarURL } from '../../constants/userAvatarURL';
import {
	Name,
	Points,
	RightPartWrapper,
	UserAvatar,
	UserAvatarNameWrapper,
	UserAvatarWrapper,
	UserPlaceInfoWrapper,
} from './UserList.styled';

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
		<>
			<ul>
				{users.slice(0, 6).map(({ username, points, id }, idx) => (
					<UserPlaceInfoWrapper key={id}>
						<p>{`\u0023${idx + 1}`}</p>
						<RightPartWrapper>
							<UserAvatarNameWrapper>
								<UserAvatarWrapper>
									<UserAvatar src={userAvatarURL} alt='user avatar' />
								</UserAvatarWrapper>
								<Name>{username}</Name>
							</UserAvatarNameWrapper>
							<Points>{points}</Points>
						</RightPartWrapper>
					</UserPlaceInfoWrapper>
				))}
			</ul>

			{users.length > 6 && (
				//todo edn this shit
				<ul>
					{users.slice(6).map(({ username, points, id }, idx) => (
						<li key={id}>
							{username}, {points}
						</li>
					))}
				</ul>
			)}
		</>
	);
}
export default UsersList;
