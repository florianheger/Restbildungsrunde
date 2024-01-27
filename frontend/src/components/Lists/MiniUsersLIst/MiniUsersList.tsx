import { userAvatarURL } from '../../../constants/userAvatarURL';
import { UsersListProps } from '../UsersList.types';
import { MiniAvatarWrapper, MiniDataWrapper, MiniNameAvatarWrapper, MiniRightPartWrapper, MiniUserAvatar, MiniUsersListDataWrapper, Name, Place, Points } from './MiniUsersList.styled';

function MiniUsersList({ arrData }: UsersListProps) {
	return (
		<MiniUsersListDataWrapper>
			{arrData.map(({ username, points, id }, idx) => {
				return (
					<MiniDataWrapper key={id}>
						<Place>{`\u0023${idx + 7}`}</Place>
						<MiniRightPartWrapper>
							<MiniNameAvatarWrapper>
								<MiniAvatarWrapper>
									<MiniUserAvatar src={userAvatarURL} alt='user avatar' />
								</MiniAvatarWrapper>
								<Name>{username}</Name>
							</MiniNameAvatarWrapper>
							<Points>{points}</Points>
						</MiniRightPartWrapper>
					</MiniDataWrapper>
				);
			})}
		</MiniUsersListDataWrapper>
	);
}

export default MiniUsersList;
