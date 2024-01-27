import { userAvatarURL } from '../../../constants/userAvatarURL';
import { UserAvatar, UserAvatarWrapper } from '../../Lists/BigUsersList/BigUsersList.styled';

import Container from '../Container/Container';
import {
	HeaderElementsWrapper, HeaderTitle,StylizedHeader
} from './Header.styled';

function Header() {
	return (
		<StylizedHeader>
			<Container>
				<HeaderElementsWrapper>
					<HeaderTitle>codewars-rest</HeaderTitle>
					<UserAvatarWrapper>
						<UserAvatar src={userAvatarURL} alt='user avatar' />
					</UserAvatarWrapper>
				</HeaderElementsWrapper>
			</Container>
		</StylizedHeader>
	);
}

export default Header;
