import { userAvatarURL } from '../../../constants/userAvatarURL';
import { Name, RightPartWrapper, UserAvatar, UserAvatarNameWrapper, UserAvatarWrapper, UserInfoWrapper, UserPlaceInfoWrapper } from './BigUsersList.styled';
import { UsersListProps } from '../UsersList.types';

function BigUsersList({arrData}: UsersListProps) {
	return (
		<UserInfoWrapper>
			{arrData.map(({ username, points, id }, idx) => (
				<UserPlaceInfoWrapper key={id}>
					<p>{`\u0023${idx + 1}`}</p>
					<RightPartWrapper>
						<UserAvatarNameWrapper>
							<UserAvatarWrapper>
								<UserAvatar src={userAvatarURL} alt='user avatar' />
							</UserAvatarWrapper>
							<Name>{username}</Name>
						</UserAvatarNameWrapper>
						<p>{points}</p>
					</RightPartWrapper>
				</UserPlaceInfoWrapper>
			))}
		</UserInfoWrapper>
	);
}

export default BigUsersList;